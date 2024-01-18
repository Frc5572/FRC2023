package frc.robot.autos;

import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Swerve;

public class PPExample extends TrajectoryBase {
    public Swerve swerve;

    public PPExample(Swerve swerve) {
        super(swerve);
        PathPlannerPath examplepath = PathPlannerPath.fromPathFile("Example Path");
        Pose2d initialState = examplepath.getPreviewStartingHolonomicPose();
        FollowPathCommand exampleCommand = baseSwerveCommand(examplepath);
        addCommands(new InstantCommand(() -> swerve.zeroGyro()),
            new InstantCommand(() -> swerve.resetOdometry(
                new Pose2d(initialState.getTranslation(), initialState.getRotation()))),
            exampleCommand);
    }

}
