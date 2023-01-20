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
        public static final int leftElevatorMotorID = 0;
        public static final int rightElevatorMotorID = 0;


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

    /**
     * Motor constants for neo calculations.
     */
    public static final class Elevator {
        public static final double motorExtendSpeed = 0;
        public static final double motorRetractSpeed = 0;
        public static final double pos1EncoderTics = 0;
        public static final double pos2EncoderTics = 0;
        public static final double pos3EncoderTics = 0;
        public static final double pos4EncoderTics = 0;
    }
}
