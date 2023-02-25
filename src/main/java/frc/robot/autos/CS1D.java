package frc.robot.autos;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.modules.AutoBase;
import frc.robot.subsystems.Swerve;

/**
 * Autonomous that aligns limelight then executes a trajectory.
 */
public class CS1D extends AutoBase {
    Swerve swerve;

    /**
     * Autonomous that aligns limelight then executes a trajectory.
     *
     * @param swerve swerve subsystem
     */
    public CS1D(Swerve swerve) {
        super(swerve);
        PathPlannerTrajectory cs1d = PathPlanner.loadPath("CS1D", 6, 3);
        PPSwerveControllerCommand firstCommand = baseSwerveCommand(cs1d);
        PathPlannerState initialState = cs1d.getInitialState();
        // TurnToAngle firstCommand = new TurnToAngle(swerve, 250, false);

        addCommands(new InstantCommand(() -> swerve.resetFieldRelativeOffset()),
            new InstantCommand(
                () -> swerve.resetOdometry(new Pose2d(initialState.poseMeters.getTranslation(),
                    initialState.holonomicRotation))),
            firstCommand);
    }
}
