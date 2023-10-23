package frc.robot.subsystems.arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticHub;
import frc.robot.Constants;


public class ArmIOReal implements ArmIO {


    // defining motors and Encoder
    private final CANSparkMax armMotor1 =
        new CANSparkMax(Constants.Arm.ARM_ID, MotorType.kBrushless);
    private final CANSparkMax armMotor2 =
        new CANSparkMax(Constants.Arm.ARM_ID_2, MotorType.kBrushless);
    private final SparkMaxAbsoluteEncoder armEncoder =
        armMotor1.getAbsoluteEncoder(Type.kDutyCycle);
    private final DoubleSolenoid armSolenoid;


    public final CANSparkMax wristMotor =
        new CANSparkMax(Constants.Wrist.WRIST_CAN_ID, MotorType.kBrushless);
    public final SparkMaxAbsoluteEncoder wristEncoder =
        wristMotor.getAbsoluteEncoder(Type.kDutyCycle);

    public ArmIOReal(PneumaticHub ph) {
        // ARM
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
        // WRIST
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
    public void updateInputs(ArmInputs inputs) {
        inputs.shoulderPosition = armEncoder.getPosition();
        inputs.wristPosition = armEncoder.getPosition();
    }

    @Override
    public void setArmVoltage(double armVoltage) {
        armMotor1.setVoltage(armVoltage);
        armMotor2.setVoltage(armVoltage);
    }

    @Override
    public void setWristVoltage(double wristVoltage) {
        wristMotor.setVoltage(wristVoltage);
    }

    @Override
    public void setArmSolenoid(Value value) {
        armSolenoid.set(value);

    }

}
