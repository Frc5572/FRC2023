package frc.robot.subsystems;

import java.util.Map;
import org.ejml.data.DMatrix2;
import org.ejml.data.DMatrix2x2;
import org.ejml.dense.fixed.CommonOps_DDF2;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

/**
 * Creates the subsystem for the arm.
 */
public class Arm extends SubsystemBase {
    private final CANSparkMax armMotor1 =
        new CANSparkMax(Constants.Arm.ARM_ID, MotorType.kBrushless);
    private final CANSparkMax armMotor2 =
        new CANSparkMax(Constants.Arm.ARM_ID_2, MotorType.kBrushless);
    private final AbsoluteEncoder armEncoder = armMotor1.getAbsoluteEncoder(Type.kDutyCycle);
    private final double armEncoderOffset = 0.0;
    private final double armEncoderMultiplier = 1.0;

    // WRIST
    public final CANSparkMax wristMotor =
        new CANSparkMax(Constants.Wrist.WRIST_MOTOR_ID, MotorType.kBrushless);
    private final AbsoluteEncoder wristEncoder = wristMotor.getAbsoluteEncoder(Type.kDutyCycle);
    private final double wristEncoderOffset = 72.6637727;
    private final double wristEncoderMultiplier = 1.0;

    private boolean enablePID = false;

    private GenericEntry armAngleWidget = RobotContainer.mainDriverTab
        .add("Arm Angle", getAngleMeasurement()).withWidget(BuiltInWidgets.kDial)
        .withProperties(Map.of("min", 0, "max", 150)).withPosition(8, 1).withSize(2, 2).getEntry();
    // private GenericEntry armExtensionWidget = RobotContainer.mainDriverTab
    // .add("Arm Extension", getElevatorPosition()).withWidget(BuiltInWidgets.kDial)
    // .withProperties(Map.of("min", 0, "max", Constants.Elevator.MAX_ENCODER)).withPosition(10, 0)
    // .withSize(2, 2).getEntry();


    // MATH
    private DMatrix2x2 B, K_b, M, C, tmp3;
    private DMatrix2 tau_g, tmp1, tmp2, u;

    private DMatrix2 theta, theta_dot, theta_ddot;

    private LinearFilter armLowPassFilter = LinearFilter.singlePoleIIR(20, 0.02); // TODO tune
                                                                                  // timeConstant
    private LinearFilter armDeriveFilter = LinearFilter.backwardFiniteDifference(1, 2, 0.02);
    private LinearFilter arm2ndDeriveFilter = LinearFilter.backwardFiniteDifference(2, 3, 0.02);

    private LinearFilter wristLowPassFilter = LinearFilter.singlePoleIIR(20, 0.02); // TODO tune
                                                                                    // timeConstant
    private LinearFilter wristDeriveFilter = LinearFilter.backwardFiniteDifference(1, 2, 0.02);
    private LinearFilter wrist2ndDeriveFilter = LinearFilter.backwardFiniteDifference(2, 3, 0.02);

    /**
     * Arm Subsystem
     */
    public Arm() {
        // ARM
        armMotor1.restoreFactoryDefaults();
        armMotor2.restoreFactoryDefaults();
        armMotor1.setIdleMode(IdleMode.kBrake);
        armMotor2.setIdleMode(IdleMode.kBrake);
        armMotor1.setInverted(false);
        armMotor2.setInverted(true);

        armMotor1.burnFlash();
        armMotor2.burnFlash();

        // WRIST
        wristMotor.restoreFactoryDefaults();
        wristMotor.setIdleMode(IdleMode.kBrake);
        wristMotor.setInverted(false);
        wristEncoder.setPositionConversionFactor(360);
        wristEncoder.setVelocityConversionFactor(360);
        wristEncoder.setInverted(false);
        wristEncoder.setZeroOffset(wristEncoderOffset);
        wristMotor.burnFlash();


        // MATH
        double K_t = Constants.Arm.STALL_TORQUE / Constants.Arm.STALL_CURRENT;
        double K_v = Constants.Arm.FREE_SPEED / 12.0; // Does this need to change if battery level
                                                      // is different?
        double R = 12.0 / Constants.Arm.STALL_CURRENT;
        this.B = new DMatrix2x2(Constants.Arm.GEARING * 2.0 * K_t / R, 0, 0,
            Constants.Wrist.GEARING * K_t / R);
        this.K_b =
            new DMatrix2x2(Constants.Arm.GEARING * Constants.Arm.GEARING * 2.0 * K_t / K_v / R, 0,
                0, Constants.Wrist.GEARING * Constants.Wrist.GEARING * K_t / K_v / R);
        this.M = new DMatrix2x2();
        this.M.zero();
        this.C = new DMatrix2x2();
        this.C.zero();
        this.tau_g = new DMatrix2(0.0, 0.0);
        this.theta = new DMatrix2();
        this.tmp1 = new DMatrix2();
        this.tmp2 = new DMatrix2();
        this.tmp3 = new DMatrix2x2();
        this.u = new DMatrix2();
        this.theta_dot = new DMatrix2();
        this.theta_ddot = new DMatrix2();
    }

    @Override
    public void periodic() {
        armAngleWidget.setDouble(getAngleMeasurement());
        // armExtensionWidget.setDouble(getElevatorPosition());

        theta.a1 =
            armLowPassFilter.calculate(Rotation2d.fromDegrees(getAngleMeasurement()).getRadians());
        theta.a2 = wristLowPassFilter
            .calculate(Rotation2d.fromDegrees(getWristAngleMeasurment()).getRadians());

        theta_dot.a1 = armDeriveFilter.calculate(theta.a1);
        theta_dot.a2 = wristDeriveFilter.calculate(theta.a2);

        theta_ddot.a1 = arm2ndDeriveFilter.calculate(theta.a1);
        theta_ddot.a2 = wrist2ndDeriveFilter.calculate(theta.a2);

        // Calculate angle derivatives
        if (enablePID) {

            // M(theta)

            M.a11 = Constants.Arm.WEIGHT * Constants.Arm.COM * Constants.Arm.COM
                + Constants.Wrist.WEIGHT * (Constants.Arm.LENGTH * Constants.Arm.LENGTH
                    + Constants.Wrist.COM * Constants.Wrist.COM)
                + Constants.Arm.INERTIA + Constants.Wrist.INERTIA + 2.0 * Constants.Wrist.WEIGHT
                    * Constants.Arm.LENGTH * Constants.Wrist.COM * Math.cos(theta.a2);

            M.a12 = Constants.Wrist.WEIGHT * Constants.Wrist.COM * Constants.Wrist.COM
                + Constants.Wrist.INERTIA + Constants.Wrist.WEIGHT * Constants.Arm.LENGTH
                    * Constants.Wrist.COM * Math.cos(theta.a2);

            M.a21 = Constants.Wrist.WEIGHT * Constants.Wrist.COM * Constants.Wrist.COM
                + Constants.Wrist.INERTIA + Constants.Wrist.WEIGHT * Constants.Arm.LENGTH
                    * Constants.Wrist.COM * Math.cos(theta.a2);

            M.a22 = Constants.Wrist.WEIGHT * Constants.Wrist.COM * Constants.Wrist.COM
                + Constants.Wrist.INERTIA;

            // C(theta_dot, theta)

            C.a11 = -Constants.Wrist.WEIGHT * Constants.Arm.LENGTH * Constants.Wrist.COM
                * Math.sin(theta.a2) * theta_dot.a2;

            C.a12 = -Constants.Wrist.WEIGHT * Constants.Arm.LENGTH * Constants.Wrist.COM
                * Math.sin(theta.a2) * (theta_dot.a1 + theta_dot.a2);

            C.a21 = Constants.Wrist.WEIGHT * Constants.Arm.LENGTH * Constants.Wrist.COM
                * Math.sin(theta.a2) * theta_dot.a2;

            C.a22 = 0.0;

            // tau_g(theta)

            tau_g.a1 = 9.81 * Math.cos(theta.a1)
                * (Constants.Arm.WEIGHT * Constants.Arm.COM
                    + Constants.Wrist.WEIGHT * Constants.Arm.LENGTH)
                + Constants.Wrist.WEIGHT * Constants.Wrist.COM * 9.81
                    * Math.cos(theta.a1 + theta.a2);

            tau_g.a2 =
                Constants.Wrist.WEIGHT * Constants.Wrist.COM * 9.81 * Math.cos(theta.a1 + theta.a2);

            // u

            CommonOps_DDF2.mult(K_b, theta_dot, tmp1); // K_b * theta_dot
            CommonOps_DDF2.add(tau_g, tmp1, tmp2); // tau_g + K_b * theta_dot
            CommonOps_DDF2.mult(C, theta_dot, tmp1); // C * theta_dot
            CommonOps_DDF2.addEquals(tmp2, tmp1); // tau_g + K_b * theta_dot + C * theta_dot
            CommonOps_DDF2.mult(M, theta_ddot, tmp1); // M * theta_ddot
            CommonOps_DDF2.addEquals(tmp2, tmp1); // tau_g + K_b * theta_dot + C * theta_dot + M *
                                                  // theta_ddot
            CommonOps_DDF2.invert(B, tmp3); // B^-1
            CommonOps_DDF2.mult(tmp3, tmp2, u); // B^-1 * [tau_g + K_b * theta_dot + C * theta_dot +
                                                // M * theta_ddot]

            double armFeedforward = u.a1;
            double wristFeedforward = u.a2;

            // TODO stuff with the feedforward!
        }
        SmartDashboard.putNumber("Arm Encoder", getAngleMeasurement());
        SmartDashboard.putNumber("Wrist Encoder", getWristAngleMeasurment());

    }

    /**
     * Set target angle for Arm
     *
     * @param goal Target Angel in Degrees
     */
    public void setArmGoal(double goal) {
        SmartDashboard.putNumber("goal", goal);
    }

    private static double wrapDegrees(double inDegrees) {
        if (inDegrees > 180) {
            inDegrees -= 360;
        }
        return inDegrees;
    }

    /**
     * Enable PID control
     */
    public void enablePID() {
        this.enablePID = true;
    }

    /**
     * Disable PID control
     *
     */
    public void disablePID() {
        this.enablePID = false;
    }

    /**
     * Get the current angle that is stated by the encoder.
     *
     * @return Current angle reported by encoder (0 - 360)
     */
    public double getAngleMeasurement() {
        return armEncoder.getPosition();
    }

    // ---------------- WRIST ----------------------------

    /**
     * @return the rotation of the Wrist Encoder
     */
    public double getWristAngleMeasurment() {
        return wristEncoder.getPosition();
    }
}
