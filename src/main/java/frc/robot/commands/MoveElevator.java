package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;

/**
 * Creates a command for movine the elevator.
 */
public class MoveElevator extends CommandBase {

    private final Elevator elevator;
    private double encoderGoal;

    /**
     * Creates a command for movine the elevator.
     *
     * @param rotation Current rotation value to determine where the motor is.
     */
    public MoveElevator(double rotation, Elevator elevator) {
        this.encoderGoal = rotation;
        this.elevator = elevator;
        addRequirements(elevator);
    }

    @Override
    public void execute() {
        this.elevator.runMotor(this.elevator.getElevatorRotation(), encoderGoal);

    }

    @Override
    public void end(boolean interrupted) {
        this.elevator.set(0);
    }

    @Override
    public boolean isFinished() {
        return this.elevator.atGoal();
    }

}
