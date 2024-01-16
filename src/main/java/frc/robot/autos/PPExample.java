package frc.robot.autos;

import com.pathplanner.lib.path.PathPlannerPath;
import frc.robot.subsystems.Swerve;

public class PPExample {
    public Swerve swerve;

    public PPExample(Swerve swerve) {
        this.swerve = swerve;
        PathPlannerPath examplepath = new PathPlannerPath.fromPathFile("Example Path")

    }
}
