package frc.robot.subsystems.arm;

import org.littletonrobotics.junction.AutoLog;

/** IO Class for Arm */
public class ArmIO {

    /** Inputs Class for Arm */
    @AutoLog
    public static class ArmInputs {
        public double armAngleRad;
        public double wristAngleRad;
        public double armVoltage;
        public double wristVoltage;
        public boolean extended;
    }

    /** Updates the set of loggable inputs. */
    public void updateInputs(ArmInputs inputs) {}

    public void setArmVoltage(ArmInputs inputs, double value) {
        inputs.armVoltage = value;
    }

    public void setWristVoltage(ArmInputs inputs, double value) {
        inputs.wristVoltage = value;
    }

    public void setArmSolenoid(ArmInputs inputs, boolean value) {
        inputs.extended = value;
    }
}
