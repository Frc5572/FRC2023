package frc.robot.subsystems.arm;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticHub;
import frc.robot.Constants;

public class ArmIOReal implements ArmIO {

    private final CANSparkMax armMotor1 =
        new CANSparkMax(Constants.Arm.ARM_ID, MotorType.kBrushless);
    private final CANSparkMax armMotor2 =
        new CANSparkMax(Constants.Arm.ARM_ID_2, MotorType.kBrushless);
    private final AbsoluteEncoder armEncoder = armMotor1.getAbsoluteEncoder(Type.kDutyCycle);
    private final DoubleSolenoid armSolenoid;

    ProfiledPIDController armPIDController =
        new ProfiledPIDController(Constants.Arm.PID.kP, Constants.Arm.PID.kI, Constants.Arm.PID.kD,
            new TrapezoidProfile.Constraints(Constants.Arm.PID.MAX_VELOCITY,
                Constants.Arm.PID.MAX_ACCELERATION));

    // WRIST
    public final CANSparkMax wristMotor =
        new CANSparkMax(Constants.Wrist.WRIST_MOTOR_ID, MotorType.kBrushless);
    private final AbsoluteEncoder wristEncoder = wristMotor.getAbsoluteEncoder(Type.kDutyCycle);

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
        inputs.wristPosition = wristEncoder.getPosition();
    }

    @Override
    public void setArmVoltages(double armVoltage, double wristVoltage) {
        armMotor1.setVoltage(armVoltage);
        armMotor2.setVoltage(armVoltage);
        wristMotor.setVoltage(wristVoltage);
    }

    @Override
    public void setSolenoid(Value value) {
        armSolenoid.set(value);
    }

}
