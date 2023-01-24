package frc.lib.math;

/**
 * Custom constants for each swerve module.
 */
public class SwerveModuleConstants {
    public int angleMotorID;
    public int driveMotorID;
    public int CANCoderID;
    public double angleOffsetID;

    /**
     * @param driveMotorID CAN ID of the driving motor
     * @param angleMotorID CAN ID of the agnle motor
     * @param canCoderID CAN ID of the encoder
     * @param angleOffset Offset angle of something
     */
    public SwerveModuleConstants(int angleMotorID, int driveMotorID, int CANCoderID,
        int angleOffsetID) {
        this.angleMotorID = angleMotorID;
        this.driveMotorID = driveMotorID;
        this.CANCoderID = CANCoderID;
        this.angleOffsetID = angleOffsetID;

    }
}
