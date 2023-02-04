package frc.robot.commands.leds;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LEDs;

/**
 * Command to flash the LED strip between 2 colors
 */
public class MorseCode extends CommandBase {
    private LEDs leds;
    private int ledLength;
    private int flashingDelay = 0;
    private Color color;
    private Color altColor;

    /**
     * Command to flash the LED strip between 2 colors
     *
     * @param leds LED Subsystem
     * @param color The first color
     * @param altColor The second color
     */
    public MorseCode(LEDs leds, Color color, Color altColor) {
        this.leds = leds;
        this.color = color;
        this.altColor = altColor;
        ledLength = leds.getLength();
        addRequirements(leds);
    }


    @Override
    public void execute() {

    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}

