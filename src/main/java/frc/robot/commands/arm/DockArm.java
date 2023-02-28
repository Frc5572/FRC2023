package frc.robot.commands.arm;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.MoveArm;
import frc.robot.commands.dropintake.MoveDDIntake;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DropIntake;
import frc.robot.subsystems.WristIntake;

/**
 * Creates command to move arm to docked position. (inside robot)
 */
public class DockArm extends SequentialCommandGroup {

    /**
     * Requirements for the command.
     *
     * @param arm Arm subsystem.
     */
    public DockArm(Arm arm, DropIntake dIntake, WristIntake wristIntake) {
        addRequirements(arm, dIntake);

        MoveDDIntake moveDDIntake = new MoveDDIntake(dIntake, dIntake.position1);
        InstantCommand closeGrabber = new InstantCommand(() -> wristIntake.closeGrabber());
        ParallelRaceGroup moveArm = new MoveArm(arm, 45, 0).until(() -> arm.checkElevatorAligned());
        MoveArm moveArm2 = new MoveArm(arm, 0, 0);
        MoveDDIntake moveDDIntake2 = new MoveDDIntake(dIntake, dIntake.position3);

        addCommands(moveDDIntake.alongWith(closeGrabber), moveArm,
            moveArm2.alongWith(moveDDIntake2));
    }

}
