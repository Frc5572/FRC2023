package frc.robot.commands.arm;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.lib.util.ArmPosition;
import frc.lib.util.Scoring;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DropIntake;
import frc.robot.subsystems.WristIntake;

public class ScoreArm extends SequentialCommandGroup {

    Supplier<ArmPosition> getScoreParameters = () -> Scoring.getScoreParameters();

    /**
     * Requirements for the command.
     *
     * @param arm Arm subsystem.
     * @param goal Goal at which the arm should move to.
     */
    public ScoreArm(Arm arm, DropIntake dIntake, WristIntake wristIntake) {
        addRequirements(arm, dIntake);

        InstantCommand closeGrabber = new InstantCommand(() -> wristIntake.closeGrabber());
        WaitCommand waitForGrabber = new WaitCommand(.5);
        MoveElevator moveElevator = new MoveElevator(arm, 0);
        SequentialCommandGroup part1 = closeGrabber.andThen(waitForGrabber).andThen(moveElevator);
        MoveArm moveArmFinal = new MoveArm(arm, getScoreParameters);
        ConditionalCommand condition = new ConditionalCommand(part1.andThen(moveArmFinal),
            moveArmFinal, () -> arm.getAverageArmAngle() < 20);

        addCommands(condition);
    }
}
