package frc.robot;

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
     * Vision constants for limelight calculations.
     */
    public static final class VisionConstants {
        public static final double deadPocket = 0.05;
        public static final double limelightHeight = 0;
        public static final double targetHeight = 0;
        public static final double limelightAngle = 0;
    }

    /*
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
