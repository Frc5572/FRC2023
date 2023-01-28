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


public class Wrist extends ProfiledPIDSubsystem {
    private final CANSparkMax wristMotor = new CANSparkMax(0, MotorType.kBrushless);
    private final CANCoder wristCANCoder = new CANCoder(0);
    private final SimpleMotorFeedforward wristFeed = new SimpleMotorFeedforward(
        Constants.wristPID.kSVolts, Constants.wristPID.kVVoltSecondsPerRotation);

    public Wrist() {
        super(new ProfiledPIDController(Constants.wristPID.kP, Constants.wristPID.kI,
            Constants.wristPID.kD,
            new TrapezoidProfile.Constraints(Constants.wristPID.kMaxVelocityRadPerSecond,
                Constants.wristPID.kMaxAccelerationRadPerSecond),
            Constants.wristPID.kF));
        wristMotor.setInverted(true);
        getController().setTolerance(getMeasurement());
    }

    public void setWristUp(double num) {
        enable();
        wristMotor.set(num);
    }

    public void setWristDown(double num) {
        enable();
        wristMotor.set(num);
    }

    public void stopWrist() {
        wristMotor.set(0);
    }


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


            useOutput(m_controller.calculate(getMeasurement(), null), null);
        }
    }



}
