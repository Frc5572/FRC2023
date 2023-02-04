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

    private static final CANSparkMax elevatorMotor =
        new CANSparkMax(Constants.Elevator.elevatorMotorID, MotorType.kBrushless);
    private static final DutyCycleEncoder elevatorEncoder = new DutyCycleEncoder(99);
    private static final ProfiledPIDController elevatorPID =
        new ProfiledPIDController(Constants.Elevator.PID.elevatorKP,
            Constants.Elevator.PID.elevatorKI, Constants.Elevator.PID.elevatorKD,
            new TrapezoidProfile.Constraints(
                Constants.Elevator.PID.elevatorKMaxVelocityRadPerSecond,
                Constants.Elevator.PID.elevatorKMaxAccelerationRadPerSecSquared));

    public Elevator() {
        elevatorPID.setTolerance(3);
    }

    /**
     * Creates runMotor method to move the motors.
     */
    public void runMotor(double currentVal, double goal) {
        elevatorMotor.set(elevatorPID.calculate(currentVal, goal));
    }

    /**
     * Sets both of the elevator motors to a specific speed.
     *
     * @param speed The speed of the motors.
     */
    public void set(double speed) {
        elevatorMotor.set(speed);
    }

    /**
     * @return the absolute value of the elevator encoder.
     */
    public double getAbsolutePosition() {
        return (elevatorEncoder.getAbsolutePosition() - Constants.Elevator.encoderOffSet) * 360;
    }

    /**
     * @return whether the Elevator PID is at the goal or not
     */
    public boolean atGoal() {
        return elevatorPID.atGoal();
    }
}
