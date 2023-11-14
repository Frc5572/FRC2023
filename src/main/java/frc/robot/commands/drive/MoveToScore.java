package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.lib.util.Scoring;
import frc.robot.commands.arm.ScoreArm;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.swerve2.Swerve;
import frc.robot.subsystems.wrist_intake.WristIntake;


/**
 * Move into scoring position
 */
public class MoveToScore extends SequentialCommandGroup {

    /**
     * Move into scoring position
     */
    public MoveToScore(Swerve swerve, Arm arm, WristIntake wristIntake) {
        // addCommands(
        // new InstantCommand(
        // () -> SmartDashboard.putNumber("Target Pose Y", Scoring.getScorPose2d().getY())),
        // new InstantCommand(
        // () -> SmartDashboard.putNumber("Target Pose X", Scoring.getScorPose2d().getX())),
        // new InstantCommand(() -> swerve.resetOdometry(Scoring.getScorPose2d())));
        MoveToPos moveToPos = new MoveToPos(swerve, Scoring::getPreScorePosition, false, 0.2);
        ParallelRaceGroup moveArm = new ScoreArm(arm, wristIntake).withTimeout(1.5);
        MoveToPos moveToScoreFinal = new MoveToPos(swerve, Scoring::getScorPose2d, false);
        addCommands(moveToPos, moveArm, moveToScoreFinal);
    }
}
