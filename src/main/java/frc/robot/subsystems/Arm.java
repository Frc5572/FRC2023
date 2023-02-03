package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Creates the subsystem for the arm.
 */
public class Arm extends SubsystemBase {
    private final CANSparkMax armMotor =
        new CANSparkMax(Constants.ArmConstants.ARM_ID, MotorType.kBrushless);
    private final CANSparkMax armMotor2 =
        new CANSparkMax(Constants.ArmConstants.ARM_ID_2, MotorType.kBrushless);
    private final MotorControllerGroup armMotors = new MotorControllerGroup(armMotor, armMotor2);
    private final ArmFeedforward m_feedforward =
        new ArmFeedforward(Constants.ArmPID.K_SVOLTS, Constants.ArmPID.K_GVOLTS,
            Constants.ArmPID.K_WOLT_SECOND_PER_RAD, Constants.ArmPID.K_AVOLT_SECOND_SUARED_PER_RAD);
    private DutyCycleEncoder ourAbsoluteEncoder =
        new DutyCycleEncoder(Constants.ArmConstants.ENCODER_CHANNEL);
    private final ProfiledPIDController pid_controller =
        new ProfiledPIDController(Constants.ArmPID.KP, Constants.ArmPID.KI, Constants.ArmPID.KD,
            new TrapezoidProfile.Constraints(Constants.ArmPID.K_MAX_VELOCITY_RAD_PER_SECOND,
                Constants.ArmPID.K_MAX_ACCELERATION_RAD_PER_SEC_SQUARED),
            Constants.ArmPID.KF);
    private final double encOffset = Constants.ArmConstants.ENCODER_OFFSET;

    public Arm() {
        armMotor.setInverted(false);
        armMotor2.setInverted(true);
    }

    /**
     * Gets the angle measurement of the arm pivot.
     */
    public double getAngleMeasurement() {
        double armAngle = -(ourAbsoluteEncoder.getAbsolutePosition() - encOffset) * 360;
        return armAngle;
    }

    /**
     * Manually moves the arm at a certain speed. This will not stop automatically at an angle.
     */
    public void movingArmManually() {
        armMotors.set(0.1);
    }

    /**
     * Moves the arm to specified angle and stops when done.
     *
     * @param angle requested angle.
     */
    public void armToAngle(double angle) {
        if (!(Math.abs(getAngleMeasurement() - angle) < 5)) {
            double v = pid_controller.calculate(getAngleMeasurement(), angle);
            armMotors.set(v);
        }
    }

    /**
     * Stops the motors from moving.
     */
    public void motorsStop() {
        armMotors.set(0);
    }
}
