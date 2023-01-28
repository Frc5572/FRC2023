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
    }

    /**
     * Pneumatics CAN id constants.
     */
    public static final class Pneumatics {
        public static final int intakeReverseChannel = 0;
        public static int intakeFowardChannel = 1;

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
     * Constants for the Intake.
     */
    public static final class IntakeConstants {
        public static final int intakeMotorNumA = 0;
        public static final int intakeMotorNumB = 0;
        public static final int intakeInSpeed = 1;
        public static final int intakeOutSpeed = -1;
        public static final int intakeStopSpeed = 0;
    }
}
