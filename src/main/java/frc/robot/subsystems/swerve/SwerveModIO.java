package frc.robot.subsystems.swerve;

import org.littletonrobotics.junction.AutoLog;

public interface SwerveModIO {

    @AutoLog
    public static class SwerveModInputs {

    }

    public default void updateInputs(SwerveModInputs inputs) {}

}
