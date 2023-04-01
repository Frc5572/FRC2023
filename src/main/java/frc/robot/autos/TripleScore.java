package frc.robot.autos;

import java.util.HashMap;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.FollowPathWithEvents;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.util.FieldConstants;
import frc.robot.commands.arm.CubeIntake;
import frc.robot.commands.arm.DockArm;
import frc.robot.commands.arm.ScoreArm;
import frc.robot.commands.drive.MoveToPos;
import frc.robot.commands.wrist.WristIntakeIn;
import frc.robot.commands.wrist.WristIntakeRelease;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.WristIntake;

public class TripleScore extends TrajectoryBase {
    private double maxVel = 6;
    private double maxAccel = 3;
    // private double armAngle = -60.0;
    // private double wristAngle = 3.0;
    Pose2d aprilTag8 = FieldConstants.aprilTags.get(8).toPose2d();

    public TripleScore(Swerve swerve, Arm arm, WristIntake intake) {
        super(swerve);

        DoubleScore doubleScore = new DoubleScore(swerve, arm, intake);
        PathPlannerTrajectory trajectory8 = PathPlanner.loadPath("TripleScore8", maxVel, maxAccel);
        PPSwerveControllerCommand TripleScore8 = baseSwerveCommand(trajectory8);
        MoveToPos move8 = new MoveToPos(swerve, () -> get8position(), true, 0.07, 3.0);
        MoveToPos cubePose = new MoveToPos(swerve, () -> getCubePose(), true, 0.07, 3.0);
        MoveToPos midPose = new MoveToPos(swerve, () -> getMidPose(), true, 0.07, 3.0);
        MoveToPos highPose = new MoveToPos(swerve, () -> getHighPose(), true, 0.07, 3.0);

        HashMap<String, Command> eventMap = new HashMap<>();
        eventMap.put("Intake On", new CubeIntake(arm).alongWith(new WristIntakeRelease(intake)));
        eventMap.put("Go Home", new DockArm(arm, intake).withTimeout(0.6));
        eventMap.put("Release Intake",
            new ScoreArm(arm, intake).andThen(new WristIntakeIn(intake).withTimeout(0.6))
                .andThen(new DockArm(arm, intake).withTimeout(0.6)));
        FollowPathWithEvents TripleScore8Events =
            new FollowPathWithEvents(TripleScore8, trajectory8.getMarkers(), eventMap);

        addCommands(doubleScore, midPose, highPose.alongWith(new CubeIntake(arm)),
            cubePose.alongWith(new WristIntakeRelease(intake).withTimeout(3.0)), highPose, midPose,
            cubePose, move8, new ScoreArm(arm, intake), new WristIntakeIn(intake).withTimeout(2.0));

    }

    private Pose2d get8position() {
        double x = aprilTag8.getX() + Units.inchesToMeters(50);
        double y = Units.inchesToMeters(59.39 / 2);
        return new Pose2d(x, y, Rotation2d.fromDegrees(180));
    }

    private Pose2d getCubePose() {
        double x = Units.inchesToMeters(100);
        double y = Units.inchesToMeters(79.39 / 2);
        return new Pose2d(x, y, Rotation2d.fromDegrees(0));

    }

    private Pose2d getMidPose() {
        double x = Units.inchesToMeters(75);
        double y = Units.inchesToMeters(59.39 / 2);
        return new Pose2d(x, y, Rotation2d.fromDegrees(180));
    }

    private Pose2d getHighPose() {
        double x = Units.inchesToMeters(90);
        double y = Units.inchesToMeters(59.39 / 2);
        return new Pose2d(x, y, Rotation2d.fromDegrees(0));

    }

}
