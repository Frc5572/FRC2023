package frc.robot.commands.leds;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LEDs;

/**
 * Command to flash the LED strip between red and blue like police lights
 */
public class PoliceLEDs extends CommandBase {
    private LEDs leds;
    private int policeDelay = 0;
    private int ledLength, ledStart, ledEnd, ledLengthHalf;

    /**
     * Command to flash the LED strip between red and blue like police lights
     *
     * @param leds LED subsystem
     */
    public PoliceLEDs(LEDs leds) {
        this.leds = leds;
        ledEnd = leds.getEnd();
        ledStart = leds.getStart();
        ledLength = ledEnd - ledStart;
        ledLengthHalf = ledStart + ledLength / 2;
        addRequirements(leds);
    }

    @Override
    public void execute() {
        if (policeDelay < 10) {
            for (var i = ledStart; i < ledLengthHalf; i++) {
                leds.setColor(i, Color.kRed);
            }
            for (var i = ledLengthHalf; i < ledEnd; i++) {
                leds.setColor(i, Color.kBlack);
            }
        } else {
            for (var i = ledStart; i < ledLengthHalf; i++) {
                leds.setColor(i, Color.kBlack);
            }
            for (var i = ledLengthHalf; i < ledEnd; i++) {
                leds.setColor(i, Color.kBlue);
            }
        }
        leds.setData();
        policeDelay += 1;
        policeDelay %= 21;
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}

