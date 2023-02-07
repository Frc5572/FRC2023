package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DropIntake;
import frc.robot.subsystems.WristIntake;

/**
 * This command will deploy the dropdown intake and run the intake motors on the dropdown and wrist
 * and then stop the intakes once the cone touch sensors are being touched and retracts the
 * dropdown.
 */
public class ConeDropIntake extends CommandBase {
    private DropIntake dIntake;
    private WristIntake wIntake;

    public ConeDropIntake(DropIntake dropIntake, WristIntake wristIntake) {
        this.dIntake = dropIntake;
        this.wIntake = wristIntake;
        addRequirements(dIntake, wIntake);
    }

    @Override
    public void initialize() {
        dIntake.intakeConeDeploy();
    }

    @Override
    public void execute() {
        dIntake.intake();
        wIntake.intake();
    }

    @Override
    public void end(boolean interrupt) {
        dIntake.stop();
        wIntake.stop();
        dIntake.intakeRetract();
    }

    @Override
    public boolean isFinished() {
        return wIntake.getConeSensor();
    }
}
