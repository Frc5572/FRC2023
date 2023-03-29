package frc.robot.autos;

import java.util.HashMap;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.FollowPathWithEvents;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.util.FieldConstants;
import frc.robot.commands.arm.DockArm;
import frc.robot.commands.arm.ScoreArm;
import frc.robot.commands.wrist.WristIntakeIn;
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

        HashMap<String, Command> eventMap = new HashMap<>();
        eventMap.put("Release Intake",
            new ScoreArm(arm, intake).andThen(new WristIntakeIn(intake).withTimeout(0.6))
                .andThen(new DockArm(arm, intake).withTimeout(0.6)));
        eventMap.put("Go Home", new DockArm(arm, intake));
        FollowPathWithEvents DoubleScore8Events =
            new FollowPathWithEvents(DoubleScore8, trajectory8.getMarkers(), eventMap);

        addCommands(new WristIntakeIn(intake).withTimeout(0.5), secGamePieSco, DoubleScore8Events);

    }

}
