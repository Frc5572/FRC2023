package frc.robot.commands.arm;

import java.util.Map;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DropIntake;
import frc.robot.subsystems.WristIntake;

public class AutoPlace extends SequentialCommandGroup {

    Arm arm;
    DropIntake dropIntake;
    WristIntake wristIntake;
    Map<Integer, Double> grid;

    public AutoPlace(Arm arm, DropIntake dropIntake, WristIntake wristIntake,
        Map<Integer, Double> map) {
        this.arm = arm;
        this.dropIntake = dropIntake;
        this.wristIntake = wristIntake;
        this.grid = map;

        ConditionalCommand part1 = new ConditionalCommand(
            new SequentialCommandGroup(new ArmMoving(this.arm, 0.000), new ConditionalCommand(
                new ElevatorControl(this.arm, 0.000), null, () -> this.arm.canElevatorMove())),
            null, () -> ));

        addCommands(part1, new InstantCommand(() -> endCommand()));
    }

    public void endCommand() {
        arm.setArmGoal(arm.getAvgAngle());
    }



}
