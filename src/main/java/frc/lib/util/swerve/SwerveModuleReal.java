package frc.lib.util.swerve;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Constants;

/**
 * Swerve Module with real motors and sensors
 */
public class SwerveModuleReal implements SwerveModuleIO {
    private TalonFX mAngleMotor;
    private TalonFX mDriveMotor;
    private CANcoder angleEncoder;
    private TalonFXConfiguration swerveAngleFXConfig = new TalonFXConfiguration();
    private TalonFXConfiguration swerveDriveFXConfig = new TalonFXConfiguration();
    private CANcoderConfiguration swerveCANcoderConfig = new CANcoderConfiguration();

    /**
     * A real Swerve Module with motors and sensors
     *
     * @param moduleNumber The Module Number
     * @param driveMotorID The Drive Motor CAN ID
     * @param angleMotorID The Angle Motor CAN ID
     * @param cancoderID The CANCode CAN ID
     * @param angleOffset The Angle Offset in {@link Rotation2d}
     */
    public SwerveModuleReal(int moduleNumber, int driveMotorID, int angleMotorID, int cancoderID,
        Rotation2d angleOffset) {

        angleEncoder = new CANcoder(cancoderID, "canivore");
        mDriveMotor = new TalonFX(driveMotorID, "canivore");
        mAngleMotor = new TalonFX(angleMotorID, "canivore");

        configAngleEncoder();
        configAngleMotor();
        configDriveMotor();
    }

    private void configAngleMotor() {
        /* Angle Motor Config */
        /* Motor Inverts and Neutral Mode */
        swerveAngleFXConfig.MotorOutput.NeutralMode = Constants.Swerve.angleNeutralMode;

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

        mAngleMotor.getConfigurator().apply(swerveAngleFXConfig);
        mAngleMotor.setInverted(Constants.Swerve.ANGLE_MOTOR_INVERT);
    }

    private void configDriveMotor() {
        /* Drive Motor Config */
        /* Motor Inverts and Neutral Mode */
        swerveDriveFXConfig.MotorOutput.NeutralMode = Constants.Swerve.driveNeutralMode;

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

        mDriveMotor.getConfigurator().apply(swerveDriveFXConfig);
        mDriveMotor.getConfigurator().setPosition(0.0);
        mDriveMotor.setInverted(Constants.Swerve.DRIVE_MOTOR_INVERT);
    }

    private void configAngleEncoder() {
        /* Angle Encoder Config */
        swerveCANcoderConfig.MagnetSensor.SensorDirection = Constants.Swerve.CAN_CODER_INVERT;
        angleEncoder.getConfigurator().apply(swerveCANcoderConfig);
    }

    @Override
    public void setAngleMotor(ControlRequest request) {
        mAngleMotor.setControl(request);
    }

    @Override
    public void setDriveMotor(ControlRequest request) {
        mDriveMotor.setControl(request);
    }

    @Override
    public void updateInputs(SwerveModuleInputs inputs) {
        inputs.driveMotorSelectedPosition = mDriveMotor.getPosition().getValueAsDouble();
        inputs.driveMotorSelectedSensorVelocity = mDriveMotor.getVelocity().getValueAsDouble();
        inputs.angleMotorSelectedPosition = mAngleMotor.getPosition().getValueAsDouble();
        inputs.absolutePositionAngleEncoder = angleEncoder.getAbsolutePosition().getValueAsDouble();
    }

    @Override
    public void setPositionAngleMotor(double absolutePosition) {
        mAngleMotor.setPosition(absolutePosition);
    }

}
