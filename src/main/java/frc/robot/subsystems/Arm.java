package frc.robot.subsystems;

import java.util.Map;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.math.DoubleJointedArmFeedforward;
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

    ProfiledPIDController armPIDController =
        new ProfiledPIDController(Constants.Arm.PID.kP, Constants.Arm.PID.kI, Constants.Arm.PID.kD,
            new TrapezoidProfile.Constraints(Constants.Arm.PID.MAX_VELOCITY,
                Constants.Arm.PID.MAX_ACCELERATION));

    // WRIST
    public final CANSparkMax wristMotor =
        new CANSparkMax(Constants.Wrist.WRIST_MOTOR_ID, MotorType.kBrushless);
    private final AbsoluteEncoder wristEncoder = wristMotor.getAbsoluteEncoder(Type.kDutyCycle);

    ProfiledPIDController wristPIDController = new ProfiledPIDController(Constants.Wrist.PID.kP,
        Constants.Wrist.PID.kI, Constants.Wrist.PID.kD, new TrapezoidProfile.Constraints(
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
        armMotor1.setInverted(true);
        armMotor2.setInverted(false);
        armEncoder.setPositionConversionFactor(360);
        armEncoder.setVelocityConversionFactor(360);
        armEncoder.setInverted(true);
        armEncoder.setZeroOffset(Constants.Arm.encoder1Offset);

        armMotor1.burnFlash();
        armMotor2.burnFlash();
        // WRIST
        wristMotor.restoreFactoryDefaults();
        wristMotor.setIdleMode(IdleMode.kBrake);
        wristMotor.setInverted(true);
        wristEncoder.setPositionConversionFactor(360);
        wristEncoder.setVelocityConversionFactor(360);
        wristEncoder.setInverted(true);
        wristEncoder.setZeroOffset(Constants.Wrist.encoderOffset);
        wristMotor.burnFlash();

        this.armSolenoid = ph.makeDoubleSolenoid(Constants.Arm.SOLENOID_FORWARD_CHANNEL,
            Constants.Arm.SOLENOID_REVERSE_CHANNEL);


        // armPIDController.enableContinuousInput(0, 2 * Math.PI);
        // armPidController2.enableContinuousInput(0, 2 * Math.PI);
        // wristPIDController.enableContinuousInput(0, 2 * Math.PI);

        enablePID = false;

    }

    @Override
    public void periodic() {
        armAngleWidget.setDouble(armEncoder.getPosition());
        solenoidStatus.setBoolean(this.armSolenoid.get() == Value.kReverse);

        SmartDashboard.putNumber("arm", getArmAngleRad());
        SmartDashboard.putNumber("wrist", getWristAngleRad());
        SmartDashboard.putNumber("goal.arm", armPIDController.getGoal().position);
        SmartDashboard.putNumber("goal.wrist", wristPIDController.getGoal().position);

        if (enablePID) {
            double armState = armPIDController.calculate(getArmAngleRad());
            double wristState = wristPIDController.calculate(getWristAngleRad());

            SmartDashboard.putNumber("set.armPos", armState);
            SmartDashboard.putNumber("set.wristPos", wristState);

            var voltages =
                feedforward.calculate(VecBuilder.fill(getArmAngleRad(), getWristAngleRad()));

            SmartDashboard.putNumber("armFF", voltages.get(0, 0));
            SmartDashboard.putNumber("wristFF", voltages.get(0, 0));
            /*
             * if (Math.abs(getArmAngleRad() - armPIDController.getGoal().position) < 0.02) { double
             * armState2 = armPidController2.calculate(getArmAngleRad());
             * armMotor1.setVoltage(-armState2 - voltages.get(0, 0));
             * armMotor2.setVoltage(-armState2 - voltages.get(0, 0));
             * SmartDashboard.putNumber("PID In Use", 2); } else
             */ {
                armMotor1.setVoltage(-voltages.get(0, 0) - armState);
                armMotor2.setVoltage(-voltages.get(0, 0) - armState);
                SmartDashboard.putNumber("PID In Use", 1);
            }
            wristMotor.setVoltage(voltages.get(1, 0) + wristState);
        }
    }

    public double getArmAngle() {
        double angle = armEncoder.getPosition();
        if (angle > 260) {
            angle -= 360;
        }
        return angle;
    }

    public double getArmAngleRad() {
        return getArmAngle() * Math.PI / 180.0;
    }

    public double getWristAngle() {
        return wristEncoder.getPosition();
    }

    public double getWristAngleRad() {
        return getWristAngle() * Math.PI / 180.0;
    }

    public void setArmGoal(double goal) {
        enablePID = true;
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
