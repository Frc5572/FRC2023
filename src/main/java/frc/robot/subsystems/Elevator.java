package frc.robot.subsystems;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import frc.robot.Constants;

/**
 * Creates subsystem and functions for the elevator.
 */
public class Elevator extends ProfiledPIDSubsystem {
    private final CANSparkMax leftElevatorMotor =
        new CANSparkMax(Constants.Elevator.leftElevatorMotorID, MotorType.kBrushless);
    private final CANSparkMax rightElevatorMotor =
        new CANSparkMax(Constants.Elevator.rightElevatorMotorID, MotorType.kBrushless);
    private int elevatorEncoderTick;
    private final CANCoder elevatorCANCoder = new CANCoder(0);
    private final Encoder test = new Encoder(null, null);
    private final ElevatorFeedforward elevatorFF =
        new ElevatorFeedforward(Constants.ElevatorPID.kSVolts, Constants.ElevatorPID.kGVolts,
            Constants.ElevatorPID.kVVoltsSecondsPerRotation);

    /**
     * Create elevator class for PID
     */
    public Elevator() {
        super(new ProfiledPIDController(Constants.ElevatorPID.kP, Constants.ElevatorPID.kI,
            Constants.ElevatorPID.kD,
            new TrapezoidProfile.Constraints(Constants.ElevatorPID.kMaxVelocityRadPerSecond,
                Constants.ElevatorPID.kMaxAccelerationRadPerSecSquared)));
    }

    public void moveElevator(int currentEncoderTick, int stateEncoderTick) {
        while (currentEncoderTick != stateEncoderTick) {
            if (Math.abs(currentEncoderTick - stateEncoderTick) < 500) {
                // Moves the motor mor in the positive direction
            } else {
                // Moves the motor mor in the negative direction
            }
        }
    }

    @Override
    public double getMeasurement() {
        // Uses the .get() method to get the count of the encoder and stores it in
        // elevatorEncoderTick which then is returned
        elevatorEncoderTick = test.get();
        return elevatorEncoderTick;
    }

    @Override
    public void useOutput(double output, State setpoint) {
        // Look over setpoint.velocity
        leftElevatorMotor.setVoltage(output + elevatorFF.calculate(setpoint.velocity));
        rightElevatorMotor.setVoltage(output + elevatorFF.calculate(setpoint.velocity));
    }

}
