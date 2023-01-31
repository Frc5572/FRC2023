package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Arm extends SubsystemBase {
    public double armAngle;
    private double feedforward;

    private final CANSparkMax armMotor =
        new CANSparkMax(Constants.Motors.armID, MotorType.kBrushless);
    private final CANSparkMax armMotor2 =
        new CANSparkMax(Constants.Motors.armID2, MotorType.kBrushless);
    private final ArmFeedforward m_feedforward =
        new ArmFeedforward(Constants.ArmPID.kSVolts, Constants.ArmPID.kGVolts,
            Constants.ArmPID.kVVoltSecondPerRad, Constants.ArmPID.kAVoltSecondSquaredPerRad);
    private DutyCycleEncoder ourAbsoluteEncoder;
    private final ProfiledPIDController pid_controller =
        new ProfiledPIDController(Constants.ArmPID.kP, Constants.ArmPID.kI, Constants.ArmPID.kD,
            new TrapezoidProfile.Constraints(Constants.ArmPID.kMaxVelocityRadPerSecond,
                Constants.ArmPID.kMaxAccelerationRadPerSecSquared),
            Constants.ArmPID.kF);

    public Arm() {
        ourAbsoluteEncoder = new DutyCycleEncoder(Constants.ArmConstants.channelA);
    }

    /**
     * gets the angle measurement of the arm pivot
     */
    public double getAngleMeasurement() {// TODO Auto-generated method stub{
        armAngle = ourAbsoluteEncoder.getAbsolutePosition() * 360;
        return armAngle;
    }

    public void armAtHome(TrapezoidProfile.State trapState) {

        if (armAngle != Constants.ArmConstants.homePosition) {
            armMotor.setVoltage(feedforward);
        }

    }

    public void Arm2ndPosition() {
        if (!(Math.abs(getAngleMeasurement() - Constants.ArmConstants.secondPosition) < 5)) {
            double v = pid_controller.calculate(armAngle, Constants.ArmConstants.secondPosition);
            armMotor.set(v);
            armMotor2.set(v);
        }
    }



}
