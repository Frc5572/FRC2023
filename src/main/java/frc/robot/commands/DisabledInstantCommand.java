package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class DisabledInstantCommand extends InstantCommand {

    /**
     * Creates a new InstantCommand that runs the given Runnable with the given requirements. This
     * command works while the robot is disabled.
     *
     * @param toRun the Runnable to run
     * @param requirements the subsystems required by this command
     */
    public DisabledInstantCommand(Runnable toRun, Subsystem... requirements) {
        super(toRun, requirements);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
