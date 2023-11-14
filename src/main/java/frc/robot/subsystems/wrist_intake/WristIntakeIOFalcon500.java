package frc.robot.subsystems.wrist_intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.Constants;

/** Real Class for WristIntake */
public class WristIntakeIOFalcon500 implements WristIntakeIO {

    private final WPI_TalonFX wristIntakeMotor = new WPI_TalonFX(Constants.Wrist.WRIST_INTAKE_ID);

    /** Initializer for real WristIntake */
    public WristIntakeIOFalcon500() {
        wristIntakeMotor.setInverted(false);
        wristIntakeMotor.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    public void updateInputs(WristIntakeInputs inputs) {
        inputs.outputCurrentAmps = wristIntakeMotor.getStatorCurrent();
    }

    @Override
    public void setMotorPercentage(double percent) {
        wristIntakeMotor.set(ControlMode.PercentOutput, percent);
    }

    @Override
    public void setMotorVoltage(double voltage) {
        wristIntakeMotor.setVoltage(voltage);
    }
}
