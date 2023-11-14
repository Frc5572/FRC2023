package frc.lib.util.swerve;

import org.littletonrobotics.junction.AutoLog;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;

public interface SwerveModuleIO {

    @AutoLog
    public static class SwerveModuleInputs {
        public double angleEncoderValue;
        public double driveMotorSelectedPosition;
        public double driveMotorSelectedSensorVelocity;
        public double angleMotorSelectedPosition;
    }

    public default void updateInputs(SwerveModuleInputs inputs) {}

    public default void setDriveMotor(ControlMode mode, double outputValue) {

    }

    public default void setDriveMotorWithFF(ControlMode mode, double demand0,
        DemandType demand1Type, double demand1) {}

    public default void setAngleMotor(ControlMode mode, double outputValue) {}

    public default void setAngleSelectedSensorPosition(double angle) {}

}
