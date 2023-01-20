package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
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
        public static final int angleMotorID = 0;
        public static final int driveMotorID = 0;
        public static final int CANCoderID = 0;
        public static final double angleOffsetID = 0.0;
        public static final SwerveModuleConstants Constants =
            new SwerveModuleConstants(angleMotorID, driveMotorID, CANCoderID, driverID);
    }
    public static class ModuleOne {
        public static final int angleMotorID = 0;
        public static final int driveMotorID = 0;
        public static final int CANCoderID = 0;
        public static final double angleOffsetID = 0.0;
        public static final SwerveModuleConstants Constants =
            new SwerveModuleConstants(angleMotorID, driveMotorID, CANCoderID, driverID);
    }
    public static class ModuleTwo {
        public static final int angleMotorID = 0;
        public static final int driveMotorID = 0;
        public static final int CANCoderID = 0;
        public static final double angleOffsetID = 0.0;
        public static final SwerveModuleConstants Constants =
            new SwerveModuleConstants(angleMotorID, driveMotorID, CANCoderID, driverID);
    }
    public static class ModuleThree {
        public static final int angleMotorID = 0;
        public static final int driveMotorID = 0;
        public static final int CANCoderID = 0;
        public static final double angleOffsetID = 0.0;
        public static final SwerveModuleConstants Constants =
            new SwerveModuleConstants(angleMotorID, driveMotorID, CANCoderID, driverID);
    }

    public static final double trackWidth = 0.0;
    public static final double wheelBase = 0.0;
    public static SwerveDriveKinematics swerveDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(wheelBase, trackWidth), new Translation2d(-wheelBase, trackWidth),
        new Translation2d(wheelBase, -trackWidth), new Translation2d(-wheelBase, -trackWidth));


}
