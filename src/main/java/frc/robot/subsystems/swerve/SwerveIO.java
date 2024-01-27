package frc.robot.subsystems.swerve;

import org.littletonrobotics.junction.AutoLog;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.lib.util.swerve.SwerveModule;
import frc.lib.util.swerve.SwerveModuleIO;

/** IO Class for Swerve */
public interface SwerveIO {

    /** Inputs Class for Swerve */
    @AutoLog
    public static class SwerveInputs {
        public float yaw;
        public float roll;
    }

    public default void updateInputs(SwerveInputs inputs) {}

    /**
     * Create a Swerve Module for the repsective IO Layer
     *
     * @param moduleNumber The Module Number
     * @param driveMotorID The Drive Motor CAN ID
     * @param angleMotorID The Angle Motor CAN ID
     * @param cancoderID The CANCode CAN ID
     * @param angleOffset The Angle Offset in {@link Rotation2d}
     * @return {@link SwerveModule}
     */
    public default SwerveModule createSwerveModule(int moduleNumber, int driveMotorID,
        int angleMotorID, int cancoderID, Rotation2d angleOffset) {
        return new SwerveModule(moduleNumber, driveMotorID, angleMotorID, cancoderID, angleOffset,
            new SwerveModuleIO() {

            });
    }

    public default void zeroGyro() {}
}
