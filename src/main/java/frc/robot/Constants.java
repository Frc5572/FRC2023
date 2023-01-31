package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;
import frc.lib.util.swerve.SwerveModuleConstants;

/**
 * Constants file.
 */

public final class Constants {
    public static final double STICK_DEADBAND = 0.1;
    public static final int DRIVER_ID = 0;
    public static final int OPERATOR_ID = 1;

    /**
     * PID constants for Swerve Auto Holonomic Drive Controller
     */
    public static class SwerveTransformPID {
        public static final double pidXkP = 1.5;
        public static final double pidXkI = 0.0;
        public static final double pidXkD = 0.0;
        public static final double pidYkP = 1.5;
        public static final double pidYkI = 0.0;
        public static final double pidYkD = 0.0;
        public static final double pidTkP = 3.0;
        public static final double pidTkI = 0.0;
        public static final double pidTkD = 0.0;

        public static final double maxAngularVelocity = 3.0;
        public static final double maxAngularAcceleration = 3.0;
        public static final double stdDevMod = 2.0;
    }

    /**
     * Camera offset constants
     */
    public static class CameraConstants {

        public static final double pitch = 0 * Math.PI / 180;
        public static final double roll = Math.PI;
        public static final double yaw = 0.0;
        public static final Transform3d kCameraToRobot =
            new Transform3d(new Translation3d(Units.inchesToMeters(-10), Units.inchesToMeters(0),
                Units.inchesToMeters(-7.5)), new Rotation3d(roll, pitch, yaw));
        public static final String cameraName = "pv2";
        public static final double largestDistance = 0.1;
    }

    /**
     * Swerve ID's
     */
    public static final class Swerve {
        public static final edu.wpi.first.wpilibj.SPI.Port navXID =
            edu.wpi.first.wpilibj.SPI.Port.kMXP;
        public static final boolean INVERT_GYRO = false; // Always ensure Gyro is CCW+ CW-

        /* Drivetrain Constants */
        // Front-Back distance
        public static final double TRACK_WIDTH = Units.inchesToMeters(14);
        // Left-Right Distance
        public static final double WHEEL_BASE = Units.inchesToMeters(14);
        public static final double WHEEL_DIAMETER = Units.inchesToMeters(4);
        public static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;

        public static final boolean IS_FIELD_RELATIVE = true;
        public static final boolean IS_OPEN_LOOP = false;

        public static final double OPEN_LOOP_RAMP = 0.25;
        public static final double CLOSED_LOOP_RAMP = 0.4;

        public static final double DRIVE_GEAR_RATIO = (8.14 / 1.0); // 6.86:1
        public static final double ANGLE_GEAR_RATIO = (12.8 / 1.0); // 12.8:1

        public static final SwerveDriveKinematics SWERVE_KINEMATICS =
            new SwerveDriveKinematics(new Translation2d(WHEEL_BASE / 2.0, TRACK_WIDTH / 2.0),
                new Translation2d(WHEEL_BASE / 2.0, -TRACK_WIDTH / 2.0),
                new Translation2d(-WHEEL_BASE / 2.0, TRACK_WIDTH / 2.0),
                new Translation2d(-WHEEL_BASE / 2.0, -TRACK_WIDTH / 2.0));

        /* Swerve Current Limiting */
        public static final int ANGLE_CONTINOUS_CURRENT_LIMIT = 25;
        public static final int ANGLE_PEAK_CURRENT_LIMIT = 40;
        public static final double ANGLE_PEAK_CURRENT_DURATION = 0.1;
        public static final boolean ANGLE_ENABLE_CURRENT_LIMIT = true;

        public static final int DRIVE_CONTINOUS_CURRENT_LIMIT = 35;
        public static final int DRIVE_PEAK_CURRENT_LIMIT = 60;
        public static final double DRIVE_PEAK_CURRENT_DURATION = 0.1;
        public static final boolean DRIVE_ENABLE_CURRENT_LIMIT = true;

        /* Angle Motor PID Values */
        public static final double ANGLE_KP = 0.6;
        public static final double ANGLE_KI = 0.0;
        public static final double ANGLE_KD = 12.0;
        public static final double ANGLE_KF = 0.0;

        /* Drive Motor PID Values */
        public static final double DRIVE_KP = 0.10;
        public static final double DRIVE_KI = 0.0;
        public static final double DRIVE_KD = 0.0;
        public static final double DRIVE_KF = 0.0;

        /* Drive Motor Characterization Values */
        public static final double DRIVE_KS = (0.667 / 12);
        // divide by 12 to convert from volts to percent output for CTRE
        public static final double DRIVE_KV = (2.44 / 12);
        public static final double DRIVE_KA = (0.27 / 12);

        /* Swerve Profiling Values */
        public static final double MAX_SPEED = 4; // meters per second
        public static final double MAX_ANGULAR_VELOCITY = 2;

        /* Neutral Modes */
        public static final NeutralMode ANGLE_NEUTRAL_MODE = NeutralMode.Coast;
        public static final NeutralMode DRIVE_NEUTRAL_MODE = NeutralMode.Brake;

        /* Motor Inverts */
        public static final boolean DRIVE_MOTOR_INVERT = false;
        public static final boolean ANGLE_MOTOT_INVERT = false;

        /* Angle Encoder Invert */
        public static final boolean CAN_CODER_INVERT = false;

        /* Module Specific Constants */
        /**
         * Front Left Module - Module 0.
         */
        public static final class Mod0 {
            public static final int DRIVE_MOTOR_ID = 50;
            public static final int ANGLE_MOTOR_ID = 8;
            public static final int CAN_CODER_ID = 3;
            public static final double ANGLE_OFFSET = 138.604;
            public static final SwerveModuleConstants constants = new SwerveModuleConstants(
                DRIVE_MOTOR_ID, ANGLE_MOTOR_ID, CAN_CODER_ID, ANGLE_OFFSET);
        }

        /**
         * Front Right Module - Module 1. PROBLEM CHILD
         */
        public static final class Mod1 {
            public static final int DRIVE_MOTOR_ID = 51;
            public static final int ANGLE_MOTOR_ID = 52;
            public static final int CAN_CODER_ID = 2;
            public static final double ANGLE_OFFSET = 66.445;
            public static final SwerveModuleConstants constants = new SwerveModuleConstants(
                DRIVE_MOTOR_ID, ANGLE_MOTOR_ID, CAN_CODER_ID, ANGLE_OFFSET);
        }

        /**
         * Back Left Module - Module 2.
         */
        public static final class Mod2 {
            public static final int DRIVE_MOTOR_ID = 4;
            public static final int ANGLE_MOTOR_ID = 2;
            public static final int CAN_CODER_ID = 1;
            public static final double ANGLE_OFFSET = 259.805;
            public static final SwerveModuleConstants constants = new SwerveModuleConstants(
                DRIVE_MOTOR_ID, ANGLE_MOTOR_ID, CAN_CODER_ID, ANGLE_OFFSET);
        }

        /**
         * Back Right Module - Module 3.
         */
        public static final class Mod3 {
            public static final int DRIVE_MOTOR_ID = 3;
            public static final int ANGLE_MOTOR_ID = 53;
            public static final int CAN_CODER_ID = 4;
            public static final double ANGLE_OFFSET = 31.992;
            public static final SwerveModuleConstants constants = new SwerveModuleConstants(
                DRIVE_MOTOR_ID, ANGLE_MOTOR_ID, CAN_CODER_ID, ANGLE_OFFSET);
        }

    }



    /**
     * Autonomous constants for swerve bot.
     */
    public static final class VisionConstants {
        public static final double DEAD_POCKET = 0.05;
        public static final double LIME_LIGHT_HEIGHT = 0;
        public static final double TARGET_HEIGHT = 0;
        public static final double LIME_LIGHT_ANGLE = 0;
    }



    /**
     * Elevator Motor constants.
     */
    public static final class Elevator {
        /* Motor ID's. */
        public static final int leftElevatorMotorID = 0;
        public static final int rightElevatorMotorID = 0;

        /* Encoder ticks. */
        public static final double stowedAway = 0.0;
        public static final double ground = 0.0;
        public static final double middleLevel = 0.0;
        public static final double highLevel = 0.0;
        public static final double humanPlayerStation = 0.0;

        /* Elevator motor speed. */
        public static final double elevatorSpeed = 0.0;
        public static final double elevatorStop = 0.0;

        /* Elevator motor PID's. */
        public static final double elevatorKP = 0.0;
        public static final double elevatorKI = 0.0;
        public static final double elevatorKD = 0.0;

        public static final double elevatorKMaxVelocityRadPerSecond = 0.0;
        public static final double elevatorKMaxAccelerationRadPerSecSquared = 0.0;
        public static final double elevatorKSVolts = 0.0;
        public static final double elevatorKGVolts = 0.0;
        public static final double elevatorKVVoltsSecondsPerRotation = 0.0;

        /* Elevator Encoder */
        public static final double encoderOffSet = 0.0;
    }
}
