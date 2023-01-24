package frc.robot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.lib.math.CTREmodules;
import frc.lib.math.Conversions;
import frc.lib.math.SwerveModuleConstants;
import frc.robot.Constants;


/**
 * Base Swerve Module Class. Creates an instance of the swerve module
 */

public class SwerveModule {
    public TalonFX angleMotor;
    public TalonFX driveMotor;
    public CANCoder angleCANCoder;
    public int moduleNumber;
    public double angleOffset;
    public double lastAngle;
    public SwerveModuleConstants constants;
    SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(Constants.Swerve.driveKS,
        Constants.Swerve.driveKV, Constants.Swerve.driveKA);

    /**
     * Creates an instance of a Swerve Module
     *
     * @param moduleNumber Swerve Module ID. Must be unique
     * @param constants Constants specific to the swerve module
     */
    public SwerveModule(int moduleNumber, SwerveModuleConstants constants) {
        this.moduleNumber = moduleNumber;
        this.constants = constants;
        angleMotor = new TalonFX(constants.angleMotorID);
        driveMotor = new TalonFX(constants.driveMotorID);
        angleCANCoder = new CANCoder(constants.canCoderID);
    }

    /**
     * Sets the desired state for the swerve module for speed and angle
     *
     * @param desiredState The desired state (speed and angle)
     * @param openLoop Whether to use open or closed loop formula
     */
    public void setDesiredState(SwerveModuleState desiredState, Boolean openLoop) {
        desiredState = CTREmodules.optimize(desiredState, getState().angle);

        if (openLoop) {
            double percentOutput = desiredState.speedMetersPerSecond / Constants.maxSpeed;
            driveMotor.set(ControlMode.PercentOutput, percentOutput);
        } else {
            double velocity = Conversions.mpsToFalcon(desiredState.speedMetersPerSecond,
                Constants.Swerve.wheelCircumference, Constants.Swerve.driveGearRatio);
            driveMotor.set(ControlMode.Velocity, velocity, DemandType.ArbitraryFeedForward,
                feedforward.calculate(desiredState.speedMetersPerSecond));
        }
    }

    /**
     * Gets the Swerve module position
     *
     * @return Swerve module position
     */
    public SwerveModulePosition getPosition() {
        double position = Conversions.falconToMeters(driveMotor.getSelectedSensorPosition(),
            Constants.Swerve.driveGearRatio, Constants.Swerve.wheelCircumference);

        Rotation2d angle = Rotation2d.fromDegrees(Conversions.falconToDegrees(
            angleMotor.getSelectedSensorPosition(), Constants.Swerve.angleGearRatio));
        return new SwerveModulePosition(position, angle);
    }

    /**
     * Gets the Swerve module state
     *
     * @return Swerve module state
     */
    public SwerveModuleState getState() {
        double velocity = Conversions.falconToMPS(driveMotor.getSelectedSensorVelocity(),
            Constants.Swerve.wheelCircumference, Constants.Swerve.driveGearRatio);
        Rotation2d angle = Rotation2d.fromDegrees(Conversions.falconToDegrees(
            angleMotor.getSelectedSensorPosition(), Constants.Swerve.angleGearRatio));
        return new SwerveModuleState(velocity, angle);
    }

}
