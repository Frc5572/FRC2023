package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

/**
 * This command will move the arm to a requested angle.
 */
public class MoveArm extends CommandBase {
    private Arm arm;
    private double armAngle;
    private double elevatorPosition;

    /**
     * Requirements for the command.
     *
     * @param arm Arm subsystem.
     * @param goal Goal at which the arm should move to.
     */
    public MoveArm(Arm arm, double armAngle, double elevatorPosition) {
        this.arm = arm;
        this.armAngle = armAngle;
        this.elevatorPosition = MathUtil.clamp(elevatorPosition, 0, arm.elevatorMaxEncoder);
        addRequirements(arm);
    }

    @Override
    public void initialize() {
        arm.enablePID();
        arm.setArmGoal(armAngle);
        arm.setElevatorGoal(elevatorPosition);
    }

    @Override
    public boolean isFinished() {
        return arm.checkArmInPosition() && arm.checkElevatorAligned();
    }
}