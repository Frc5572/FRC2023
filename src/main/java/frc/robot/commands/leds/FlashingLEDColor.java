package frc.robot.commands.leds;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LEDs;

public class FlashingLEDColor extends CommandBase {
    private LEDs leds;
    private int ledLength;
    private int flashingDelay = 0;
    private Color color;
    private Color altColor = Color.kBlack;

    public FlashingLEDColor(LEDs leds, Color color, Color altColor) {
        this.leds = leds;
        this.color = color;
        this.altColor = altColor;
        ledLength = leds.getLength();
    }

    public FlashingLEDColor(LEDs leds, Color color) {
        this.leds = leds;
        this.color = color;
        ledLength = leds.getLength();
    }

    @Override
    public void execute() {
        if (flashingDelay < 10) {
            for (var i = 0; i < ledLength; i++) {
                leds.setColor(i, color);
            }
        } else {
            for (var i = 0; i < ledLength; i++) {
                leds.setColor(i, altColor);
            }
        }
        leds.setData();
        flashingDelay++;
        flashingDelay %= 20;
    }
}

