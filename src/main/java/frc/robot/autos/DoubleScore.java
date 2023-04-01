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
import frc.robot.commands.drive.MoveToPos;
import frc.robot.commands.wrist.WristIntakeIn;
import frc.robot.commands.wrist.WristIntakeRelease;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.WristIntake;


public class DoubleScore extends TrajectoryBase {

    private double maxVel = 6;
    private double maxAccel = 3;
    // private double armAngle = -60.0;
    // private double wristAngle = 3.0;
    Pose2d aprilTag8 = FieldConstants.aprilTags.get(8).toPose2d();


    public DoubleScore(Swerve swerve, Arm arm, WristIntake intake) {
        super(swerve);

        SecondGamePiece secGamePieSco = new SecondGamePiece(swerve, arm, intake);
        PathPlannerTrajectory trajectory8 = PathPlanner.loadPath("DoubleScore8", maxVel, maxAccel);
        PPSwerveControllerCommand DoubleScore8 = baseSwerveCommand(trajectory8);
        MoveToPos move8 = new MoveToPos(swerve, () -> get8position(), true, 0.07, 3.0);
        MoveToPos cubePose = new MoveToPos(swerve, () -> getCubePose(), true, 0.07, 3.0);
        MoveToPos midPose = new MoveToPos(swerve, () -> getMidPose(), true, 0.07, 3.0);

        HashMap<String, Command> eventMap = new HashMap<>();
        eventMap.put("Release Intake", new WristIntakeIn(intake).withTimeout(1.2));
        // eventMap.put("Go Home", new DockArm(arm, intake));
        FollowPathWithEvents DoubleScore8Events =
            new FollowPathWithEvents(DoubleScore8, trajectory8.getMarkers(), eventMap);

        addCommands(move8, new WristIntakeIn(intake).withTimeout(0.5), midPose,
            cubePose.alongWith(new WristIntakeRelease(intake).withTimeout(2)), midPose, move8,
            new WristIntakeIn(intake).withTimeout(2.0));

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


}
