package frc.robot.subsystems.wrist_intake;

import org.littletonrobotics.junction.AutoLog;

/** IO Class for WristIntake */
public interface WristIntakeIO {

    /** Inputs Class for WristIntake */
    @AutoLog
    public static class WristIntakeInputs {
        public double wristAngleRad;
        public double wristVelocityRotPerSecond;
        public double voltage;
        public double outputCurrentAmps;
    }

    public default void updateInputs(WristIntakeInputs inputs) {}

    public default void setMotorVoltage(double voltage) {}

    public default void setMotorPercentage(double percent) {}

}
