package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
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
        new CANSparkMax(Constants.Arm.ARM_ID, MotorType.kBrushless);
    private final CANSparkMax armMotor2 =
        new CANSparkMax(Constants.Arm.ARM_ID_2, MotorType.kBrushless);
    private final MotorControllerGroup armMotors = new MotorControllerGroup(armMotor, armMotor2);
    private final ArmFeedforward m_feedforward = new ArmFeedforward(Constants.Arm.PID.K_SVOLTS,
        Constants.Arm.PID.K_GVOLTS, Constants.Arm.PID.K_WVOLT_SECOND_PER_RAD,
        Constants.Arm.PID.K_AVOLT_SECOND_SQUARED_PER_RAD);
    private DutyCycleEncoder encoder1 = new DutyCycleEncoder(Constants.Arm.ENCODER_CHANNEL1);
    private DutyCycleEncoder encoder2 = new DutyCycleEncoder(Constants.Arm.ENCODER_CHANNEL2);
    private final ProfiledPIDController pid_controller =
        new ProfiledPIDController(Constants.Arm.PID.KP, Constants.Arm.PID.KI, Constants.Arm.PID.KD,
            new TrapezoidProfile.Constraints(Constants.Arm.PID.K_MAX_VELOCITY_RAD_PER_SECOND,
                Constants.Arm.PID.K_MAX_ACCELERATION_RAD_PER_SEC_SQUARED),
            Constants.Arm.PID.KF);
    private final double encOffset = Constants.Arm.ENCODER_OFFSET;
    private double goalAngle;
    private boolean enablePID = false;

    public Arm() {
        armMotor.setIdleMode(IdleMode.kBrake);
        armMotor2.setIdleMode(IdleMode.kBrake);
        armMotor.setInverted(false);
        armMotor2.setInverted(true);
    }

    @Override
    public void periodic() {
        // if (enablePID) {
        // ddToAngle(goalAngle);
        // }
    }

    /**
     * Set target angle for Arm
     *
     * @param goal Target Angel in Degrees
     */
    public void setGoal(double goal) {
        this.goalAngle = goal;
    }

    /**
     * Get the target angle for the Arm
     *
     * @return The target angle in degrees
     */
    public double getGoal() {
        return this.goalAngle;
    }

    /**
     * Moves the arm to specified angle and stops when done.
     *
     * @param angle requested angle in degrees
     */
    public void armToAngle(double angle) {
        if (!(Math.abs(getAngleMeasurement() - angle) < 5)) {
            armMotors.set(pid_controller.calculate(Math.toRadians(getAngleMeasurement()), angle)
                + m_feedforward.calculate(Math.toRadians(getAngleMeasurement()), 0));
        }
    }

    /**
     * Check if aligned with a requested goal.
     *
     * @param goal The requesed goal.
     * @return True if properly aligned, false if not.
     */
    public boolean checkIfAligned(double goal) {
        return Math.abs(getAngleMeasurement() - goal) < 5;
    }

    /**
     * Gets the angle measurement of the arm pivot in degrees.
     */
    public double getAngleMeasurement() {
        double armAngle = -(encoder1.getAbsolutePosition() - encOffset) * 360;
        return armAngle;
    }

    /**
     * Manually moves the arm at a certain speed. This will not stop automatically at an angle.
     */
    public void movingArmManually() {
        armMotors.set(0.1);
    }

    /**
     * Stops the motors from moving.
     */
    public void motorsStop() {
        armMotors.set(0);
    }
}
