package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;

/**
 * Creates the command that allows the elevator to move.
 */
public class ElevatorControl extends CommandBase {
    private Elevator elevator;
    private double angle;

    /**
     * Requirements for the command.
     *
     * @param elevator Elevator subsystem.
     * @param goal Goal at which the user is requesting the elevator to go to.
     */
    public ElevatorControl(Elevator elevator, double goal) {
        this.elevator = elevator;
        this.angle = goal;
        addRequirements(elevator);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        elevator.elevatorToDegree(angle);
    }

    @Override
    public void end(boolean interrupt) {
        elevator.stop();
    }

    @Override
    public boolean isFinished() {
        return Math.abs(elevator.getDegreeMeasurement() - angle) < 3;
    }
}
