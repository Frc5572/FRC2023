package frc.robot.subsystems;

import java.util.Map;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.math.DoubleJointedArmFeedforward;
import frc.lib.math.ProfiledPIDControllerPosVel;
import frc.robot.Constants;
import frc.robot.RobotContainer;

/**
 * Creates the subsystem for the arm.
 */
public class Arm extends SubsystemBase {
    private final CANSparkMax armMotor1 =
        new CANSparkMax(Constants.Arm.ARM_ID, MotorType.kBrushless);
    private final CANSparkMax armMotor2 =
        new CANSparkMax(Constants.Arm.ARM_ID_2, MotorType.kBrushless);
    private final AbsoluteEncoder armEncoder = armMotor1.getAbsoluteEncoder(Type.kDutyCycle);
    private final DoubleSolenoid armSolenoid;

    ProfiledPIDControllerPosVel armPIDController =
        new ProfiledPIDControllerPosVel(Constants.Arm.PID.kP, Constants.Arm.PID.kI,
            Constants.Arm.PID.kD, new TrapezoidProfile.Constraints(Constants.Arm.PID.MAX_VELOCITY,
                Constants.Arm.PID.MAX_ACCELERATION));

    // WRIST
    public final CANSparkMax wristMotor =
        new CANSparkMax(Constants.Wrist.WRIST_MOTOR_ID, MotorType.kBrushless);
    private final AbsoluteEncoder wristEncoder = wristMotor.getAbsoluteEncoder(Type.kDutyCycle);

    ProfiledPIDControllerPosVel wristPIDController =
        new ProfiledPIDControllerPosVel(Constants.Wrist.PID.kP, Constants.Wrist.PID.kI,
            Constants.Wrist.PID.kD, new TrapezoidProfile.Constraints(
                Constants.Wrist.PID.MAX_VELOCITY, Constants.Wrist.PID.MAX_ACCELERATION));

    private GenericEntry armAngleWidget = RobotContainer.mainDriverTab
        .add("Arm Angle", armEncoder.getPosition()).withWidget(BuiltInWidgets.kDial)
        .withProperties(Map.of("min", 0, "max", 150)).withPosition(8, 1).withSize(2, 2).getEntry();
    private GenericEntry solenoidStatus = RobotContainer.mainDriverTab.add("Grabber Open", false)
        .withWidget(BuiltInWidgets.kBooleanBox)
        .withProperties(Map.of("Color when true", "green", "Color when false", "red"))
        .withPosition(6, 4).withSize(2, 1).getEntry();

    private DoubleJointedArmFeedforward feedforward =
        new DoubleJointedArmFeedforward(Constants.Arm.config, Constants.Wrist.config);

    private boolean enablePID = false;

    /**
     * Arm Subsystem
     */
    public Arm(PneumaticHub ph) {
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
        wristMotor.setIdleMode(IdleMode.kBrake);
        wristMotor.setInverted(false);
        wristEncoder.setPositionConversionFactor(360);
        wristEncoder.setVelocityConversionFactor(360);
        wristEncoder.setInverted(false);
        wristEncoder.setZeroOffset(Constants.Wrist.encoderOffset);
        wristMotor.burnFlash();

        this.armSolenoid = ph.makeDoubleSolenoid(Constants.Arm.SOLENOID_FORWARD_CHANNEL,
            Constants.Arm.SOLENOID_REVERSE_CHANNEL);

        enablePID = false;

    }

    @Override
    public void periodic() {
        armAngleWidget.setDouble(armEncoder.getPosition());
        solenoidStatus.setBoolean(this.armSolenoid.get() == Value.kReverse);

        if (enablePID) {
            State armState = armPIDController.calculate(getArmAngleRad());
            State wristState = wristPIDController.calculate(getWristAngleRad());

            var voltages =
                feedforward.calculate(VecBuilder.fill(armState.position, wristState.position),
                    VecBuilder.fill(armState.velocity, wristState.velocity), VecBuilder.fill(0, 0));
            armMotor1.setVoltage(voltages.get(0, 0));
            armMotor2.setVoltage(voltages.get(0, 0));
            wristMotor.setVoltage(voltages.get(1, 0));
        }
    }

    public double getArmAngle() {
        return armEncoder.getPosition();
    }

    public double getArmAngleRad() {
        return Rotation2d.fromDegrees(getArmAngle()).getRadians();
    }

    public double getWristAngle() {
        return wristEncoder.getPosition();
    }

    public double getWristAngleRad() {
        return Rotation2d.fromDegrees(getWristAngle()).getRadians();
    }

    public void setArmGoal(double goal) {
        armPIDController.setGoal(Rotation2d.fromDegrees(goal).getRadians());
    }

    public void setWristGoal(double goal) {
        wristPIDController.setGoal(Rotation2d.fromDegrees(goal).getRadians());
    }

    public void extendArm() {
        armSolenoid.set(Value.kReverse);
    }

    public void retractArm() {
        armSolenoid.set(Value.kForward);
    }
}
