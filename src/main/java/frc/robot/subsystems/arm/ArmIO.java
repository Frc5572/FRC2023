package frc.robot.subsystems.arm;

import org.littletonrobotics.junction.AutoLog;

public interface ArmIO {

    //
    @AutoLog
    public static class ArmInputs {
        public double armAngle;
        public double wristAngle;
        public double armVoltage;
        public double wristVoltage;
        public boolean extended;
    }

    /* Sets the numbers */
    public void updateInputs(ArmInputs inputs){}

    public void setArmVoltage(ArmInputs inputs, double value){
        inputs.armVoltage = value;
    }

    public void setWristVoltage(ArmInputs inputs, double value){
        inputs.wristVoltage = value;
    }

    public void setArmSolenoid(ArmInputs inputs, boolean value){
        inputs.extended = value;
    }


}
