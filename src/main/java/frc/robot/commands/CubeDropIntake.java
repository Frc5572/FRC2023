package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DropIntake;
import frc.robot.subsystems.WristIntake;

/**
 * This command will deploy the dropdown intake and run the intake motors on the dropdown and wrist
 * and then stop the intakes once the cube touch sensors are being touched and retracts the
 * dropdown.
 */
public class CubeDropIntake extends CommandBase {
    private DropIntake dIntake;
    private WristIntake wIntake;

    /**
     * Requirements for the command.
     *
     * @param dropIntake Dropdown intake subsystem
     * @param wristIntake Wrist intake subsystem
     */
    public CubeDropIntake(DropIntake dropIntake, WristIntake wristIntake) {
        this.dIntake = dropIntake;
        this.wIntake = wristIntake;
        addRequirements(dIntake, wIntake);
    }

    @Override
    public void initialize() {
        dIntake.intakeCubeDeploy();
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
        return wIntake.getCubeSensor();
    }
}
