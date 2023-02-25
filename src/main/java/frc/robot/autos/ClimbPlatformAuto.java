package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ClimbPlatform;
import frc.robot.subsystems.Swerve;

public class ClimbPlatformAuto extends SequentialCommandGroup {

    public ClimbPlatformAuto(Swerve swerve) {
        addRequirements(swerve);


        // MoveToPos rotate = new MoveToPos(swerve)
        ClimbPlatform climb = new ClimbPlatform(swerve);

        addCommands(climb);
    }

}
