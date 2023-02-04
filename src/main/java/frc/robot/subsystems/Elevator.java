package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Creates subsystem and functions for the elevator.
 */
public class Elevator extends SubsystemBase {

    private static final CANSparkMax leftElevatorMotor =
        new CANSparkMax(Constants.Elevator.leftElevatorMotorID, MotorType.kBrushless);
    private static final CANSparkMax rightElevatorMotor =
        new CANSparkMax(Constants.Elevator.rightElevatorMotorID, MotorType.kBrushless);
    private static final DutyCycleEncoder elevatorEncoder = new DutyCycleEncoder(99);
    private static final ProfiledPIDController elevatorPID = new ProfiledPIDController(
        Constants.Elevator.elevatorKP, Constants.Elevator.elevatorKI, Constants.Elevator.elevatorKD,
        new TrapezoidProfile.Constraints(Constants.Elevator.elevatorKMaxVelocityRadPerSecond,
            Constants.Elevator.elevatorKMaxAccelerationRadPerSecSquared));

    public Elevator() {

    }

    /**
     * Creates runMotor method to move the motors.
     */
    public void runMotor(double currentVal, double goal) {
        leftElevatorMotor.set(elevatorPID.calculate(currentVal, goal));
        rightElevatorMotor.set(elevatorPID.calculate(currentVal, goal));
    }

    /**
     * Sets both of the elevator motors to a specific speed.
     * 
     * 
     * @param speed The speed of the motors.
     */
    public void set(double speed) {
        leftElevatorMotor.set(speed);
        rightElevatorMotor.set(speed);
    }

    /**
     * @return return the current encoder rotation in contrast with the off set.
     */
    public double getElevatorRotation() {

        return elevatorEncoder.getAbsolutePosition() - Constants.Elevator.encoderOffSet;
    }

    /**
     * @return the absolute balue of the elevator encoder.
     */
    public double getAbsolutePosition() {

        return elevatorEncoder.getAbsolutePosition();
    }

    /**
     * @return whether the Elevator PID is at the goal or not
     */
    public boolean atGoal() {

        return elevatorPID.atGoal();
    }
}
