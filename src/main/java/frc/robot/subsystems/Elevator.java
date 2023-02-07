package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Elevator extends SubsystemBase {
    private final CANSparkMax eMotor =
        new CANSparkMax(Constants.Elevator.ELEVATOR_MOTOR_ID, MotorType.kBrushless);
    private final DutyCycleEncoder dEncoder =
        new DutyCycleEncoder(Constants.Elevator.ELEVATOR_ENCODER_ID);
    private final ProfiledPIDController pid_controller =
        new ProfiledPIDController(Constants.Elevator.PID.ELEVATOR_KP,
            Constants.Elevator.PID.ELEVATOR_KI, Constants.Elevator.PID.ELEVATOR_KD,
            new TrapezoidProfile.Constraints(
                Constants.Elevator.PID.ELEVATOR_K_MAX_VELOCITY_RAD_PER_SECOND,
                Constants.Elevator.PID.ELEVATOR_K_MAX_ACCELERATION_RAD_PER_SEC_SQUARED));
    private final ElevatorFeedforward feedforward = new ElevatorFeedforward(
        Constants.Elevator.PID.ELEVATOR_KS_VOLTS, Constants.Elevator.PID.ELEVATOR_KG_VOLTS,
        Constants.Elevator.PID.ELEVATOR_KV_VOLT_SECONDS_PER_ROTATION);

    private final double encOffset = Constants.Elevator.ENCODER_OFFSET;

    public Elevator() {
        dEncoder.setDistancePerRotation(0.0000); // i dont think we need this but might as well have
    }

    /**
     * Gets the encoder measurement of the elevator in rotation degrees.
     */
    public double getDegreeMeasurement() {
        double armAngle = (dEncoder.getAbsolutePosition() - encOffset) * 360;
        return armAngle;
    }

    /**
     * Sets the elevator to go until a certain degree has been reached.
     */
    public void elevatorToDegree(double degrees) {
        if (!(Math.abs(getDegreeMeasurement() - degrees) < 3)) {
            eMotor.setVoltage(pid_controller.calculate(getDegreeMeasurement(), degrees)
                + feedforward.calculate(getDegreeMeasurement(), 0));
        }
    }

    /**
     * Stops the elevator motor.
     */
    public void stop() {
        eMotor.set(Constants.Elevator.ELEVATOR_STOP);
    }

}
