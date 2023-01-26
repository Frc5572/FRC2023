package frc.robot.subsystems;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import frc.robot.Constants;

public class Arm extends ProfiledPIDSubsystem {
    private final CANSparkMax armMotor =
        new CANSparkMax(Constants.Motors.armID, MotorType.kBrushless);
    private final CANCoder armEncoder = new CANCoder(0);
    private final ArmFeedforward m_feedforward =
        new ArmFeedforward(Constants.ArmPID.kSVolts, Constants.ArmPID.kGVolts,
            Constants.ArmPID.kVVoltSecondPerRad, Constants.ArmPID.kAVoltSecondSquaredPerRad);


    public Arm() {
        super(
            new ProfiledPIDController(Constants.ArmPID.kP, Constants.ArmPID.kI, Constants.ArmPID.kD,
                new TrapezoidProfile.Constraints(Constants.ArmPID.kMaxVelocityRadPerSecond,
                    Constants.ArmPID.kMaxAccelerationRadPerSecSquared),
                Constants.ArmPID.kF));
    }

    public void useOutput(double output, TrapezoidProfile.State trapState){
        double feedforward = m_feedforward.calculate(trapState.position, trapState.velocity)
        armMotor.setVoltage(output+feedforward);
    }

    @Override
    // this class is supposed to return a double
    public double getMeasurement() { //

        return 0;
    }

    public void getAngleMeasurement() {// TODO Auto-generated method stub{

    }



}
