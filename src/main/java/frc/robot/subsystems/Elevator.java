package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Encoder;
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
    private int elevatorEncoderTick;

    private final Encoder elevatorEncoder = new Encoder(null, null);

    private final ElevatorFeedforward elevatorFF = new ElevatorFeedforward(
        Constants.Elevator.elevatorKSVolts, Constants.Elevator.elevatorKGVolts,
        Constants.Elevator.elevatorKVVoltsSecondsPerRotation);

    private final ProfiledPIDController elevatorPID = new ProfiledPIDController(
        Constants.Elevator.elevatorKP, Constants.Elevator.elevatorKI, Constants.Elevator.elevatorKD,
        new TrapezoidProfile.Constraints(Constants.Elevator.elevatorKMaxVelocityRadPerSecond,
            Constants.Elevator.elevatorKMaxAccelerationRadPerSecSquared));


    /**
     * Creates moveElevator method to move the motors.
     */
    public void moveElevator(int currentEncoderTick, int stateEncoderTick) {
        if (Math.abs(currentEncoderTick - stateEncoderTick) < 500) {
            // Moves the motor more in the positive direction
        } else {
            // Moves the motor more in the negative direction
        }
    }

    /**
     * Creates getElevatorTick method to return the current encoder tick.
     */
    public int getElevatorTick() {
        // Uses the .get() method to get the count of the encoder and stores it in
        // elevatorEncoderTick which then is returned
        elevatorEncoderTick = elevatorEncoder.get();
        return elevatorEncoderTick;
    }
}
