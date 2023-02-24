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

/**
 * Create an automated command that will automatically align the arm and elevator to place down a
 * cube / cone.
 */
public class AutoPlace extends SequentialCommandGroup {

    Arm arm;
    DropIntake dropIntake;
    WristIntake wristIntake;
    Map<Integer, Double> grid;
    boolean safety;

    /**
     * Requirements for the command.
     *
     * @param arm Arm subsystem
     * @param dropIntake Dropdown intake subsystem
     * @param wristIntake Wrist intake subsystem
     * @param map Grid of coordinates and values of where to align subsystems to.
     */
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

    /**
     * Check if the elevator is allowed to move based on the given conditions.
     *
     * @return true if we are going less than 60 degrees, false if we aren't
     */
    public boolean elevatorCheck() {
        return arm.goingBelow60();
    }

    /**
     * Commands to run when we reach the end of this command.
     */
    public void endCommand() {
        arm.setArmGoal(arm.getAvgAngle());
        arm.setElevatorGoal(arm.getElevatorPosition());
    }
}
