package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Creates the subsystem for the dropdown intake.
 */
public class DropIntake extends SubsystemBase {
    private final CANSparkMax leftDropMotor =
        new CANSparkMax(Constants.DropDownIntake.LEFT_DROP_MOTOR_ID, MotorType.kBrushless);
    private final CANSparkMax rightDropMotor =
        new CANSparkMax(Constants.DropDownIntake.RIGHT_DROP_MOTOR_ID, MotorType.kBrushless);
    private final MotorControllerGroup dropdownMotors =
        new MotorControllerGroup(leftDropMotor, rightDropMotor);
    private final CANSparkMax intakeMotor =
        new CANSparkMax(Constants.DropDownIntake.INTAKE_MOTOR_ID, MotorType.kBrushless);
    private final AbsoluteEncoder dropEncoder = leftDropMotor.getAbsoluteEncoder(Type.kDutyCycle);
    private final PIDController pidController = new PIDController(Constants.DropDownIntake.PID.KP,
        Constants.DropDownIntake.PID.KI, Constants.DropDownIntake.PID.KD);
    private final ArmFeedforward feedforward = new ArmFeedforward(Constants.DropDownIntake.PID.KS,
        Constants.DropDownIntake.PID.KG, Constants.DropDownIntake.PID.KV);

    private final double dropEncoderOffset = 216.6783500;
    public final double position1 = 0;
    public final double position2 = 60;
    public final double position3 = 104;

    /**
     * Drop Down Intake Subsystem
     */
    public DropIntake() {
        leftDropMotor.restoreFactoryDefaults();
        rightDropMotor.restoreFactoryDefaults();
        leftDropMotor.setIdleMode(IdleMode.kBrake);
        rightDropMotor.setIdleMode(IdleMode.kBrake);
        leftDropMotor.setInverted(false);
        rightDropMotor.setInverted(true);
        dropEncoder.setZeroOffset(dropEncoderOffset);
        dropEncoder.setPositionConversionFactor(360);
        dropEncoder.setVelocityConversionFactor(360);
        leftDropMotor.burnFlash();
        rightDropMotor.burnFlash();
    }

    /**
     * Deploy the dropdown intake to the specified height for a cone. Stops when within threshold.
     */
    public void intakeConeDeploy() {
        dropdownMotors.set(Constants.DropDownIntake.DROP_VOLTS);
    }

    /**
     * Deploy the dropdown intake to the specified height for a cube. Stops when within threshold.
     */
    public void intakeCubeDeploy() {
        dropdownMotors.setVoltage(Constants.DropDownIntake.DROP_VOLTS);
    }

    /**
     * Retracts the dropdown intake to the default height. Stops when within threshold.
     */
    public void intakeRetract() {
        dropdownMotors.set(Constants.DropDownIntake.RETRACT_VOLTS);
    }

    public void stopDrop() {
        dropdownMotors.setVoltage(Constants.DropDownIntake.STOP_VOLTS);
    }

    // Runs the intake at a specified speed.
    public void intake() {
        intakeMotor.set(Constants.DropDownIntake.INTAKE_SPEED);
    }

    // Stops the intake from running.
    public void stop() {
        intakeMotor.setVoltage(0);
    }

    /**
     * Get the current angle that is stated by the encoder.
     *
     * @return Current angle reported by encoder (0 - 360)
     */
    public double getAngleMeasurement() {
        return dropEncoder.getPosition();
    }

    /**
     * Set the dropdown motors to go to a certain angle.
     *
     * @param angle Requested angle.
     */
    public void ddToAngle(double angle) {
        dropdownMotors.set(pidController.calculate(getAngleMeasurement(), angle)
            + feedforward.calculate((getAngleMeasurement() * (Math.PI / 180.0)), 0.0));
    }

    /**
     * Check if aligned with a requested goal.
     *
     * @param goal The requesed goal in degrees.
     * @return True if properly aligned, false if not.
     */
    public boolean checkIfAligned(double goal) {
        return Math.abs(getAngleMeasurement() - goal) < 5;
    }
}

