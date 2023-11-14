package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.swerve2.Swerve;
import frc.robot.subsystems.wrist_intake.WristIntake;


/**
 * Move into scoring position
 */
public class MoveToEngage extends SequentialCommandGroup {
    Swerve swerve;

    /**
     * Move into scoring position
     */
    public MoveToEngage(Swerve swerve, Arm arm, WristIntake wristIntake) {
        this.swerve = swerve;
        // ConditionalCommand cond = new ConditionalCommand(new TurnToAngle(swerve, 0, false),
        // new TurnToAngle(swerve, 180, false), () -> centerField(swerve));

        ClimbPlatform climbPlatform = new ClimbPlatform(swerve);
        addCommands(climbPlatform);
    }
}
