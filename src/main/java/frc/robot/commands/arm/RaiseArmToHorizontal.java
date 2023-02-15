package frc.robot.commands.arm;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

/**
 * Command to raise the Drop Down Intake to the top position
 */
public class RaiseArmToHorizontal extends CommandBase {
    private Arm arm;

    /**
     * Command to lower the Drop Down Intake to the bottom position
     *
     * @param intake Drop Down Intake
     */
    public RaiseArmToHorizontal(Arm arm) {
        this.arm = arm;
        addRequirements(arm);
    }

    @Override
    public void initialize() {
        arm.setGoal(0);
    }

    // @Override
    // public void end(boolean interrupt) {
    // arm.motorsStop();
    // }

    @Override
    public boolean isFinished() {
        return arm.checkIfAligned(0);
    }
}
