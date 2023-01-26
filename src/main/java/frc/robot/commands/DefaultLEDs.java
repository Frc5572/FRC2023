package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LEDs;

public class DefaultLEDs extends CommandBase {
    private final LEDs leds;


    public DefaultLEDs(LEDs subsystem) {
        this.leds = subsystem;
        addRequirements(leds);
    }

    @Override
    public void execute() {
        if (leds.pattern == 0) {
            // leds.
        } else if (leds.pattern == 1) {
            // leds.
        } else if (leds.pattern == 2) {
            // leds.
        }
    }

}
