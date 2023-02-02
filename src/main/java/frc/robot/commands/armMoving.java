package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

public class armMoving extends CommandBase {
    private Arm arm;
    private double angle;

    public armMoving(Arm arm, double goal) {
        this.arm = arm;
        this.angle = goal;
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
