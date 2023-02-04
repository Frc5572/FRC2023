package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.WristIntake;

public class IntakeWrist extends CommandBase {
    private WristIntake wIntake;

    public IntakeWrist(WristIntake wristIntake) {
        this.wIntake = wristIntake;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        wIntake.intake();
    }

    @Override
    public void end(boolean interrupt) {
        wIntake.stop();
    }

    @Override
    public boolean isFinished() {
        return wIntake.getConeSensor() || wIntake.getCubeSensor();
    }
}
