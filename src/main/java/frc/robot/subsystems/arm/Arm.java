package frc.robot.subsystems.arm;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.lib.math.DoubleJointedArmFeedforward;
import frc.robot.Constants;

public class Arm implements Subsystem {

    private ArmIO io;
    private ArmInputsAutoLogged inputs = new ArmInputsAutoLogged();

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

    private Value solenoidState;

    public Arm(ArmIO io) {
        this.io = io;
        this.wristPIDController.setIntegratorRange(Constants.Wrist.PID.MIN_INTEGRAL,
            Constants.Wrist.PID.MAX_INTEGRAL);
        wristPIDController.setTolerance(Math.toRadians(2));
        armPIDController.setTolerance(Math.toRadians(2));

        // armPIDController.enableContinuousInput(0, 2 * Math.PI);
        // armPidController2.enableContinuousInput(0, 2 * Math.PI);
        // wristPIDController.enableContinuousInput(0, 2 * Math.PI);

        enablePID = false;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Arm", inputs);

        Logger.getInstance().recordOutput("Arm/Shoulder/Goal",
            armPIDController.getGoal().position * 180 / Math.PI);
        Logger.getInstance().recordOutput("Arm/Wrist/Goal",
            wristPIDController.getGoal().position * 180 / Math.PI);
        Logger.getInstance().recordOutput("Arm/reset0", reset0);

        if (reset0) {
            armPIDController.reset(getArmAngleRad());
            reset0 = false;
        }
        io.setSolenoid(solenoidState);
        Logger.getInstance().recordOutput("Arm/Solenoid", solenoidState.toString());
        double armState = armPIDController.calculate(getArmAngleRad());
        Logger.getInstance().recordOutput("Arm/Shoulder/PIDState", armState);
        double wristState = wristPIDController.calculate(getWristAngleRad());
        Logger.getInstance().recordOutput("Arm/Wrist/PIDState", armState);

        var voltages = feedforward.calculate(VecBuilder.fill(getArmAngleRad(), getWristAngleRad()));

        Logger.getInstance().recordOutput("Arm/Shoulder/FeedForward", voltages.get(0, 0));
        Logger.getInstance().recordOutput("Arm/Wrist/FeedForward", voltages.get(1, 0));

        double armVoltage = voltages.get(0, 0) + armState;
        double wristVoltage = voltages.get(1, 0) + wristState;

        if (enablePID) {
            io.setArmVoltages(armVoltage, wristVoltage);
        }
        Logger.getInstance().recordOutput("Arm/Shoulder/Voltage", armVoltage);
        Logger.getInstance().recordOutput("Arm/Wrist/Voltage", wristVoltage);
    }

    /**
     * Get angle of arm in degrees with a crossover outside of the configuration space
     */
    public double getArmAngle() {
        double angle = inputs.shoulderPosition;
        if (angle > Constants.Arm.PID.TURNOVER_THRESHOLD) {
            angle -= 360;
        }
        return angle;
    }

    /**
     * Get angle of arm in radians with a crossover outside of the configuration space
     */
    public double getArmAngleRad() {
        return getArmAngle() * Math.PI / 180.0;
    }

    /**
     * Get angle of wrist in degrees with a crossover outside of the configuration space
     */
    public double getWristAngle() {
        double angle = 360 - inputs.wristPosition;
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
        solenoidState = Value.kReverse;
    }

    /**
     * Set arm to retracted position
     */
    public void retractArm() {
        solenoidState = Value.kForward;
    }

}
