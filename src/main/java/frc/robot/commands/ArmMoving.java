package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

/**
 * This command will move the arm to a requested angle.
 */
public class ArmMoving extends CommandBase {
    private Arm arm;
    private double angle;

    /**
     * Requirements for the command.
     *
     * @param arm Arm subsystem.
     * @param goal Goal at which the arm should move to.
     */
    public ArmMoving(Arm arm, double goal) {
        this.arm = arm;
        this.angle = goal;
        addRequirements(arm);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        arm.armToAngle(angle);
    }

    @Override
    public void end(boolean interrupt) {
        arm.motorsStop();
    }

    @Override
    public boolean isFinished() {
        return Math.abs(arm.getAngleMeasurement() - angle) < 3;
    }
}
