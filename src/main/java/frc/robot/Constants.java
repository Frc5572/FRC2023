package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;
import frc.lib.math.SwerveModuleConstants;

/**
 * Constants file.
 */

public final class Constants {
    public static final double stickDeadband = 0.1;
    public static final int driverID = 0;
    public static final int operatorID = 1;

    /**
     * Motor CAN id's.
     */
    public static final class Motors {
    }

    /**
     * Pneumatics CAN id constants.
     */
    public static final class Pneumatics {
    }

    /**
     * Vision constants for limelight calculations.
     */
    public static final class VisionConstants {
        public static final double deadPocket = 0.05;
        public static final double limelightHeight = 0;
        public static final double targetHeight = 0;
        public static final double limelightAngle = 0;
    }
    public static class ModuleZero {
        public static final int angleMotorID = 50;
        public static final int driveMotorID = 8;
        public static final int CANCoderID = 3;
        public static final double angleOffsetID = 138.428;
        public static final SwerveModuleConstants Constants =
            new SwerveModuleConstants(angleMotorID, driveMotorID, CANCoderID, driverID);
    }
    public static class ModuleOne {
        public static final int angleMotorID = 51;
        public static final int driveMotorID = 52;
        public static final int CANCoderID = 2;
        public static final double angleOffsetID = 87.891;
        public static final SwerveModuleConstants Constants =
            new SwerveModuleConstants(angleMotorID, driveMotorID, CANCoderID, driverID);
    }
    public static class ModuleTwo {
        public static final int angleMotorID = 4;
        public static final int driveMotorID = 2;
        public static final int CANCoderID = 1;
        public static final double angleOffsetID = 259.541;
        public static final SwerveModuleConstants Constants =
            new SwerveModuleConstants(angleMotorID, driveMotorID, CANCoderID, driverID);
    }
    public static class ModuleThree {
        public static final int angleMotorID = 3;
        public static final int driveMotorID = 53;
        public static final int CANCoderID = 4;
        public static final double angleOffsetID = 32.695;
        public static final SwerveModuleConstants Constants =
            new SwerveModuleConstants(angleMotorID, driveMotorID, CANCoderID, driverID);
    }

    public static final double maxSpeed = 4;
    public static final double trackWidth = Units.inchesToMeters(27);
    public static final double wheelBase = Units.inchesToMeters(27);
    public static SwerveDriveKinematics swerveDriveKinematics =
        new SwerveDriveKinematics(new Translation2d(wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0));



    public static final class Swerve {
        public static final edu.wpi.first.wpilibj.SPI.Port navXID =
            edu.wpi.first.wpilibj.SPI.Port.kMXP;
        public static final boolean invertGyro = true;
        public static final double maxAngularVelocity = 2;
        public static final double wheelCircumference = Units.inchesToMeters(3.94) * Math.PI;
        public static final double angleGearRatio = (12.8 / 1.0);
        public static final double driveGearRatio = (8.14 / 1.0); // 6.86:1


    }


}
