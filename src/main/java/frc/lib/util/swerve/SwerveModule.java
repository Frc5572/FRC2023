package frc.lib.util.swerve;

import org.littletonrobotics.junction.Logger;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.lib.math.Conversions;
import frc.lib.util.ctre.CTREModuleState;
import frc.robot.Constants;

/**
 * Base Swerve Module Class. Creates an instance of the swerve module
 */
public class SwerveModule {
    public int moduleNumber;
    private double angleOffset;
    private double lastAngle;
    SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(Constants.Swerve.DRIVE_KS,
        Constants.Swerve.DRIVE_KV, Constants.Swerve.DRIVE_KA);

    private SwerveModuleIO io;
    private SwerveModuleInputsAutoLogged inputs = new SwerveModuleInputsAutoLogged();

    /**
     * Creates an instance of a Swerve Module
     *
     * @param moduleNumber Swerve Module ID. Must be unique
     * @param constants Constants specific to the swerve module
     */
    public SwerveModule(int moduleNumber, frc.lib.util.swerve.SwerveModuleConstants constants,
        SwerveModuleIO io) {
        this.io = io;
        io.updateInputs(inputs);

        this.moduleNumber = moduleNumber;
        angleOffset = constants.angleOffset;

        lastAngle = getState().angle.getDegrees();
    }

    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("SwerveModule" + moduleNumber, inputs);
    }

    /**
     * Sets the desired state for the swerve module for speed and angle
     *
     * @param desiredState The desired state (speed and angle)
     * @param isOpenLoop Whether to use open or closed loop formula
     */
    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
        desiredState = CTREModuleState.optimize(desiredState, getState().angle);
        // Custom optimize
        // command, since
        // default WPILib
        // optimize assumes
        // continuous
        // controller which
        // CTRE is
        // not

        if (isOpenLoop) {
            double percentOutput = desiredState.speedMetersPerSecond / Constants.Swerve.MAX_SPEED;
            io.setDriveMotor(ControlMode.PercentOutput, percentOutput);
        } else {
            double velocity = Conversions.mpsToFalcon(desiredState.speedMetersPerSecond,
                Constants.Swerve.WHEEL_CIRCUMFERENCE, Constants.Swerve.DRIVE_GEAR_RATIO);

            io.setDriveMotorWithFF(ControlMode.Velocity, velocity, DemandType.ArbitraryFeedForward,
                feedforward.calculate(desiredState.speedMetersPerSecond));
        }

        double angle =
            (Math.abs(desiredState.speedMetersPerSecond) <= (Constants.Swerve.MAX_SPEED * 0.01))
                ? lastAngle
                : desiredState.angle.getDegrees(); // Prevent rotating module if speed is
        // less then 1%. Prevents
        // Jittering.
        io.setAngleMotor(ControlMode.Position,
            Conversions.degreesToFalcon(angle, Constants.Swerve.ANGLE_GEAR_RATIO));
        lastAngle = angle;
    }

    /**
     *
     *
     * @param rotationSpeed Drive motor speed (-1 &lt;= value &lt;= 1)
     */
    public void setTurnAngle(double rotationSpeed) {
        double absolutePosition = Conversions.degreesToFalcon(
            getCanCoder().getDegrees() - angleOffset, Constants.Swerve.ANGLE_GEAR_RATIO);
        io.setAngleSelectedSensorPosition(absolutePosition);
        io.setDriveMotor(ControlMode.PercentOutput, rotationSpeed);
    }

    /**
     * Gets the Swerve module position
     *
     * @return Swerve module position
     */
    public SwerveModulePosition getPosition() {
        double position = Conversions.falconToMeters(inputs.driveMotorSelectedPosition,
            Constants.Swerve.DRIVE_GEAR_RATIO, Constants.Swerve.WHEEL_CIRCUMFERENCE);

        Rotation2d angle = Rotation2d.fromDegrees(Conversions
            .falconToDegrees(inputs.angleMotorSelectedPosition, Constants.Swerve.ANGLE_GEAR_RATIO));
        return new SwerveModulePosition(position, angle);
    }

    /**
     * Get the 2d rotation of the module
     *
     * @return 2d rotation of the module
     */
    public Rotation2d getCanCoder() {
        return Rotation2d.fromDegrees(inputs.angleEncoderValue);
    }

    /**
     * Gets the Swerve module state
     *
     * @return Swerve module state
     */
    public SwerveModuleState getState() {
        double velocity = Conversions.falconToMPS(inputs.driveMotorSelectedSensorVelocity,
            Constants.Swerve.WHEEL_CIRCUMFERENCE, Constants.Swerve.DRIVE_GEAR_RATIO);
        Rotation2d angle = Rotation2d.fromDegrees(Conversions
            .falconToDegrees(inputs.angleMotorSelectedPosition, Constants.Swerve.ANGLE_GEAR_RATIO));
        return new SwerveModuleState(velocity, angle);
    }

}
