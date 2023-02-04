package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Creates new Subsystem and methods for the Wrist
 */
public class Wrist extends SubsystemBase {
    private final CANSparkMax wristMotor =
        new CANSparkMax(Constants.Motors.wristMotorID, MotorType.kBrushless);
    private final DutyCycleEncoder ourAbsoluteEncoder;

    private final SimpleMotorFeedforward wristFeed = new SimpleMotorFeedforward(
        Constants.WristPID.kSVolts, Constants.WristPID.kVVoltSecondsPerRotation);
    private final Solenoid wristSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 0);
    private final ProfiledPIDController pidController;
    private boolean m_enabled;

    /**
     * Creates a new ProfilePIDController
     */
    public Wrist() {
        pidController = new ProfiledPIDController(Constants.WristPID.kP, Constants.WristPID.kI,
            Constants.WristPID.kD,
            new TrapezoidProfile.Constraints(Constants.WristPID.kMaxVelocityRadPerSecond,
                Constants.WristPID.kMaxAccelerationRadPerSecond),
            Constants.WristPID.kF);
        ourAbsoluteEncoder = new DutyCycleEncoder(Constants.Motors.wristCoderID);
        wristMotor.setInverted(true);
        pidController.setTolerance(getMeasurement());
    }

    /**
     * Sets the wrist speed
     */
    public void setWrist(double num) {
        pidController.setGoal(num);
        enable();
        wristSolenoid.set(m_enabled);
    }

    /**
     * Sets the wrist speed to zero
     */
    public void stopWrist() {
        wristMotor.set(0);
        disable();
        wristSolenoid.set(true);
    }

    /**
     * Opens the wrist
     */
    public void openWrist(double num) {
        pidController.setGoal(num);
        enable();
        wristSolenoid.set(false);
    }

    /**
     * Uses the output from the ProfiledPIDController
     */
    public void useOutput(double output, State setpoint) {
        wristMotor.setVoltage(output + wristFeed.calculate(setpoint.velocity));
    }

    /**
     * @return the rotation of the Wrist Encoder
     */
    public double getMeasurement() {

        return ourAbsoluteEncoder.getAbsolutePosition();
    }

    /**
     * Going to use the next output from m_controller if m_enabled returns true
     */
    @Override
    public void periodic() {
        if (m_enabled) {
            useOutput(pidController.calculate(getMeasurement(), pidController.getGoal()),
                pidController.getSetpoint());
        }
    }

    /**
     * @return whether or not the wrist Encoder is align with the arm Encoder (will later replace
     *         with the cancoder of the arm)
     */
    public boolean getAlignment() {
        boolean alignment = (getMeasurement() - 0 == 0) ? true : false;
        return alignment;
    }

    /**
     * @return whether the wrist has reached its certain goal or not
     */
    public boolean atGoal() {
        return pidController.atGoal();
    }

    /** Enables the PID control. Resets the controller. */
    public void enable() {
        m_enabled = true;
        pidController.reset(getMeasurement());
    }

    /** Disables the PID control. Sets output to zero. */
    public void disable() {
        m_enabled = false;
        useOutput(0, new State());
    }


}
