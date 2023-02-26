package frc.robot.commands.arm;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DropIntake;
import frc.robot.subsystems.WristIntake;

public class DockArm extends SequentialCommandGroup {

    /**
     * Requirements for the command.
     *
     * @param arm Arm subsystem.
     * @param goal Goal at which the arm should move to.
     */
    public DockArm(Arm arm, DropIntake dIntake, WristIntake wristIntake) {
        addRequirements(arm, dIntake);

        // MoveDDIntake moveDDIntake = new MoveDDIntake(dIntake, dIntake.position1);
        InstantCommand closeGrabber = new InstantCommand(() -> wristIntake.closeGrabber());
        WaitCommand waitForGrabber = new WaitCommand(.5);
        MoveElevator moveElevator = new MoveElevator(arm, 0);
        MoveArm moveArm = new MoveArm(arm, 0, 0);
        // MoveDDIntake moveDDIntake2 = new MoveDDIntake(dIntake, dIntake.position3);

        addCommands(closeGrabber, waitForGrabber, moveElevator, moveArm);
    }

}
