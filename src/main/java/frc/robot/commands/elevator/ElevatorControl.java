package frc.robot.commands.elevator;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;

/**
 * Creates the command that allows the elevator to move.
 */
public class ElevatorControl extends CommandBase {
    private Elevator elevator;
    private double goal;

    /**
     * Requirements for the command.
     *
     * @param elevator Elevator subsystem.
     * @param goal Goal at which the user is requesting the elevator to go to.
     */
    public ElevatorControl(Elevator elevator, double goal) {
        this.elevator = elevator;
        this.goal = goal;
        addRequirements(elevator);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        elevator.enablePID();
        elevator.setGoal(goal);
    }

    @Override
    public boolean isFinished() {
        return elevator.checkIfAligned(goal);
    }
}
