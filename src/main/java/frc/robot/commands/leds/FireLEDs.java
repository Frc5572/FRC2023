package frc.robot.commands.leds;

import java.util.Arrays;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LEDs;

/**
 * Command to move LEDs back and forth like a Cylon eye
 */
public class FireLEDs extends CommandBase {
    private final LEDs leds;
    private int end, start;
    private int cooling, sparking, speedDelay;
    private int[] heat;

    /**
     * Command to move LEDs back and forth like a Cylon eye
     *
     * @param leds LED subsystem
     * @param color Color to which to set the LEDs
     * @param count The number of LEDs in the moving dot. Best is an odd number.
     * @param inverted Whether to invert the Color that moves.
     */
    public FireLEDs(LEDs leds) {
        this.leds = leds;
        this.end = leds.getLength();
        this.start = 0;
        this.cooling = 100;
        this.sparking = 50;
        this.speedDelay = 15;
        this.heat = new int[leds.getLength()];
        addRequirements(leds);
    }

    @Override
    public void initialize() {
        Arrays.fill(heat, (int) 127);
        for (int i = start; i < end; i++) {
            // System.out.println((int) heat[i]);
        }
    }

    @Override
    public void execute() {
        // Step 1. Cool down every cell a little
        for (int i = start; i < end; i++) {
            int cooldown = random(((cooling * 10) / leds.getLength()) + 2, 0);
            if (cooldown > heat[i]) {
                heat[i] = 0;
            } else {
                heat[i] = heat[i] - cooldown;
            }
        }

        // Step 2. Heat from each cell drifts 'up' and diffuses a little
        for (int i = end - 1; i >= start + 2; i--) {
            heat[i] = (int) ((heat[i - 1] + heat[i - 2] + heat[i - 2]) / 3);
        }
        // Step 3. Randomly ignite new 'sparks' near the bottom
        if (random(255, 0) < this.sparking) {
            int y = random(7, 0);
            heat[y] = heat[y] + random(255, 160);
            // heat[y] = random(160,255);
        }
        // Step 4. Convert heat to LED colors
        for (int j = start; j < end; j++) {
            setPixelHeatColor(j, heat[j]);
        }
        leds.setData();
        // Timer.delay(1 / 5);
    }


    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

    void setPixelHeatColor(int Pixel, int temperature) {
        // Scale 'heat' down from 0-255 to 0-191
        int t192 = (int) Math.round((temperature / 255.0) * 191);

        // calculate ramp up from
        int heatramp = (int) (t192 & 0x3F); // 0..63
        heatramp <<= 2; // scale up to 0..252

        // figure out which third of the spectrum we're in:
        if (t192 > 0x80) { // hottest
            leds.setRGB(Pixel, 255, 255, heatramp);
        } else if (t192 > 0x40) { // middle
            leds.setRGB(Pixel, 255, heatramp, 0);
        } else { // coolest
            leds.setRGB(Pixel, heatramp, 0, 0);
        }
    }

    public int random(int upper, int lower) {
        return (int) (Math.random() * (upper - lower)) + lower;
    }
}
