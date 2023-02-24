package frc.robot.modules;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Swerve;

/**
 * Parent class for all autonomous commands
 */
public class AutoBase extends SequentialCommandGroup {
    public Swerve swerve;

    public static final PIDController thetaController =
        new PIDController(Constants.AutoConstants.KP_THETA_CONTROLLER, 0, 0);

    /**
     * Autonomous that aligns limelight then executes a trajectory.
     *
     * @param swerve swerve subsystem
     */
    public AutoBase(Swerve swerve) {
        this.swerve = swerve;
        addRequirements(swerve);
        thetaController.enableContinuousInput(-Math.PI, Math.PI);
    }

    /**
     * Creates a SwerveControllerCommand from a Trajectory
     *
     * @param trajectory Trajectory to run
     * @return A SwerveControllerCommand for the robot to move
     */
    public SwerveControllerCommand baseSwerveCommand(Trajectory trajectory) {
        SwerveControllerCommand command = new SwerveControllerCommand(trajectory, swerve::getPose,
            Constants.Swerve.SWERVE_KINEMATICS,
            new PIDController(Constants.AutoConstants.KP_X_CONTROLLER, 0, 0),
            new PIDController(Constants.AutoConstants.KP_Y_CONTROLLER, 0, 0), thetaController,
            swerve::setModuleStates, swerve);
        return command;
    }

    /**
     * Creates a SwerveController Command using a Path Planner Trajectory
     *
     * @param trajectory a Path Planner Trajectory
     * @return A SwerveControllerCommand for the robot to move
     */
    public PPSwerveControllerCommand baseSwerveCommand(PathPlannerTrajectory trajectory) {
        PPSwerveControllerCommand command = new PPSwerveControllerCommand(trajectory,
            swerve::getPose, Constants.Swerve.SWERVE_KINEMATICS,
            new PIDController(Constants.AutoConstants.KP_X_CONTROLLER, 0, 0),
            new PIDController(Constants.AutoConstants.KP_Y_CONTROLLER, 0, 0), thetaController,
            swerve::setModuleStates, swerve);
        return command;
    }
}
