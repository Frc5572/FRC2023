package frc.robot.subsystems.arm;

import org.littletonrobotics.junction.AutoLog;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public interface ArmIO {

    @AutoLog
    public static class ArmInputs {
        public double shoulderPosition = 0.0;
        public double wristPosition = 0.0;
    }

    public default void updateInputs(ArmInputs inputs) {}

    public default void setSolenoid(Value value) {}

    public default void setArmVoltages(double armVoltage, double wristVoltage) {}

}
