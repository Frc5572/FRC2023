package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LEDs;

public class RainbowLEDs extends CommandBase {
    private LEDs leds;
    private int m_rainbowFirstPixelHue = 0;
    private int ledLength;

    public RainbowLEDs(LEDs leds) {
        this.leds = leds;
        ledLength = leds.getLength();;
    }

    @Override
    public void execute() {
        for (var i = 1; i < ledLength; i++) {
            // calculate the hue
            final var hue = (m_rainbowFirstPixelHue + (i * 180 / ledLength)) % 180;
            leds.setHSV(i, hue, 255, 128);
        }
        // increase by to make the rainbow "move"
        m_rainbowFirstPixelHue += 3;
        // check bounds
        m_rainbowFirstPixelHue %= 180;
        leds.setData();
    }
}

