package frc.lib.math;

/**
 * Custom constants for each swerve module.
 */
public class SwerveModuleConstants {
    public int angleMotorID;
    public int driveMotorID;
    public int canCoderID;
    public double angleOffsetID;

    /**
     * @param driveMotorID CAN ID of the driving motor
     * @param angleMotorID CAN ID of the agnle motor
     * @param canCoderID CAN ID of the encoder
     * @param angleOffsetID Offset angle of something
     */
    public SwerveModuleConstants(int angleMotorID, int driveMotorID, int canCoderID,
        int angleOffsetID) {
        this.angleMotorID = angleMotorID;
        this.driveMotorID = driveMotorID;
        this.canCoderID = canCoderID;
        this.angleOffsetID = angleOffsetID;

    }
}
