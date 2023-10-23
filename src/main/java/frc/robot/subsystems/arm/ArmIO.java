package frc.robot.subsystems.arm;

import org.littletonrobotics.junction.AutoLog;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;


public interface ArmIO {

    //
    @AutoLog
    public static class ArmInputs {
        public double armAngle;
        public double wristAngle;
        public double shoulderPosition;
        public double wristPosition;
    }

    /* Sets the numbers */
    public void updateInputs(ArmInputs inputs);

    public void setArmVoltage(double inputs);

    public void setWristVoltage(double inputs);

    public void setArmSolenoid(Value value);



}
