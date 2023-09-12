package frc.robot.subsystems.swerve;

import org.littletonrobotics.junction.AutoLog;
import frc.lib.util.swerve.SwerveModuleConstants;

public interface SwerveIO {

    @AutoLog
    public static class SwerveInputs {

    }

    public default void updateInputs(SwerveInputs inputs) {}

    public default SwerveModIO newSwerveMod(int idx, SwerveModuleConstants constants) {
        return new SwerveModIO() {};
    }

}
