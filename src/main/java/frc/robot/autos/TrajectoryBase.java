package frc.robot.autos;

import com.pathplanner.lib.commands.FollowPathHolonomic;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.swerve.Swerve;

/**
 * Parent class for all autonomous commands
 */
public class TrajectoryBase extends SequentialCommandGroup {
    public Swerve swerve;
    private PIDConstants pidX = new PIDConstants(Constants.SwerveTransformPID.PID_XKP,
        Constants.SwerveTransformPID.PID_XKI, Constants.SwerveTransformPID.PID_XKD);

    private PIDConstants pidTheta = new PIDConstants(Constants.SwerveTransformPID.PID_TKP,
        Constants.SwerveTransformPID.PID_TKI, Constants.SwerveTransformPID.PID_TKD);

    /**
     * Autonomous that aligns limelight then executes a trajectory.
     *
     * @param swerve swerve subsystem
     */
    public TrajectoryBase(Swerve swerve) {
        this.swerve = swerve;
        addRequirements(swerve);
    }

    /**
     * Creates a SwerveController Command using a Path Planner Trajectory
     *
     * @param trajectory a Path Planner Trajectory
     * @return A SwerveControllerCommand for the robot to move
     */
    public FollowPathHolonomic baseSwerveCommand(PathPlannerPath trajectory) {
        FollowPathHolonomic command = new FollowPathHolonomic(trajectory, swerve::getPose,
            swerve::getChassisSpeeds, swerve::setModuleStates, pidX, pidTheta,
            Constants.Swerve.MAX_SPEED, Constants.Swerve.ROBOT_RADIUS, new ReplanningConfig(),
            () -> swerve.shouldFlipPath(), swerve);
        return command;
    }
}
