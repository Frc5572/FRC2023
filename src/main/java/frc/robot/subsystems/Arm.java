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
        new ArmFeedforward(Constants.ArmPID.K_SVOLTS, Constants.ArmPID.K_GVOLTS,
            Constants.ArmPID.K_WOLT_SECOND_PER_RAD, Constants.ArmPID.K_AVOLT_SECOND_SUARED_PER_RAD);
    private DutyCycleEncoder ourAbsoluteEncoder;
    private final ProfiledPIDController pid_controller =
        new ProfiledPIDController(Constants.ArmPID.KP, Constants.ArmPID.KI, Constants.ArmPID.KD,
            new TrapezoidProfile.Constraints(Constants.ArmPID.K_MAX_VELOCITY_RAD_PER_SECOND,
                Constants.ArmPID.K_MAX_ACCELERATION_RAD_PER_SEC_SQUARED),
            Constants.ArmPID.KF);

    public Arm() {
        ourAbsoluteEncoder = new DutyCycleEncoder(Constants.ArmConstants.CHANNEL_A);
    }

    /**
     * gets the angle measurement of the arm pivot
     */
    public double getAngleMeasurement() {// TODO Auto-generated method stub{
        armAngle = ourAbsoluteEncoder.getAbsolutePosition() * 360;
        return armAngle;
    }

    public void armAtHome(TrapezoidProfile.State trapState) {

        if (armAngle != Constants.ArmConstants.HOME_POSITION) {
            armMotor.setVoltage(feedforward);
        }

    }

    public void Arm2ndPosition() {
        if (!(Math.abs(getAngleMeasurement() - Constants.ArmConstants.SECOND_POSITION) < 5)) {
            double v = pid_controller.calculate(armAngle, Constants.ArmConstants.SECOND_POSITION);
            armMotor.set(v);
            armMotor2.set(v);
        }
    }



}
