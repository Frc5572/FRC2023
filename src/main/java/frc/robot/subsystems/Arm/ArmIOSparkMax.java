package frc.robot.subsystems.arm;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticHub;
import frc.robot.Constants;


public class ArmIOSparkMax extends ArmIO {
    private final CANSparkMax armMotor1 =
        new CANSparkMax(Constants.Arm.ARM_ID, MotorType.kBrushless);
    private final CANSparkMax armMotor2 =
        new CANSparkMax(Constants.Arm.ARM_ID_2, MotorType.kBrushless);
    private final AbsoluteEncoder armEncoder = armMotor1.getAbsoluteEncoder(Type.kDutyCycle);
    private final DoubleSolenoid armSolenoid;

    private final CANSparkMax wristMotor =
        new CANSparkMax(Constants.Wrist.WRIST_MOTOR_ID, MotorType.kBrushless);
    private final AbsoluteEncoder wristEncoder = wristMotor.getAbsoluteEncoder(Type.kDutyCycle);

    public ArmIOSparkMax(PneumaticHub ph) {
        armMotor1.restoreFactoryDefaults();
        armMotor2.restoreFactoryDefaults();
        armMotor1.setIdleMode(IdleMode.kBrake);
        armMotor2.setIdleMode(IdleMode.kBrake);
        armMotor1.setInverted(false);
        armMotor2.setInverted(true);
        armEncoder.setPositionConversionFactor(360);
        armEncoder.setVelocityConversionFactor(360);
        armEncoder.setInverted(true);
        armEncoder.setZeroOffset(Constants.Arm.encoder1Offset);

        armMotor1.burnFlash();
        armMotor2.burnFlash();
        wristMotor.restoreFactoryDefaults();
        wristMotor.setIdleMode(IdleMode.kCoast);
        wristMotor.setInverted(false);
        wristEncoder.setPositionConversionFactor(360);
        wristEncoder.setVelocityConversionFactor(360);
        wristEncoder.setInverted(false);
        wristEncoder.setZeroOffset(Constants.Wrist.encoderOffset);
        wristMotor.burnFlash();

        this.armSolenoid = ph.makeDoubleSolenoid(Constants.Arm.SOLENOID_FORWARD_CHANNEL,
            Constants.Arm.SOLENOID_REVERSE_CHANNEL);
    }

    @Override
    public void setArmSolenoid(ArmInputs inputs, boolean value) {
        super.setArmSolenoid(inputs, value);
        armSolenoid.set(value ? Value.kForward : Value.kReverse);
    }

    @Override
    public void setArmVoltage(ArmInputs inputs, double value) {
        super.setArmVoltage(inputs, value);
        armMotor1.setVoltage(value);
        armMotor2.setVoltage(value);
    }

    @Override
    public void setWristVoltage(ArmInputs inputs, double value) {
        super.setWristVoltage(inputs, value);
        wristMotor.setVoltage(value);
    }

}
