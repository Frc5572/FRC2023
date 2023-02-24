package frc.robot.commands.arm;

import java.util.Map;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.elevator.ElevatorControl;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DropIntake;
import frc.robot.subsystems.WristIntake;

public class AutoPlace extends SequentialCommandGroup {

    Arm arm;
    DropIntake dropIntake;
    WristIntake wristIntake;
    Map<Integer, Double> grid;
    boolean safety;

    public AutoPlace(Arm arm, DropIntake dropIntake, WristIntake wristIntake,
        Map<Integer, Double> map) {
        this.arm = arm;
        this.dropIntake = dropIntake;
        this.wristIntake = wristIntake;
        this.grid = map;

        ParallelRaceGroup part = new ParallelRaceGroup(
            new ConditionalCommand(
                new SequentialCommandGroup(new ArmMoving(this.arm, 0.000),
                    new ConditionalCommand(new ElevatorControl(this.arm, 0.000), null,
                        () -> this.arm.canElevatorMove())),
                new SequentialCommandGroup(new ArmMoving(this.arm, 0.000),
                    new ConditionalCommand(new ElevatorControl(this.arm, 0.000), null,
                        () -> this.arm.canElevatorMove())),
                () -> safety),
            new RepeatCommand(new InstantCommand(() -> safety = elevatorCheck())));

        addCommands(part, new InstantCommand(() -> endCommand()));
    }

    public boolean elevatorCheck() {
        return arm.goingBelow60();
    }

    public void endCommand() {
        arm.setArmGoal(arm.getAvgAngle());
        arm.setElevatorGoal(arm.getElevatorPosition());
    }
}
