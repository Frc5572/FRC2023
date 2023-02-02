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
    private final CANSparkMax armMotor =
        new CANSparkMax(Constants.ArmConstants.ARM_ID, MotorType.kBrushless);
    private final CANSparkMax armMotor2 =
        new CANSparkMax(Constants.ArmConstants.ARM_ID_2, MotorType.kBrushless);
    private final ArmFeedforward m_feedforward =
        new ArmFeedforward(Constants.ArmPID.K_SVOLTS, Constants.ArmPID.K_GVOLTS,
            Constants.ArmPID.K_WOLT_SECOND_PER_RAD, Constants.ArmPID.K_AVOLT_SECOND_SUARED_PER_RAD); // not
                                                                                                     // used
                                                                                                     // yet
    private DutyCycleEncoder ourAbsoluteEncoder =
        new DutyCycleEncoder(Constants.ArmConstants.ENCODER_CHANNEL);
    private final ProfiledPIDController pid_controller =
        new ProfiledPIDController(Constants.ArmPID.KP, Constants.ArmPID.KI, Constants.ArmPID.KD,
            new TrapezoidProfile.Constraints(Constants.ArmPID.K_MAX_VELOCITY_RAD_PER_SECOND,
                Constants.ArmPID.K_MAX_ACCELERATION_RAD_PER_SEC_SQUARED),
            Constants.ArmPID.KF);

    public Arm() {}

    /**
     * gets the angle measurement of the arm pivot
     */
    public double getAngleMeasurement() {
        double armAngle = ourAbsoluteEncoder.getAbsolutePosition() * 360;
        return armAngle;
    }

    public void movingArmManually() {
        armMotor.set(0.1);
    }

    public void armToAngle(double angle) {
        if (!(Math.abs(getAngleMeasurement() - angle) < 5)) {
            double v = pid_controller.calculate(getAngleMeasurement(), angle);
            armMotor.set(v);
            armMotor2.set(v);
        }
    }

    public void motorsStop() {
        armMotor.set(0);
        armMotor2.set(0);
    }
}
