package frc.robot.subsystems.arm;

import org.littletonrobotics.junction.AutoLog;

public class ArmIO {

    public double armEncoderPos;

    @AutoLog
    public static class ArmInputs {
        public double armAngleRad;        
        public double armVoltage;
        public double wristVoltage;
        
        public boolean isExtended;
    }

    public void updateInputs(ArmInputs inputs) {
        inputs.armAngleRad = armEncoderPos;
    }

    
    public  void setArmVoltage(ArmInputs inputs, double value) {
        inputs.armVoltage = value;
    }

    
    public void setWristVoltage(ArmInputs inputs, double value){
        inputs.wristVoltage = value;
    }

    
    public void setArmSolenoid(ArmInputs inputs, boolean value){
        inputs.isExtended = value;
    }
  
}
