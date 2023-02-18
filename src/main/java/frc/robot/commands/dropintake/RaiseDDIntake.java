package frc.robot.commands.dropintake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DropIntake;

/**
 * Command to raise the Drop Down Intake to the top position
 */
public class RaiseDDIntake extends CommandBase {
    private DropIntake intake;

    /**
     * Command to lower the Drop Down Intake to the bottom position
     *
     * @param intake Drop Down Intake
     */
    public RaiseDDIntake(DropIntake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.intakeRetract();
        // intake.setGoal(intake.position2);
        // intake.enablePID();
    }

    @Override
    public void end(boolean interrupt) {
        intake.stopDrop();
    }

    @Override
    public boolean isFinished() {
        return intake.checkIfAligned(intake.position2);
    }
}
