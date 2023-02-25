package frc.robot.commands.wrist;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.WristIntake;

/**
 * Command to raise the Drop Down Intake to the top position
 */
public class WristIntakeRelease extends CommandBase {
    private WristIntake intake;

    /**
     * Command to lower the Drop Down Intake to the bottom position
     *
     * @param intake Drop Down Intake
     */
    public WristIntakeRelease(WristIntake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.openGrabber();
        intake.setMotors(Constants.Wrist.INTAKE_RELEASE_SPEED);
    }

    @Override
    public void end(boolean interrupt) {
        intake.setMotors(Constants.Wrist.INTAKE_STOP_SPEED);
    }
}
