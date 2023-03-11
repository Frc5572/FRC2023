package frc.robot.subsystems;

import java.util.Map;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.math.ArmFeedForwardTunable;
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
    // private final MotorControllerGroup armMotors = new MotorControllerGroup(armMotor1,
    // armMotor2);
    private ArmFeedforward m_feedforward;
    private final AbsoluteEncoder encoder1 = armMotor1.getAbsoluteEncoder(Type.kDutyCycle);
    private final AbsoluteEncoder encoder2 = armMotor2.getAbsoluteEncoder(Type.kDutyCycle);
    // private final ProfiledPIDController pid_controller =
    // new ProfiledPIDController(Constants.Arm.PID.KP, Constants.Arm.PID.KI, Constants.Arm.PID.KD,
    // new TrapezoidProfile.Constraints(Constants.Arm.PID.K_MAX_VELOCITY_RAD_PER_SECOND,
    // Constants.Arm.PID.K_MAX_ACCELERATION_RAD_PER_SEC_SQUARED));
    private final PIDController armPIDController1 =
        new PIDController(Constants.Arm.PID.KP, 0.0, Constants.Arm.PID.KD);
    private final PIDController armPIDController2 =
        new PIDController(Constants.Arm.PID.KP2, Constants.Arm.PID.KI, Constants.Arm.PID.KD);

    private boolean enablePID = false;
    private final DoubleSolenoid armSolenoid;


    // WRIST
    public final CANSparkMax wristMotor =
        new CANSparkMax(Constants.Wrist.WRIST_MOTOR_ID, MotorType.kBrushless);
    private final ArmFeedForwardTunable wristFeedforward = new ArmFeedForwardTunable(
        Constants.Wrist.PID.kS, Constants.Wrist.PID.kG, Constants.Wrist.PID.kV);
    private final AbsoluteEncoder wristEncoder = wristMotor.getAbsoluteEncoder(Type.kDutyCycle);
    private final PIDController wristPIDController =
        new PIDController(Constants.Wrist.PID.kP, Constants.Wrist.PID.kI, Constants.Wrist.PID.kD);
    private final double wristEncoderOffset = 331.8928313;
    private double wristLastAngle = 0;
    private double wristOffset = 0;

    private GenericEntry armAngleWidget = RobotContainer.mainDriverTab
        .add("Arm Angle", getAverageArmAngle()).withWidget(BuiltInWidgets.kDial)
        .withProperties(Map.of("min", 0, "max", 150)).withPosition(8, 1).withSize(2, 2).getEntry();
    private GenericEntry solenoidStatus = RobotContainer.mainDriverTab.add("Grabber Open", false)
        .withWidget(BuiltInWidgets.kBooleanBox)
        .withProperties(Map.of("Color when true", "green", "Color when false", "red"))
        .withPosition(6, 4).withSize(2, 1).getEntry();

    private boolean goingDown = false;

    /**
     * Arm Subsystem
     */
    public Arm(PneumaticHub ph) {
        // ARM
        armPIDController1.setTolerance(2);
        armPIDController2.setTolerance(2);
        armPIDController1.enableContinuousInput(0, 360);
        armPIDController2.enableContinuousInput(0, 360);
        armPIDController1.setIntegratorRange(-.2, 0);
        armMotor1.restoreFactoryDefaults();
        armMotor2.restoreFactoryDefaults();
        armMotor1.setIdleMode(IdleMode.kBrake);
        armMotor2.setIdleMode(IdleMode.kBrake);
        armMotor1.setInverted(false);
        armMotor2.setInverted(true);
        encoder1.setPositionConversionFactor(360);
        encoder1.setVelocityConversionFactor(360);
        encoder1.setInverted(true);
        encoder1.setZeroOffset(Constants.Arm.encoder1Offset);
        m_feedforward = new ArmFeedforward(Constants.Arm.PID.K_SVOLTS,
            Constants.Arm.PID.K_GVOLTS_MIN, Constants.Arm.PID.K_WVOLT_SECOND_PER_RAD,
            Constants.Arm.PID.K_AVOLT_SECOND_SQUARED_PER_RAD);
        // setArmGoal()

        armMotor1.burnFlash();
        armMotor2.burnFlash();
        // WRIST
        wristMotor.restoreFactoryDefaults();
        wristMotor.setIdleMode(IdleMode.kBrake);
        wristMotor.setInverted(false);
        wristEncoder.setPositionConversionFactor(360);
        wristEncoder.setVelocityConversionFactor(360);
        wristEncoder.setInverted(false);
        wristEncoder.setZeroOffset(wristEncoderOffset);
        wristMotor.burnFlash();
        wristPIDController.setTolerance(1);
        wristPIDController.setSetpoint(90);
        wristPIDController.enableContinuousInput(0, 360);
        wristPIDController.setIntegratorRange(-0.2, 0.2);

        this.armSolenoid = ph.makeDoubleSolenoid(Constants.Arm.SOLENOID_FORWARD_CHANNEL,
            Constants.Arm.SOLENOID_REVERSE_CHANNEL);

        SmartDashboard.putData("wrist", wristPIDController);
        SmartDashboard.putData("arm", armPIDController1);

        SmartDashboard.putNumber("Wrist kG", Constants.Wrist.PID.kG);

    }

    @Override
    public void periodic() {
        armAngleWidget.setDouble(getAverageArmAngle());
        if (enablePID) {
            armToAngle();
            wristToPosition();
        }
        SmartDashboard.putNumber("Arm Encoder 1", getAngleMeasurement1());
        SmartDashboard.putNumber("Arm Encoder 2", getAngleMeasurement2());
        SmartDashboard.putNumber("Wrist Encoder", getWristAngleMeasurment());
        solenoidStatus.setBoolean(this.armSolenoid.get() == Value.kReverse);
        Constants.Wrist.PID.kG = SmartDashboard.getNumber("Wrist kG", Constants.Wrist.PID.kG);

        wristFeedforward.kg = Constants.Wrist.PID.kG;
    }

    /**
     * Set target angle for Arm
     *
     * @param goal Target Angel in Degrees
     */
    public void setArmGoal(double goal) {
        goingDown = wrapDegrees(getAngleMeasurement1()) > goal;
        // goal = !goingDown ? goal - 10 : goal;
        armPIDController1.setSetpoint(goal);
        armPIDController1.reset();
        // armPIDController2.setSetpoint(goal);
        // armPIDController2.reset();
        SmartDashboard.putNumber("goal", goal);
    }

    private static double wrapDegrees(double inDegrees) {
        if (inDegrees > 180) {
            inDegrees -= 360;
        }
        return inDegrees;
    }

    /**
     * Moves the arm to specified angle and stops when done.
     *
     */
    public void armToAngle() {
        m_feedforward = new ArmFeedforward(Constants.Arm.PID.K_SVOLTS,
            Constants.Arm.PID.K_GVOLTS_MIN, Constants.Arm.PID.K_WVOLT_SECOND_PER_RAD,
            Constants.Arm.PID.K_AVOLT_SECOND_SQUARED_PER_RAD);
        double armPID1 = 0;
        double armPID2 = 0;
        armPID1 = armPIDController1.calculate(getAngleMeasurement1());
        armPID2 = armPIDController1.calculate(getAngleMeasurement1());
        // if (!goingDown) {
        // if (armPIDController1.getSetpoint() - wrapDegrees(getAngleMeasurement1()) < 10) {
        // armPIDController2.setSetpoint(armPIDController1.getSetpoint() + 10);
        // goingDown = true;
        // }
        // armPID1 = armPIDController1.calculate(getAngleMeasurement1());
        // armPID2 = armPIDController1.calculate(getAngleMeasurement1());
        // } else {
        // armPID1 = armPIDController2.calculate(getAngleMeasurement1());
        // armPID2 = armPIDController2.calculate(getAngleMeasurement1());
        // }
        armMotor1.setVoltage(
            armPID1 + m_feedforward.calculate(Math.toRadians(getAngleMeasurement1() - 90), 0));
        armMotor2.setVoltage(
            armPID2 + m_feedforward.calculate(Math.toRadians(getAngleMeasurement1() - 90), 0));

        // if (armPIDController2.atSetpoint()) {
        // armPIDController2.reset();
        // }
        SmartDashboard.putNumber("Arm Voltage 1", armPID1);
        SmartDashboard.putNumber("Arm Voltage 2", armPID2);
    }

    /**
     * Enable PID control
     */
    public void enablePID() {
        this.enablePID = true;
    }

    /**
     * Disable PID control
     *
     */
    public void disablePID() {
        this.enablePID = false;
    }

    /**
     * Get the current angle that is stated by the encoder.
     *
     * @return Current angle reported by encoder (0 - 360)
     */
    public double getAngleMeasurement1() {
        return encoder1.getPosition();
    }


    /**
     * Get the current angle that is stated by the encoder.
     *
     * @return Current angle reported by encoder (0 - 360)
     */
    public double getAngleMeasurement2() {
        return encoder2.getPosition();
    }

    /**
     * Calculate the average angle of arm from both encoders
     *
     * @return Average arm angle
     */
    public double getAverageArmAngle() {
        return getAngleMeasurement1();
    }

    /**
     * Check if aligned with a requested goal.
     *
     * @return True if properly aligned, false if not.
     */
    public boolean checkIfAligned1() {
        return armPIDController1.atSetpoint();
    }

    /**
     * Check if aligned with a requested goal.
     *
     * @return True if properly aligned, false if not.
     */
    public boolean checkIfAligned2() {
        return armPIDController2.atSetpoint();
    }

    public boolean checkArmInPosition() {
        return checkIfAligned1();
    }

    // ---------------- WRIST ----------------------------

    /**
     * Set target position for wrist
     *
     * @param goal Set target angle for wrist
     */
    public void setWristGoal(double goal) {
        wristPIDController.setSetpoint(goal);
        wristPIDController.reset();
        SmartDashboard.putNumber("Wrist Goal", goal);
    }

    /**
     * Set target offset from 0 for wrist
     *
     * @param offset The offset from 0 degrees
     */
    public void setWristOffset(double offset) {
        wristOffset = offset;
    }

    /**
     * Test function
     */
    public void wristToPosition() {
        // setWristGoal(getAverageArmAngle() + wristOffset);
        // double angle = getWristAngleMeasurment();
        double wristPID = wristPIDController.calculate(getWristAngleMeasurment());
        double ff = wristFeedforward.calculate(0, 0);
        // if (Math.abs(angle - wristLastAngle) < 2 || wristLastAngle == 0) {
        wristMotor.setVoltage(wristPID// + ff
        );
        // }
        SmartDashboard.putNumber("Wrist Voltage", wristPID);
        // SmartDashboard.putNumber("PID Voltage", pidVol);
        // SmartDashboard.putNumber("FF Voltage", ff);
        SmartDashboard.putNumber("Wrist Encoder", getWristAngleMeasurment());
        SmartDashboard.putNumber("Wrist Offset", wristOffset);
        SmartDashboard.putNumber("Wrist Goal", wristPIDController.getSetpoint());
        // wristLastAngle = angle;
    }

    /**
     * @return the rotation of the Wrist Encoder
     */
    public double getWristAngleMeasurment() {
        return wristEncoder.getPosition();
    }

    public boolean getWristAligned() {
        return wristPIDController.atSetpoint();
    }

    public void extendArm() {
        armSolenoid.set(Value.kReverse);
    }

    public void retractArm() {
        armSolenoid.set(Value.kForward);
    }

    /**
     * Choses what command to run based off position.
     *
     * @param extend Wheter arm is extended or not
     */
    public void toExtendOrNotToExtend(boolean extend) {
        if (extend) {
            extendArm();
        } else {
            retractArm();
        }
    }
}
