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
    // note quadratic curve only applies to deadband of 0.1 and 0.2
    public static final double STICK_DEADBAND = 0.1;
    public static final int DRIVER_ID = 0;
    public static final int OPERATOR_ID = 1;

    /**
     * Arm constants.
     */
    public static final class ArmConstant {
        public static final int LEFT_SIDE_CAN_ID = 9;
        public static final int RIGHT_SIDE_CAN_ID = 10;
    }

    /**
     * Elevator constants.
     */
    public static final class ElevatorConstants {
        public static final int CAN_ID = 11;
    }

    /**
     * Wrist constants.
     */
    public static final class WristConstants {
        public static final int WRIST_CAN_ID = 12;
        public static final int LEFT_INTAKE_MOTOR_CAN_ID = 13;
        public static final int RIGHT_INTAKE_MOTOR_CAN_ID = 16;

    }

    /**
     * Wrist constants.
     */
    public static final class DropdownIntake {
        public static final int DROPDOWN_MOTOR_CAN_ID = 14;
        public static final int INTAKE_MOTOR_CAN_ID = 15;
    }


    /**
     * LED constants.
     */
    public static final class LEDConstants {
        public static final int PWM_PORT = 9;
        public static final int LED_COUNT = 36;
    }

    /**
     * Motor CAN id's. PID constants for Swerve Auto Holonomic Drive Controller.
     */
    public static class SwerveTransformPID {
        public static final double PID_XKP = 1.5;
        public static final double PID_XKI = 0.0;
        public static final double PID_XKD = 0.0;
        public static final double PID_YKP = 1.5;
        public static final double PID_YKI = 0.0;
        public static final double PID_YKD = 0.0;
        public static final double PID_TKP = 3.0;
        public static final double PID_TKI = 0.0;
        public static final double PID_TKD = 0.0;

        public static final double MAX_ANGULAR_VELOCITY = 3.0;
        public static final double MAX_ANGULAR_ACCELERATION = 3.0;
        public static final double STD_DEV_MOD = 2.0;
    }

    /**
     * Camera offset constants.
     */
    public static class CameraConstants {

        public static final double ROLL = Math.PI;
        public static final double PITCH = 0 * Math.PI / 180;
        public static final double YAW = 0.0;
        public static final Transform3d KCAMERA_TO_ROBOT =
            new Transform3d(new Translation3d(Units.inchesToMeters(-10), Units.inchesToMeters(0),
                Units.inchesToMeters(-7.5)), new Rotation3d(ROLL, PITCH, YAW));
        public static final String CAMERA_NAME = "pv2";
        public static final double LARGEST_DISTANCE = 0.1;
    }

    /**
     * Swerve ID's.
     */
    public static final class Swerve {
        public static final edu.wpi.first.wpilibj.SPI.Port navXID =
            edu.wpi.first.wpilibj.SPI.Port.kMXP;
        public static final boolean INVERT_GYRO = true; // Always ensure Gyro is CCW+ CW-

        /* Drivetrain Constants */
        // Front-Back distance.
        public static final double TRACK_WIDTH = Units.inchesToMeters(22);
        // Left-Right Distance
        public static final double WHEEL_BASE = Units.inchesToMeters(22);
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
            public static final int DRIVE_MOTOR_ID = 6;
            public static final int ANGLE_MOTOR_ID = 8;
            public static final int CAN_CODER_ID = 4;
            public static final double ANGLE_OFFSET = 212.607;
            public static final SwerveModuleConstants constants = new SwerveModuleConstants(
                DRIVE_MOTOR_ID, ANGLE_MOTOR_ID, CAN_CODER_ID, ANGLE_OFFSET);
        }

        /**
         * Front Right Module - Module 1.
         */
        public static final class Mod1 {
            public static final int DRIVE_MOTOR_ID = 1;
            public static final int ANGLE_MOTOR_ID = 4;
            public static final int CAN_CODER_ID = 1;
            public static final double ANGLE_OFFSET = 280.195;
            public static final SwerveModuleConstants constants = new SwerveModuleConstants(
                DRIVE_MOTOR_ID, ANGLE_MOTOR_ID, CAN_CODER_ID, ANGLE_OFFSET);
        }

        /**
         * Back Left Module - Module 2.
         */
        public static final class Mod2 {
            public static final int DRIVE_MOTOR_ID = 3;
            public static final int ANGLE_MOTOR_ID = 2;
            public static final int CAN_CODER_ID = 2;
            public static final double ANGLE_OFFSET = 111.621;
            public static final SwerveModuleConstants constants = new SwerveModuleConstants(
                DRIVE_MOTOR_ID, ANGLE_MOTOR_ID, CAN_CODER_ID, ANGLE_OFFSET);
        }

        /**
         * Back Right Module - Module 3.
         */
        public static final class Mod3 {
            public static final int DRIVE_MOTOR_ID = 7;
            public static final int ANGLE_MOTOR_ID = 5;
            public static final int CAN_CODER_ID = 3;
            public static final double ANGLE_OFFSET = 248.467;
            public static final SwerveModuleConstants constants = new SwerveModuleConstants(
                DRIVE_MOTOR_ID, ANGLE_MOTOR_ID, CAN_CODER_ID, ANGLE_OFFSET);
        }



    }

    /**
     * Motor CAN id's.
     */
    public static final class Motors {
        public static final int motorID1 = 0;
        public static final int motorID2 = 0;
        public static final int wristMotorID = 0;
        public static final int wristCoderID = 0;



        // ...
    }

    /**
     * Pneumatics CAN id constants.
     */
    public static final class Pneumatics {
    }



    /**
     * Arm PID constants
     */
    public static final class ArmPID {
        public static final double kP = 0;
        public static final double kI = 0;
        public static final double kD = 0;
        public static final double kF = 0;
        public static final double kGVolts = 0;
        public static final double kSVolts = 0;
        public static final double kMaxVelocityRadPerSecond = 0;
        public static final double kMaxAccelerationRadPerSecSquared = 0;
        public static final double kVVoltSecondPerRad = 0;
        public static final double kAVoltSecondSquaredPerRad = 0;
        public static final double kEncoderDistancePerPulse = 0;
    }

    /**
     * WristPID id Constants
     */
    public static final class WristPID {
        public static final double kP = 0.0;
        public static final double kI = 0.0;
        public static final double kD = 0.0;
        public static final double kMaxVelocityRadPerSecond = 0;
        public static final double kMaxAccelerationRadPerSecond = 0;
        public static final double kF = 0.0;
        public static final double kSVolts = 0.0;
        public static final double kVVoltSecondsPerRotation = 0.0;
        public static final double TargetEncoderDegree = 0.0;


    }

}

