package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Creates the subsystem for the dropdown intake.
 */
public class DropIntake extends SubsystemBase {
    private final CANSparkMax leftDropMotor = new CANSparkMax(
        Constants.IntakeConstants.DropConstants.LEFT_DROP_MOTOR_ID, MotorType.kBrushless);
    private final CANSparkMax rightDropMotor = new CANSparkMax(
        Constants.IntakeConstants.DropConstants.RIGHT_DROP_MOTOR_ID, MotorType.kBrushless);
    private final MotorControllerGroup dropdownMotors =
        new MotorControllerGroup(leftDropMotor, rightDropMotor);
    private final CANSparkMax intakeMotor = new CANSparkMax(
        Constants.IntakeConstants.DropConstants.INTAKE_MOTOR_ID, MotorType.kBrushless);
    private final DutyCycleEncoder dropEncoder =
        new DutyCycleEncoder(Constants.IntakeConstants.DropConstants.DROP_ENCODER_ID);
    private final PIDController pid_controller =
        new PIDController(Constants.IntakeConstants.DropConstants.PID.KP,
            Constants.IntakeConstants.DropConstants.PID.KI,
            Constants.IntakeConstants.DropConstants.PID.KD);
    private final ArmFeedforward feedforward =
        new ArmFeedforward(Constants.IntakeConstants.DropConstants.PID.KS,
            Constants.IntakeConstants.DropConstants.PID.KG,
            Constants.IntakeConstants.DropConstants.PID.KV);

    private final double dropEncoderOffset = 0.000;
    private final double defaultGoal = 0.0893 * 360;
    private final double coneDeployGoal = 0.000 * 360;
    private final double cubeDeployGoal = 0.0601 * 360;

    public DropIntake() {
        leftDropMotor.setIdleMode(IdleMode.kBrake);
        rightDropMotor.setIdleMode(IdleMode.kBrake);
    }

    /**
     * Deploy the dropdown intake to the specified height for a cone. Stops when within threshold.
     */
    public void intakeConeDeploy() {
        leftDropMotor.setVoltage(Constants.IntakeConstants.DropConstants.DROP_VOLTS);
        rightDropMotor.setVoltage(Constants.IntakeConstants.DropConstants.DROP_VOLTS);
    }

    /**
     * Deploy the dropdown intake to the specified height for a cube. Stops when within threshold.
     */
    public void intakeCubeDeploy() {
        leftDropMotor.setVoltage(Constants.IntakeConstants.DropConstants.DROP_VOLTS);
        rightDropMotor.setVoltage(Constants.IntakeConstants.DropConstants.DROP_VOLTS);
    }

    /**
     * Retracts the dropdown intake to the default height. Stops when within threshold.
     */
    public void intakeRetract() {
        leftDropMotor.setVoltage(Constants.IntakeConstants.DropConstants.RETRACT_VOLTS);
        rightDropMotor.setVoltage(Constants.IntakeConstants.DropConstants.RETRACT_VOLTS);
    }

    public void stopDrop() {
        leftDropMotor.setVoltage(Constants.IntakeConstants.DropConstants.STOP_VOLTS);
        rightDropMotor.setVoltage(Constants.IntakeConstants.DropConstants.STOP_VOLTS);
    }

    // Runs the intake at a specified speed.
    public void intake() {
        intakeMotor.set(Constants.IntakeConstants.DropConstants.INTAKE_SPEED);
    }

    // Stops the intake from running.
    public void stop() {
        intakeMotor.setVoltage(Constants.IntakeConstants.DropConstants.STOP_VOLTS);
    }

    /**
     * Get the current angle that is stated by the encoder.
     *
     * @return Current angle reported by encoder (0 - 360)
     */
    public double getAngleMeasurement() {
        double dropAngle = (dropEncoder.getAbsolutePosition() - dropEncoderOffset) * 360;
        return dropAngle;
    }

    /**
     * Set the dropdown motors to go to a certain angle.
     *
     * @param angle Requested angle.
     */
    public void ddToAngle(double angle) {
        dropdownMotors.set(pid_controller.calculate(getAngleMeasurement(), angle)
            + feedforward.calculate((getAngleMeasurement() * (Math.PI / 180.0)), 0.0));
    }

    /**
     * Check if aligned with a requested goal.
     *
     * @param goal The requesed goal.
     * @return True if properly aligned, false if not.
     */
    public boolean checkIfAligned(double goal) {
        return Math.abs(getAngleMeasurement() - goal) < 5;
    }
}

