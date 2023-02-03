package frc.robot.commands;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Elevator;

/**
 * Creates a command for movine the elevator.
 */
public class MoveElevator extends CommandBase {

    private CANSparkMax motor1 = Elevator.leftElevatorMotor;
    private CANSparkMax motor2 = Elevator.rightElevatorMotor;
    private DutyCycleEncoder dutyCycleEncoder = Elevator.elevatorEncoder;
    private double encoderGoal;

    /**
     * Creates a command for movine the elevator.
     *
     * @param rotation Current rotation value to determine where the motor is.
     */
    public MoveElevator(double rotation) {
        this.encoderGoal = rotation;
    }

    @Override
    public void execute() {
        Elevator.runMotor(encVal(), encoderGoal);

    }

    @Override
    public void end(boolean interrupted) {
        motor1.set(0);
        motor2.set(0);
    }

    @Override
    public boolean isFinished() {
        return (this.encoderGoal == 0);
    }

    public double encVal() {
        return (dutyCycleEncoder.getAbsolutePosition() - Constants.Elevator.encoderOffSet);
    }
}
