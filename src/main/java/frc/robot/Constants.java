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
        // Encoder ticks.
        public static final double elevatorPos0 = 0;
        public static final double elevatorPos1 = 0;
        public static final double elevatorPos2 = 0;
        public static final double elevatorPos3 = 0;



        // Need someone to review my use of this array, I'm unsure about the use
        public static final double[] elevatorEncoderPos =
            {elevatorPos0, elevatorPos1, elevatorPos2, elevatorPos3};



        // Motor speed.
        public static final double elevatorSpeed = 0;
        public static final double elevatorStop = 0;
    }
}
