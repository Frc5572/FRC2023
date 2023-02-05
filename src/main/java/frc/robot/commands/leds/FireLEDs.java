package frc.robot.commands.leds;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LEDs;

/**
 * Command to move LEDs back and forth like a Cylon eye
 */
public class FireLEDs extends CommandBase {
    private final LEDs leds;
    private int end, start;
    private int cooling, sparking, speedDelay;
    private byte[] heat;

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
        this.end = leds.getEnd();
        this.start = leds.getStart();
        this.cooling = 55;
        this.sparking = 120;
        this.speedDelay = 15;
        this.heat = new byte[leds.getLength()];
        addRequirements(leds);
    }

    @Override
    public void execute() {
        // Step 1. Cool down every cell a little
        for (int i = start; i < end; i++) {
            int cooldown = (int) Math.random() * (((cooling * 10) / leds.getLength()) + 2);
            int k = i - start;
            if (cooldown > heat[k]) {
                heat[k] = 0;
            } else {
                heat[k] = (byte) (heat[k] - cooldown);
            }
        }
        // Step 2. Heat from each cell drifts 'up' and diffuses a little
        for (int i = end - 1; i >= start + 2; i--) {
            int k = i - start;
            heat[k] = (byte) ((heat[k - 1] + heat[k - 2] + heat[k - 2]) / 3);
        }
        // Step 3. Randomly ignite new 'sparks' near the bottom
        if ((int) Math.random() * 255 < this.sparking) {
            int y = (int) Math.random() * 7;
            heat[y] = (byte) (heat[y] + (int) (Math.random() * (255 - 160)) + 160);
            // heat[y] = random(160,255);
        }
        // Step 4. Convert heat to LED colors
        for (int j = start; j < end; j++) {
            int k = j - start;
            setPixelHeatColor(j, heat[k]);
        }
        leds.setData();
        Timer.delay(speedDelay / 1000);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

    void setPixelHeatColor(int Pixel, byte temperature) {
        // Scale 'heat' down from 0-255 to 0-191
        byte t192 = (byte) Math.round((temperature / 255.0) * 191);

        // calculate ramp up from
        byte heatramp = (byte) (t192 & 0x3F); // 0..63
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
}
