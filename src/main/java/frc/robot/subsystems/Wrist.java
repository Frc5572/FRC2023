package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import frc.robot.Constants;

/**
 * Creates new ProfiledPIDSubsystem and methods for the Wrist
 */
public class Wrist extends ProfiledPIDSubsystem {
    private final CANSparkMax wristMotor =
        new CANSparkMax(Constants.Motors.wristMotorID, MotorType.kBrushless);
    private final DutyCycleEncoder ourAbsoluteEncoder;

    private final SimpleMotorFeedforward wristFeed = new SimpleMotorFeedforward(
        Constants.WristPID.kSVolts, Constants.WristPID.kVVoltSecondsPerRotation);

    /**
     * Creates a new ProfilePIDController
     */
    public Wrist() {
        super(new ProfiledPIDController(Constants.WristPID.kP, Constants.WristPID.kI,
            Constants.WristPID.kD,
            new TrapezoidProfile.Constraints(Constants.WristPID.kMaxVelocityRadPerSecond,
                Constants.WristPID.kMaxAccelerationRadPerSecond),
            Constants.WristPID.kF));
        ourAbsoluteEncoder = new DutyCycleEncoder(Constants.Motors.wristCoderID);
        wristMotor.setInverted(true);
        getController().setTolerance(getMeasurement());
    }

    /**
     * Sets the wrist speed
     */
    public void setWrist(double num) {
        m_controller.setGoal(num);
        enable();
    }

    /**
     * Sets the wrist speed to zero
     */
    public void stopWrist() {
        wristMotor.set(0);
        disable();
    }


    /**
     * Uses the output from the ProfiledPIDController
     */
    @Override
    public void useOutput(double output, State setpoint) {
        wristMotor.setVoltage(output + wristFeed.calculate(setpoint.velocity));
    }

    /**
     * @return the rotation of the Wrist Encoder
     */
    @Override
    public double getMeasurement() {

        return ourAbsoluteEncoder.getAbsolutePosition();
    }

    /**
     * Going to use the next output from m_controller if m_enabled returns true
     */
    @Override
    public void periodic() {
        if (m_enabled) {
            useOutput(m_controller.calculate(getMeasurement(), m_controller.getGoal()),
                m_controller.getSetpoint());
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
        return m_controller.atGoal();
    }



}
