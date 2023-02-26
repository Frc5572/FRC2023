package frc.robot.commands.arm;

import java.util.function.Supplier;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.lib.util.ArmPosition;
import frc.robot.Constants;
import frc.robot.subsystems.Arm;

/**
 * This command will move the arm to a requested angle.
 */
public class MoveArm extends CommandBase {
    private Arm arm;
    private double armAngle;
    private double elevatorPosition;
    private double wristAngle;

    /**
     * Requirements for the command.
     *
     * @param arm Arm subsystem.
     * @param goal Goal at which the arm should move to.
     */
    public MoveArm(Arm arm, double armAngle, double elevatorPosition) {
        this.arm = arm;
        this.armAngle = armAngle;
        this.elevatorPosition = MathUtil.clamp(elevatorPosition, 0, Constants.Elevator.MAX_ENCODER);
        addRequirements(arm);
    }

    /**
     * Requirements for the command.
     *
     * @param arm Arm subsystem.
     * @param goal Goal at which the arm should move to.
     */
    public MoveArm(Arm arm, Supplier<ArmPosition> positionSupplier) {
        this.arm = arm;
        ArmPosition position = positionSupplier.get();
        this.armAngle = position.getArmAngle();
        this.elevatorPosition =
            MathUtil.clamp(position.getElevatorPosition(), 0, Constants.Elevator.MAX_ENCODER);
        this.wristAngle = position.getWristAngle();
        addRequirements(arm);
    }

    @Override
    public void initialize() {
        arm.enablePID();
        arm.setArmGoal(armAngle);
        arm.setWristOffset(wristAngle);
        // arm.setElevatorGoal(elevatorPosition);
    }

    @Override
    public boolean isFinished() {
        return arm.checkArmInPosition() && arm.checkElevatorAligned();
    }
}
