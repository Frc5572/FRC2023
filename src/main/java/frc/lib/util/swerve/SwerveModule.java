package frc.lib.util.swerve;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.lib.math.Conversions;
import frc.robot.Constants;

/**
 * Swerve Module
 */
public class SwerveModule {
    public int moduleNumber;
    private Rotation2d angleOffset;

    private TalonFX mAngleMotor;
    private TalonFX mDriveMotor;
    private CANcoder angleEncoder;
    private TalonFXConfiguration swerveAngleFXConfig = new TalonFXConfiguration();
    private TalonFXConfiguration swerveDriveFXConfig = new TalonFXConfiguration();
    private CANcoderConfiguration swerveCANcoderConfig = new CANcoderConfiguration();

    private final SimpleMotorFeedforward driveFeedForward = new SimpleMotorFeedforward(
        Constants.Swerve.DRIVE_KS, Constants.Swerve.DRIVE_KV, Constants.Swerve.DRIVE_KA);

    /* drive motor control requests */
    private final DutyCycleOut driveDutyCycle = new DutyCycleOut(0);
    private final VelocityVoltage driveVelocity = new VelocityVoltage(0);

    /* angle motor control requests */
    private final PositionVoltage anglePosition = new PositionVoltage(0);

    /**
     * Swerve Module
     *
     * @param moduleNumber Module Number
     * @param driveMotorID CAN ID of the Drive Motor
     * @param angleMotorID CAN ID of the Angle Motor
     * @param cancoderID CAN ID of the CANCoder
     * @param angleOffset Angle Offset of the CANCoder to align the wheels
     */
    public SwerveModule(int moduleNumber, int driveMotorID, int angleMotorID, int cancoderID,
        Rotation2d angleOffset) {
        this.moduleNumber = moduleNumber;
        this.angleOffset = angleOffset;

        /* Angle Encoder Config */
        swerveCANcoderConfig.MagnetSensor.SensorDirection = Constants.Swerve.CAN_CODER_INVERT;

        angleEncoder = new CANcoder(cancoderID);
        angleEncoder.getConfigurator().apply(swerveCANcoderConfig);

        /* Angle Motor Config */
        /* Motor Inverts and Neutral Mode */
        swerveAngleFXConfig.MotorOutput.Inverted = Constants.Swerve.ANGLE_MOTOT_INVERT;
        swerveAngleFXConfig.MotorOutput.NeutralMode = Constants.Swerve.ANGLE_NEUTRAL_MODE;

        /* Gear Ratio and Wrapping Config */
        swerveAngleFXConfig.Feedback.SensorToMechanismRatio = Constants.Swerve.ANGLE_GEAR_RATIO;
        swerveAngleFXConfig.ClosedLoopGeneral.ContinuousWrap = true;

        /* Current Limiting */
        swerveAngleFXConfig.CurrentLimits.SupplyCurrentLimitEnable =
            Constants.Swerve.ANGLE_ENABLE_CURRENT_LIMIT;
        swerveAngleFXConfig.CurrentLimits.SupplyCurrentLimit =
            Constants.Swerve.ANGLE_CONTINOUS_CURRENT_LIMIT;
        swerveAngleFXConfig.CurrentLimits.SupplyCurrentThreshold =
            Constants.Swerve.ANGLE_PEAK_CURRENT_LIMIT;
        swerveAngleFXConfig.CurrentLimits.SupplyTimeThreshold =
            Constants.Swerve.ANGLE_PEAK_CURRENT_DURATION;

        /* PID Config */
        swerveAngleFXConfig.Slot0.kP = Constants.Swerve.ANGLE_KP;
        swerveAngleFXConfig.Slot0.kI = Constants.Swerve.ANGLE_KI;
        swerveAngleFXConfig.Slot0.kD = Constants.Swerve.ANGLE_KD;
        mAngleMotor = new TalonFX(angleMotorID);
        mAngleMotor.getConfigurator().apply(swerveAngleFXConfig);
        resetToAbsolute();

        /* Drive Motor Config */
        /* Motor Inverts and Neutral Mode */
        swerveDriveFXConfig.MotorOutput.Inverted = Constants.Swerve.DRIVE_MOTOR_INVERT;
        swerveDriveFXConfig.MotorOutput.NeutralMode = Constants.Swerve.DRIVE_NEUTRAL_MODE;

        /* Gear Ratio Config */
        swerveDriveFXConfig.Feedback.SensorToMechanismRatio = Constants.Swerve.DRIVE_GEAR_RATIO;

        /* Current Limiting */
        swerveDriveFXConfig.CurrentLimits.SupplyCurrentLimitEnable =
            Constants.Swerve.DRIVE_ENABLE_CURRENT_LIMIT;
        swerveDriveFXConfig.CurrentLimits.SupplyCurrentLimit =
            Constants.Swerve.DRIVE_CONTINOUS_CURRENT_LIMIT;
        swerveDriveFXConfig.CurrentLimits.SupplyCurrentThreshold =
            Constants.Swerve.DRIVE_PEAK_CURRENT_LIMIT;
        swerveDriveFXConfig.CurrentLimits.SupplyTimeThreshold =
            Constants.Swerve.DRIVE_PEAK_CURRENT_DURATION;

        /* PID Config */
        swerveDriveFXConfig.Slot0.kP = Constants.Swerve.DRIVE_KP;
        swerveDriveFXConfig.Slot0.kI = Constants.Swerve.DRIVE_KI;
        swerveDriveFXConfig.Slot0.kD = Constants.Swerve.DRIVE_KD;

        /* Open and Closed Loop Ramping */
        swerveDriveFXConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod =
            Constants.Swerve.OPEN_LOOP_RAMP;
        swerveDriveFXConfig.OpenLoopRamps.VoltageOpenLoopRampPeriod =
            Constants.Swerve.OPEN_LOOP_RAMP;

        swerveDriveFXConfig.ClosedLoopRamps.DutyCycleClosedLoopRampPeriod =
            Constants.Swerve.CLOSED_LOOP_RAMP;
        swerveDriveFXConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod =
            Constants.Swerve.CLOSED_LOOP_RAMP;
        mDriveMotor = new TalonFX(driveMotorID);
        mDriveMotor.getConfigurator().apply(swerveDriveFXConfig);
        mDriveMotor.getConfigurator().setPosition(0.0);
    }

    /**
     * Set the desired state of the Swerve Module
     *
     * @param desiredState The desired {@link SwerveModuleState} for the module
     * @param isOpenLoop Whether the state should be open or closed loop controlled
     */
    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
        desiredState = SwerveModuleState.optimize(desiredState, getState().angle);
        mAngleMotor.setControl(anglePosition.withPosition(desiredState.angle.getRotations()));
        setSpeed(desiredState, isOpenLoop);
    }

    /**
     * Set the velocity or power of the drive motor
     *
     * @param desiredState The desired {@link SwerveModuleState} of the module
     * @param isOpenLoop Whether the state should be open or closed loop controlled
     */
    private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop) {
        if (isOpenLoop) {
            driveDutyCycle.Output = desiredState.speedMetersPerSecond / Constants.Swerve.MAX_SPEED;
            mDriveMotor.setControl(driveDutyCycle);
        } else {
            driveVelocity.Velocity = Conversions.MPSToRPS(desiredState.speedMetersPerSecond,
                Constants.Swerve.WHEEL_CIRCUMFERENCE);
            driveVelocity.FeedForward =
                driveFeedForward.calculate(desiredState.speedMetersPerSecond);
            mDriveMotor.setControl(driveVelocity);
        }
    }

    /**
     * Get the rotation of the CANCoder
     *
     * @return The rotation of the CANCoder in {@link Rotation2d}
     */
    public Rotation2d getCANcoder() {
        return Rotation2d.fromRotations(angleEncoder.getAbsolutePosition().getValue());
    }

    /**
     * Reset the Swerve Module angle to face forward
     */
    public void resetToAbsolute() {
        double absolutePosition = getCANcoder().getRotations() - angleOffset.getRotations();
        mAngleMotor.setPosition(absolutePosition);
    }

    /**
     * Get the current Swerve Module State
     *
     * @return The current {@link SwerveModuleState}
     */
    public SwerveModuleState getState() {
        return new SwerveModuleState(
            Conversions.RPSToMPS(mDriveMotor.getVelocity().getValue(),
                Constants.Swerve.WHEEL_CIRCUMFERENCE),
            Rotation2d.fromRotations(mAngleMotor.getPosition().getValue()));
    }

    /**
     * Get the current Swerve Module Position
     *
     * @return The current {@link SwerveModulePosition}
     */
    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(
            Conversions.rotationsToMeters(mDriveMotor.getPosition().getValue(),
                Constants.Swerve.WHEEL_CIRCUMFERENCE),
            Rotation2d.fromRotations(mAngleMotor.getPosition().getValue()));
    }
}
