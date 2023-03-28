package frc.robot.autos;

import java.util.HashMap;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.FollowPathWithEvents;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.util.FieldConstants;
import frc.robot.commands.arm.CubeIntake;
import frc.robot.commands.arm.DockArm;
import frc.robot.commands.arm.ScoreArm;
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

        HashMap<String, Command> eventMap = new HashMap<>();
        eventMap.put("Intake On", new CubeIntake(arm).alongWith(new WristIntakeRelease(intake)));
        eventMap.put("Go Home", new DockArm(arm, intake).withTimeout(0.6));
        eventMap.put("Release Intake", new ScoreArm(arm, intake).andThen(new WristIntakeIn(intake))
            .andThen(new DockArm(arm, intake).withTimeout(0.6)));
        FollowPathWithEvents TripleScore8Events =
            new FollowPathWithEvents(TripleScore8, trajectory8.getMarkers(), eventMap);

        addCommands(doubleScore, TripleScore8Events);

    }
}
