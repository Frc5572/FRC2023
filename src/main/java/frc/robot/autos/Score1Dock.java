package frc.robot.autos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.lib.util.FieldConstants;
import frc.robot.commands.arm.DockArm;
import frc.robot.commands.drive.MoveToPos;
import frc.robot.commands.drive.MoveToScore;
import frc.robot.commands.wrist.WristIntakeRelease;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.WristIntake;

/**
 * Score1Dock Auto.
 */
public class Score1Dock extends SequentialCommandGroup {

    Swerve swerve;
    Pose2d aprilTag8 = FieldConstants.aprilTags.get(8).toPose2d();
    Pose2d aprilTag6 = FieldConstants.aprilTags.get(6).toPose2d();

    /**
     * Auto constructor
     *
     * @param swerve Swerve
     * @param arm Arm
     * @param wristIntake Wrist Intake
     */
    public Score1Dock(Swerve swerve, Arm arm, WristIntake wristIntake) {
        this.swerve = swerve;
        MoveToScore moveToScore = new MoveToScore(swerve, arm, wristIntake);
        ParallelRaceGroup wristIntakeRelease = new WristIntakeRelease(wristIntake).withTimeout(.5);
        MoveToPos move6 = new MoveToPos(swerve, () -> get6position(), true);
        MoveToPos move8 = new MoveToPos(swerve, () -> get8position(), true);
        ConditionalCommand cond = new ConditionalCommand(move6, move8, () -> chooseSide());
        ParallelRaceGroup dockArm = new DockArm(arm, wristIntake).withTimeout(1);
        CrossAndDock crossAndDock = new CrossAndDock(swerve, arm, wristIntake);

        addCommands(moveToScore, wristIntakeRelease,
            cond.alongWith(new WaitCommand(.5).andThen(dockArm)), crossAndDock);
    }

    /**
     * Boolean to choose which side to cross community zone
     *
     * @return True if closer to feeder station, False if closer to Scoring Table
     */
    private boolean chooseSide() {
        Pose2d pose2d = swerve.getPose();
        double midPlatform =
            Units.inchesToMeters(59.39) + FieldConstants.Community.chargingStationWidth / 2;
        return pose2d.getY() > midPlatform;
    }

    /**
     * Create the Pose2d closer to scoring table
     *
     * @return Pose2d on scoring table side
     */
    private Pose2d get8position() {
        double x = aprilTag8.getX() + Units.inchesToMeters(50);
        double y = Units.inchesToMeters(59.39 / 2);
        return new Pose2d(x, y, swerve.getPose().getRotation());
    }


    /**
     * Create the Pose2d closer to feeder station
     *
     * @return Pose2d on feeder station side
     */
    private Pose2d get6position() {
        double x = aprilTag6.getX() + Units.inchesToMeters(50);
        double y = aprilTag6.getY() + Units.inchesToMeters(30);
        return new Pose2d(x, y, swerve.getPose().getRotation());
    }
}
