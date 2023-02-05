package frc.robot.commands.leds;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.lib.math.Conversions;
import frc.robot.subsystems.LEDs;

/**
 * Command to move LEDs back and forth like a Cylon eye
 */
public class MeteorRain extends CommandBase {
    private final LEDs leds;
    private int NUM_LEDS, start;
    private boolean meteorRandomDecay = true;
    private int meteorTrailDecay = 64;
    private int meteorSize = 10;

    public MeteorRain(LEDs leds) {
        this.leds = leds;
        this.NUM_LEDS = leds.getLength();
        this.start = 0;
        addRequirements(leds);
    }

    @Override
    public void initialize() {
        leds.setColor(Color.kBlack);
    }

    @Override
    public void execute() {
        // leds.setColor(Color.kBlack);
        for (int i = 0; i < NUM_LEDS + NUM_LEDS; i++) {


            // fade brightness all LEDs one step
            for (int j = 0; j < NUM_LEDS; j++) {
                if (!meteorRandomDecay || (Conversions.random(10) > 5)) {
                    fadeToBlack(j, meteorTrailDecay);
                }
            }

            // draw meteor
            for (int j = 0; j < meteorSize; j++) {
                if ((i - j < NUM_LEDS) && (i - j >= 0)) {
                    leds.setColor(i - j, Color.kWhite);
                }
            }

            leds.setData();
            Timer.delay(30 / 1000.0);
        }
    }


    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

    private void fadeToBlack(int ledNo, int fadeValue) {
        Color oldColor;
        int r, g, b;

        oldColor = leds.getColor(ledNo);
        r = (int) oldColor.red;
        g = (int) oldColor.green;
        b = (int) oldColor.blue;
        r = (r <= 10) ? 0 : (int) r - (r * fadeValue / 256);
        g = (g <= 10) ? 0 : (int) g - (g * fadeValue / 256);
        b = (b <= 10) ? 0 : (int) b - (b * fadeValue / 256);

        leds.setRGB(ledNo, r, g, b);
    }
}
