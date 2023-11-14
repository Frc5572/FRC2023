package frc.robot.subsystems.swerve;

import org.littletonrobotics.junction.AutoLog;

public interface SwerveIO {
    @AutoLog
    public static class SwerveInputs{
        public double yaw;
    }
    public default void updateInputs(SwerveInputs inputs){}
}
