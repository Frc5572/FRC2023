package frc.robot.autos;

import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.Swerve;

public class PPExample extends SequentialCommandGroup {
    public Swerve swerve;

    private PIDController pidX = new PIDController(Constants.SwerveTransformPID.PID_XKP,
        Constants.SwerveTransformPID.PID_XKI, Constants.SwerveTransformPID.PID_XKD);
    private PIDController pidTheta = new PIDController(Constants.SwerveTransformPID.PID_TKP,
        Constants.SwerveTransformPID.PID_TKI, Constants.SwerveTransformPID.PID_TKD);

    public PPExample(Swerve swerve) {
        this.swerve = swerve;
        PathPlannerPath examplepath = PathPlannerPath.fromPathFile("Example Path");
        FollowPathCommand exmpmpleCommand =
            new FollowPathCommand(examplepath, null, null, null, null, null, null, swerve);

    }
}
