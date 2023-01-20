package frc.lib.math;

public class SwerveModuleConstants {
    public int angleMotorID;
    public int driveMotorID;
    public int CANCoderID;
    public double angleOffsetID;

    public SwerveModuleConstants(int angleMotorID, int driveMotorID, int CANCoderID,
        int angleOffsetID) {
        this.angleMotorID = angleMotorID;
        this.driveMotorID = driveMotorID;
        this.CANCoderID = CANCoderID;
        this.angleOffsetID = angleOffsetID;

    }
}
