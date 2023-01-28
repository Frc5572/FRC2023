package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Creates subsystem and functions for the elevator.
 */
public class Elevator extends SubsystemBase {

    private final CANSparkMax leftElevatorMotor =
        new CANSparkMax(Constants.Elevator.leftElevatorMotorID, MotorType.kBrushless);
    private final CANSparkMax rightElevatorMotor =
        new CANSparkMax(Constants.Elevator.rightElevatorMotorID, MotorType.kBrushless);
    private double elevatorElevatorRotation;

    private final DutyCycleEncoder elevatorEncoder = new DutyCycleEncoder(99);

    private final ElevatorFeedforward elevatorFF = new ElevatorFeedforward(
        Constants.Elevator.elevatorKSVolts, Constants.Elevator.elevatorKGVolts,
        Constants.Elevator.elevatorKVVoltsSecondsPerRotation);

    private final ProfiledPIDController elevatorPID = new ProfiledPIDController(
        Constants.Elevator.elevatorKP, Constants.Elevator.elevatorKI, Constants.Elevator.elevatorKD,
        new TrapezoidProfile.Constraints(Constants.Elevator.elevatorKMaxVelocityRadPerSecond,
            Constants.Elevator.elevatorKMaxAccelerationRadPerSecSquared));



    public Elevator() {

    }

    /**
     * Creates runMotor method to move the motors.
     */
    public void runMotor(double currentVal, double goal) {
        leftElevatorMotor.set(elevatorPID.calculate(currentVal * 360, goal));
        rightElevatorMotor.set(elevatorPID.calculate(currentVal * 360, goal));
    }

    /**
     * Creates getElevatorRotation method to return the current encoder rotation.
     */
    public double getElevatorRotation() {
        elevatorElevatorRotation =
            elevatorEncoder.getAbsolutePosition() - Constants.Elevator.encoderOffSet;
        return elevatorElevatorRotation;
    }
}
