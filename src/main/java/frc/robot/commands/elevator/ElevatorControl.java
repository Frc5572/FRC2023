package frc.robot.commands.elevator;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

/**
 * Creates the command that allows the elevator to move.
 */
public class ElevatorControl extends CommandBase {
    private Arm arm;
    private double goal;

    /**
     * Requirements for the command.
     *
     * @param arm Arm subsystem.
     * @param goal Goal at which the user is requesting the elevator to go to.
     */
    public ElevatorControl(Arm arm, double goal) {
        this.arm = arm;
        this.goal = goal;
        addRequirements(this.arm);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        arm.setElevatorGoal(goal);
        arm.elevatorToPosition();
    }

    @Override
    public boolean isFinished() {
        return arm.checkElevatorAligned();
    }
}
