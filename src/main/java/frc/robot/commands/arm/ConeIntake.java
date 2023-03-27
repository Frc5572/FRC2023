package frc.robot.commands.arm;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.lib.util.ArmPosition;
import frc.robot.subsystems.Arm;

/**
 * This command will move the arm to a requested angle.
 */
public class ConeIntake extends SequentialCommandGroup {
    private double armAngle = -66.0;
    private double wristAngle = 35.0;

    /**
     * Requirements for the command.
     *
     * @param arm Arm subsystem.
     */
    public ConeIntake(Arm arm) {
        addRequirements(arm);
        MoveArm moveArm2 = new MoveArm(arm, () -> new ArmPosition(armAngle, false, wristAngle));
        addCommands(moveArm2);
    }
}