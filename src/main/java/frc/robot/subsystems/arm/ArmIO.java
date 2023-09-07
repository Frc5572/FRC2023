package frc.robot.subsystems.arm;

import org.littletonrobotics.junction.AutoLog;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public interface ArmIO {

    @AutoLog
    public static class ArmInputs {
        public double armAngleRad;
        public double armVelocityRotPerSecond;
    }

    /** Updates the set of loggable inputs. */
    public default void updateInputs(ArmInputs inputs) {}

    public default void setArmVoltage(double value) {}

    public default void setWristVoltage(double value) {}

    public default double getWristPosition() {
        return 0.0;
    }

    public default void setArmSolenoid(Value value) {}

    public default void setExtended(boolean value) {}

    public default double getArmRotationDeg() {
        return 0.0;
    }

}
