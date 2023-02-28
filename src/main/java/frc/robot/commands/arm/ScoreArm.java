package frc.robot.commands.arm;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.MoveArm;
import frc.robot.commands.dropintake.MoveDDIntake;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DropIntake;
import frc.robot.subsystems.WristIntake;

/**
 * Creates command to move arm to scoring position.
 */
public class ScoreArm extends SequentialCommandGroup {

    /**
     * Requirements for the command.
     *
     * @param arm Arm subsystem.
     */
    public ScoreArm(Arm arm, DropIntake dIntake, WristIntake wristIntake, double armAngle,
        double elevatorPosition) {
        elevatorPosition = MathUtil.clamp(elevatorPosition, 0, arm.elevatorMaxEncoder);
        addRequirements(arm, dIntake);

        MoveDDIntake moveDDIntake = new MoveDDIntake(dIntake, dIntake.position1);
        InstantCommand closeGrabber = new InstantCommand(() -> wristIntake.closeGrabber());
        ParallelRaceGroup moveArm =
            new MoveArm(arm, armAngle, 0).until(() -> arm.getAngleMeasurement1() > 45);
        MoveArm moveArm2 = new MoveArm(arm, armAngle, elevatorPosition);
        MoveDDIntake moveDDIntake2 = new MoveDDIntake(dIntake, dIntake.position3);

        addCommands(moveDDIntake.alongWith(closeGrabber), moveArm,
            moveArm2.alongWith(moveDDIntake2));
    }

}
