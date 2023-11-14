package frc.robot.subsystems.arm;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.math.DoubleJointedArmFeedforward;
import frc.robot.Constants;

/**
 * Creates the subsystem for the arm.
 */
public class Arm extends SubsystemBase {

    ProfiledPIDController armPIDController =
        new ProfiledPIDController(Constants.Arm.PID.kP, Constants.Arm.PID.kI, Constants.Arm.PID.kD,
            new TrapezoidProfile.Constraints(Constants.Arm.PID.MAX_VELOCITY,
                Constants.Arm.PID.MAX_ACCELERATION));

    ProfiledPIDController wristPIDController = new ProfiledPIDController(Constants.Wrist.PID.kP,
        Constants.Wrist.PID.kI, Constants.Wrist.PID.kD, new TrapezoidProfile.Constraints(
            Constants.Wrist.PID.MAX_VELOCITY, Constants.Wrist.PID.MAX_ACCELERATION));

    private DoubleJointedArmFeedforward feedforward =
        new DoubleJointedArmFeedforward(Constants.Arm.config, Constants.Wrist.config);

    private boolean enablePID = false;
    private boolean reset0 = true;

    private ArmIO io;
    private ArmInputsAutoLogged armInputs = new ArmInputsAutoLogged();

    /**
     * Arm Subsystem
     */
    public Arm(ArmIO armIO) {
        io = armIO;
        io.updateInputs(armInputs);


        this.wristPIDController.setIntegratorRange(Constants.Wrist.PID.MIN_INTEGRAL,
            Constants.Wrist.PID.MAX_INTEGRAL);
        wristPIDController.setTolerance(Math.toRadians(2));
        armPIDController.setTolerance(Math.toRadians(2));

        // armPIDController.enableContinuousInput(0, 2 * Math.PI);
        // armPidController2.enableContinuousInput(0, 2 * Math.PI);
        // wristPIDController.enableContinuousInput(0, 2 * Math.PI);

        enablePID = false;

    }

    /**
     *
     */
    public void adjust(double amount) {
        var goal = armPIDController.getGoal();
        goal.position += amount;
        goal.position = MathUtil.clamp(goal.position, Math.toRadians(-45), Math.toRadians(30));
        armPIDController.setGoal(goal);
    }

    @Override
    public void periodic() {

        io.updateInputs(armInputs);
        Logger.getInstance().processInputs("Arm", armInputs);

        SmartDashboard.putNumber("arm", getArmAngle());
        SmartDashboard.putNumber("wrist", getWristAngle());
        SmartDashboard.putNumber("goal.arm", armPIDController.getGoal().position * 180 / Math.PI);
        SmartDashboard.putNumber("goal.wrist",
            wristPIDController.getGoal().position * 180 / Math.PI);
        if (reset0) {
            armPIDController.reset(getArmAngleRad());
            reset0 = false;
        }
        if (enablePID) {
            double armState = armPIDController.calculate(getArmAngleRad());
            double wristState = wristPIDController.calculate(getWristAngleRad());

            SmartDashboard.putNumber("set.armPos", armState);
            SmartDashboard.putNumber("set.wristPos", wristState);
            SmartDashboard.putBoolean("arm.atgoal", armPIDController.atGoal());
            SmartDashboard.putBoolean("wrist.atgoal", wristPIDController.atGoal());

            var voltages =
                feedforward.calculate(VecBuilder.fill(getArmAngleRad(), getWristAngleRad()));

            SmartDashboard.putNumber("armFF", voltages.get(0, 0));
            SmartDashboard.putNumber("wristFF", voltages.get(1, 0));

            SmartDashboard.putNumber("armPID", armState);
            SmartDashboard.putNumber("wristPID", wristState);

            io.setArmVoltage(armInputs, voltages.get(0, 0) + armState);
            io.setWristVoltage(armInputs, voltages.get(1, 0) + wristState);


        }
    }



    /**
     * Get angle of arm in degrees with a crossover outside of the configuration space
     */
    public double getArmAngle() {
        return Math.toDegrees(armInputs.armAngleRad);
    }

    /**
     * Get angle of arm in radians with a crossover outside of the configuration space
     */
    public double getArmAngleRad() {
        return armInputs.armAngleRad;
    }

    /**
     * Get angle of wrist in degrees with a crossover outside of the configuration space
     */
    public double getWristAngle() {
        double angle = 360 - Math.toDegrees(armInputs.wristAngleRad);
        if (angle > Constants.Wrist.PID.TURNOVER_THRESHOLD) {
            angle -= 360;
        }
        return angle;
    }

    /**
     * Get angle of wrist in radians with a crossover outside of the configuration space
     */
    public double getWristAngleRad() {
        return getWristAngle() * Math.PI / 180.0;
    }

    /**
     * Set setpoint for arm angle in degrees
     */
    public void setArmGoal(double goal) {
        enablePID = true;
        if (goal > Constants.Arm.PID.TURNOVER_THRESHOLD) {
            goal -= 360;
        }
        armPIDController.setGoal(goal * Math.PI / 180.0);
    }

    /**
     * Set setpoint for wrist angle in degrees
     */
    public void setWristGoal(double goal) {
        /*
         * if (goal > Constants.Wrist.PID.TURNOVER_THRESHOLD) { goal -= 360; }
         */
        wristPIDController.setGoal(goal * Math.PI / 180.0);
    }

    /**
     * Get if arm angle is close to the setpoint
     */
    public boolean armInPosition() {
        return armPIDController.atGoal();
    }

    /**
     * Get if wrist angle is close to the setpoint
     */
    public boolean wristInPosition() {
        return wristPIDController.atGoal();
    }

    /**
     * Set arm to extended position
     */
    public void extendArm() {
        io.setArmSolenoid(armInputs, false);
    }

    /**
     * Set arm to retracted position
     */
    public void retractArm() {
        io.setArmSolenoid(armInputs, true);
    }
}
