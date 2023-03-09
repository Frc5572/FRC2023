package frc.robot.autos;

import java.util.HashMap;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.FollowPathWithEvents;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.lib.util.FieldConstants;
import frc.robot.RobotContainer;
import frc.robot.commands.drive.MoveToEngage;
import frc.robot.commands.wrist.WristIntakeIn;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.WristIntake;

public class SecondGamePiece extends TrajectoryBase {

    private double maxVel = 0;
    private double maxAccel = 0;

    public SecondGamePiece(Swerve swerve, Arm arm, WristIntake intake) {
        super(swerve);
        PathPlannerTrajectory trajectory6 =
            PathPlanner.loadPath("Second Game Piece - 6", maxVel, maxAccel);
        PathPlannerTrajectory trajectory8 =
            PathPlanner.loadPath("Second Game Piece - 8", maxVel, maxAccel);
        PPSwerveControllerCommand secondGamePiece6 = baseSwerveCommand(trajectory6);
        PPSwerveControllerCommand secondGamePiece8 = baseSwerveCommand(trajectory8);
        HashMap<String, Command> eventMap = new HashMap<>();
        eventMap.put("Intake On", new WristIntakeIn(intake));
        FollowPathWithEvents secondGamePiece6Events =
            new FollowPathWithEvents(secondGamePiece6, trajectory6.getMarkers(), eventMap);
        FollowPathWithEvents secondGamePiece8Events =
            new FollowPathWithEvents(secondGamePiece8, trajectory8.getMarkers(), eventMap);
        MoveToEngage engage = new MoveToEngage(swerve, arm, intake);
        ConditionalCommand cond = new ConditionalCommand(secondGamePiece6Events,
            secondGamePiece8Events, () -> chooseSide());
        ConditionalCommand cond1 = new ConditionalCommand(engage, new InstantCommand(),
            () -> RobotContainer.enableDockWidget.getBoolean(true));

        addCommands(cond, cond1);

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
}
