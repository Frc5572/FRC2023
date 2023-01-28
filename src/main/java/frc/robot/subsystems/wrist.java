package frc.robot.subsystems;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import frc.robot.Constants;

/*
 * Creates new ProfiledPIDSubsystem and methods for the Wrist
 */
public class Wrist extends ProfiledPIDSubsystem {
    private final CANSparkMax wristMotor = new CANSparkMax(0, MotorType.kBrushless);
    private final CANCoder wristCANCoder = new CANCoder(0);
    private final SimpleMotorFeedforward wristFeed = new SimpleMotorFeedforward(
        Constants.wristPID.kSVolts, Constants.wristPID.kVVoltSecondsPerRotation);

    /*
     * Creates a new ProfilePIDController
     */
    public Wrist() {
        super(new ProfiledPIDController(Constants.wristPID.kP, Constants.wristPID.kI,
            Constants.wristPID.kD,
            new TrapezoidProfile.Constraints(Constants.wristPID.kMaxVelocityRadPerSecond,
                Constants.wristPID.kMaxAccelerationRadPerSecond),
            Constants.wristPID.kF));
        wristMotor.setInverted(true);
        getController().setTolerance(getMeasurement());
    }

    /*
     * Sets the wrist speed
     */
    public void setWrist(double num) {
        m_controller.setGoal(num);
        enable();
    }

    /*
     * Sets the wrist speed to zero
     */
    public void stopWrist() {
        m_controller.setGoal(0);
    }


    /*
     * Uses the output from the ProfiledPIDController
     */
    @Override
    public void useOutput(double output, State setpoint) {
        wristMotor.setVoltage(output + wristFeed.calculate(setpoint.velocity));
    }

    @Override
    public double getMeasurement() {

        return wristCANCoder.getPosition();
    }

    @Override
    public void periodic() {
        if (m_enabled) {
            useOutput(m_controller.calculate(getMeasurement(), m_controller.getGoal()),
                m_controller.getSetpoint());
        }
    }

    /*
     * Returns whether or not the wrist Encoder is align with the arm Encoder
     */
    public boolean getAlignment() {
        boolean alignment = (wristCANCoder.getPosition() - 0 == 0) ? true : false;
        return alignment;
    }



}
