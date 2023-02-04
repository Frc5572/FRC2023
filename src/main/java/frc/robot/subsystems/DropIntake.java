package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Creates the subsystem for the dropdown intake.
 */
public class DropIntake extends SubsystemBase {
    private final CANSparkMax dropMotor = new CANSparkMax(
        Constants.IntakeConstants.DropConstants.DROP_MOTOR_ID, MotorType.kBrushless);
    private final CANSparkMax intakeMotor = new CANSparkMax(
        Constants.IntakeConstants.DropConstants.INTAKE_MOTOR_ID, MotorType.kBrushless);
    private final DutyCycleEncoder dropEncoder =
        new DutyCycleEncoder(Constants.IntakeConstants.DropConstants.DROP_ENCODER_ID);
    private final double dropEncoderOffset = 0.000;
    private final double defaultGoal = 0.000;
    private final double coneDeployGoal = 0.000;
    private final double cubeDeployGoal = 0.000;

    public DropIntake() {}

    // Deploy the dropdown intake to the specified height for a cone. Stops when within threshold.
    public void intakeConeDeploy() {
        dropMotor.set(Constants.IntakeConstants.DropConstants.DROP_SPEED);
        if (Math.abs(getAngleMeasurement() - coneDeployGoal) < 3) {
            dropMotor.set(Constants.IntakeConstants.DropConstants.STOP_SPEED);
        }
    }

    // Deploy the dropdown intake to the specified height for a cube. Stops when within threshold.
    public void intakeCubeDeploy() {
        dropMotor.set(Constants.IntakeConstants.DropConstants.DROP_SPEED);
        if (Math.abs(getAngleMeasurement() - cubeDeployGoal) < 3) {
            dropMotor.set(Constants.IntakeConstants.DropConstants.STOP_SPEED);
        }
    }

    // Retracts the dropdown intake to the default height. Stops when within threshold.
    public void intakeRetract() {
        dropMotor.set(Constants.IntakeConstants.DropConstants.RETRACT_SPEED);
        if (Math.abs(getAngleMeasurement() - defaultGoal) < 3) {
            dropMotor.set(Constants.IntakeConstants.DropConstants.STOP_SPEED);
        }
    }

    // Runs the intake at a specified speed.
    public void intake() {
        intakeMotor.set(Constants.IntakeConstants.DropConstants.INTAKE_SPEED);
    }

    // Stops the intake from running.
    public void stop() {
        intakeMotor.set(Constants.IntakeConstants.DropConstants.STOP_SPEED);
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
}

