package frc.robot.subsystems.wristIntake;

import org.littletonrobotics.junction.AutoLog;

public interface WristIntakeIO {

    @AutoLog
    public static class WristIntakeInputs {
        public double wristAngleRad;
        public double wristVelocityRotPerSecond;
    }

    public default void setMotorVoltage(double voltage) {}

    public default void setMotorPercentage(double percent) {}

    public default void getVoltage() {}

    public default double getOutputCurrentAmps() {
        return 0.0;
    }

}
