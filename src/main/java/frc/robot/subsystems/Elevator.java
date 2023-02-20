package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Creates the subsystem for the elevator.
 */
public class Elevator extends SubsystemBase {
    private final CANSparkMax elevatorMotor =
        new CANSparkMax(Constants.Elevator.ELEVATOR_MOTOR_ID, MotorType.kBrushless);
    private final RelativeEncoder elevatorEncoder = elevatorMotor.getEncoder();
    private final PIDController pid_controller =
        new PIDController(Constants.Elevator.PID.ELEVATOR_KP, Constants.Elevator.PID.ELEVATOR_KI,
            Constants.Elevator.PID.ELEVATOR_KD);

    private final double maxEncoder = 2.70; // 2.7142841815948486
    private double goalPosition;
    private boolean enablePID = false;


    public Elevator() {
        elevatorMotor.restoreFactoryDefaults();
        elevatorMotor.setInverted(false);
        elevatorMotor.setIdleMode(IdleMode.kBrake);
        elevatorMotor.burnFlash();
    }

    @Override
    public void periodic() {
        if (enablePID) {
            elevatorToPosition();
        }
    }

    /**
     * Set target angle for Arm
     *
     * @param goal Target Angel in Degrees
     */
    public void setGoal(double goal) {
        this.goalPosition = goal;
        pid_controller.setSetpoint(goal);
    }

    /**
     * Get the target angle for the Arm
     *
     * @return The target angle in degrees
     */
    public double getGoal() {
        return this.goalPosition;
    }

    /**
     * Gets the encoder measurement of the elevator in rotation degrees.
     */
    public double getDegreeMeasurement() {
        return elevatorEncoder.getPosition();
    }

    /**
     * Sets the elevator to go until a certain degree has been reached.
     */
    public void elevatorToPosition() {
        elevatorMotor.setVoltage(pid_controller.calculate(getDegreeMeasurement()));
    }

    /**
     * Enable PID control
     */
    public void enablePID() {
        this.enablePID = true;
    }

    /**
     * Disable PID control
     *
     */
    public void disablePID() {
        this.enablePID = false;
    }


    /**
     * Check if aligned with a requested goal.
     *
     * @param goal The requesed goal in degrees.
     * @return True if properly aligned, false if not.
     */
    public boolean checkIfAligned(double goal) {
        return Math.abs(getDegreeMeasurement() - goal) < 2;
    }

}
