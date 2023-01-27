package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import frc.robot.Constants;

public class Arm extends ProfiledPIDSubsystem {
    public double armAngle;
    private double feedforward;

    private final CANSparkMax armMotor =
        new CANSparkMax(Constants.Motors.armID, MotorType.kBrushless);
    private final ArmFeedforward m_feedforward =
        new ArmFeedforward(Constants.ArmPID.kSVolts, Constants.ArmPID.kGVolts,
            Constants.ArmPID.kVVoltSecondPerRad, Constants.ArmPID.kAVoltSecondSquaredPerRad);
    private DutyCycleEncoder ourAbsoluteEncoder;

    public Arm() {
        super(
            new ProfiledPIDController(Constants.ArmPID.kP, Constants.ArmPID.kI, Constants.ArmPID.kD,
                new TrapezoidProfile.Constraints(Constants.ArmPID.kMaxVelocityRadPerSecond,
                    Constants.ArmPID.kMaxAccelerationRadPerSecSquared),
                Constants.ArmPID.kF));
        ourAbsoluteEncoder = new DutyCycleEncoder(Constants.ArmConstants.channelA);
    }

    public void useOutput(double output, TrapezoidProfile.State trapState) {
        feedforward = m_feedforward.calculate(trapState.position, trapState.velocity);
        armMotor.setVoltage(output + feedforward);
    }

    @Override
    // this class is supposed to return a double
    public double getMeasurement() { //

        return 0;
    }

    /**
     * gets the angle measurement of the arm pivot
     */
    public void getAngleMeasurement() {// TODO Auto-generated method stub{
        armAngle = ourAbsoluteEncoder.getAbsolutePosition();
    }

    public void ArmAtHome(double output, TrapezoidProfile.State trapState) {

        if (armAngle != 0) {
            armMotor.setVoltage(output + feedforward);
        }

    }



}
