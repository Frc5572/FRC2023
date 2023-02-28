package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.lib.util.FieldConstants;
import frc.robot.subsystems.Swerve;

/**
 * Autonomous that aligns limelight then executes a trajectory.
 */
public class P0 extends SequentialCommandGroup {
    Swerve swerve;

    /**
     * Autonomous that aligns limelight then executes a trajectory.
     *
     * @param swerve swerve subsystem
     */
    public P0(Swerve swerve) {
        // PathPlannerTrajectory p0 = PathPlanner.loadPath("P0", 6, 3);
        // PPSwerveControllerCommand firstCommand = baseSwerveCommand(p0);
        // PathPlannerState initialState = p0.getInitialState();
        // TurnToAngle firstCommand = new TurnToAngle(swerve, 250, false);

        addCommands(new InstantCommand(
            () -> swerve.resetOdometry(FieldConstants.aprilTags.get(5).toPose2d())));

    }
}
