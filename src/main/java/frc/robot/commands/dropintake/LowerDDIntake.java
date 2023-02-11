package frc.robot.commands.dropintake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DropIntake;

/**
 * Command to raise the Drop Down Intake to the top postion
 */
public class LowerDDIntake extends CommandBase {
    private DropIntake intake;

    /**
     * Command to lower the Drop Down Intake to the bottom postion
     *
     * @param intake Drop Down Intake
     */
    public LowerDDIntake(DropIntake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.intakeRetract();
    }

    @Override
    public void end(boolean interrupt) {
        intake.stopDrop();
    }

    @Override
    public boolean isFinished() {
        return intake.checkIfAligned(216.0);
    }
}
