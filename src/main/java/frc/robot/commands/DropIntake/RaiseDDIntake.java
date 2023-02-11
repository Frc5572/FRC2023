package frc.robot.commands.DropIntake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DropIntake;

/**
 * This command will enable the intake on the wrist and then stop the intake once a set of touch
 * sensors are being touched.
 */
public class RaiseDDIntake extends CommandBase {
    private DropIntake intake;

    public RaiseDDIntake(DropIntake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.intakeCubeDeploy();
    }

    @Override
    public void end(boolean interrupt) {
        intake.stopDrop();
    }

    @Override
    public boolean isFinished() {
        return intake.checkIfAligned(321.0);
    }
}
