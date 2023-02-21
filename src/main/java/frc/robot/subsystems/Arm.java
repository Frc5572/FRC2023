package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.math.AngledElevatorFeedForward;
import frc.robot.Constants;

/**
 * Creates the subsystem for the arm.
 */
public class Arm extends SubsystemBase {
    private final CANSparkMax armMotor1 =
        new CANSparkMax(Constants.Arm.ARM_ID, MotorType.kBrushless);
    private final CANSparkMax armMotor2 =
        new CANSparkMax(Constants.Arm.ARM_ID_2, MotorType.kBrushless);
    // private final MotorControllerGroup armMotors = new MotorControllerGroup(armMotor1,
    // armMotor2);
    private ArmFeedforward m_feedforward = new ArmFeedforward(Constants.Arm.PID.K_SVOLTS,
        getArmKg(), Constants.Arm.PID.K_WVOLT_SECOND_PER_RAD,
        Constants.Arm.PID.K_AVOLT_SECOND_SQUARED_PER_RAD);
    private final AbsoluteEncoder encoder1 = armMotor1.getAbsoluteEncoder(Type.kDutyCycle);
    private final AbsoluteEncoder encoder2 = armMotor2.getAbsoluteEncoder(Type.kDutyCycle);
    // private final ProfiledPIDController pid_controller =
    // new ProfiledPIDController(Constants.Arm.PID.KP, Constants.Arm.PID.KI, Constants.Arm.PID.KD,
    // new TrapezoidProfile.Constraints(Constants.Arm.PID.K_MAX_VELOCITY_RAD_PER_SECOND,
    // Constants.Arm.PID.K_MAX_ACCELERATION_RAD_PER_SEC_SQUARED));
    private final PIDController armPIDController1 =
        new PIDController(Constants.Arm.PID.KP, Constants.Arm.PID.KI, Constants.Arm.PID.KD);
    private final PIDController armPIDController2 =
        new PIDController(Constants.Arm.PID.KP, Constants.Arm.PID.KI, Constants.Arm.PID.KD);

    private final double encoder1Offset = 324.8828030;
    private final double encoder2Offset = 271.8351674;
    private boolean enablePID = false;


    private final CANSparkMax elevatorMotor =
        new CANSparkMax(Constants.Elevator.ELEVATOR_MOTOR_ID, MotorType.kBrushless);
    private final RelativeEncoder elevatorEncoder = elevatorMotor.getEncoder();
    private AngledElevatorFeedForward elevatorFeedForward =
        new AngledElevatorFeedForward(0, getArmKg(), 0);
    private final PIDController elevatorPIDController =
        new PIDController(Constants.Elevator.PID.ELEVATOR_KP, Constants.Elevator.PID.ELEVATOR_KI,
            Constants.Elevator.PID.ELEVATOR_KD);

    public final double elevatorMaxEncoder = 2.70; // 2.7142841815948486



    /**
     * Arm Subsystem
     */
    public Arm() {
        // Arm
        armPIDController1.setTolerance(2);
        armPIDController2.setTolerance(2);
        armMotor1.restoreFactoryDefaults();
        armMotor2.restoreFactoryDefaults();
        armMotor1.setIdleMode(IdleMode.kBrake);
        armMotor2.setIdleMode(IdleMode.kBrake);
        armMotor1.setInverted(false);
        armMotor2.setInverted(true);
        encoder1.setPositionConversionFactor(360);
        encoder1.setVelocityConversionFactor(360);
        encoder1.setInverted(true);
        encoder1.setZeroOffset(encoder1Offset);
        encoder2.setPositionConversionFactor(360);
        encoder2.setVelocityConversionFactor(360);
        encoder2.setInverted(false);
        encoder2.setZeroOffset(encoder2Offset);
        armMotor1.burnFlash();
        armMotor2.burnFlash();
        // Elevator Stuff
        elevatorPIDController.setTolerance(2);
        elevatorMotor.restoreFactoryDefaults();
        elevatorMotor.setInverted(false);
        elevatorMotor.setIdleMode(IdleMode.kBrake);
        elevatorMotor.burnFlash();
    }

    @Override
    public void periodic() {
        if (enablePID) {
            armToAngle();
            elevatorToPosition();
        }
    }

    /**
     * Set target angle for Arm
     *
     * @param goal Target Angel in Degrees
     */
    public void setArmGoal(double goal) {
        armPIDController1.setSetpoint(goal);
        armPIDController2.setSetpoint(goal);
    }

    /**
     * Moves the arm to specified angle and stops when done.
     *
     * @param angle requested angle in degrees
     */
    public void armToAngle() {
        m_feedforward = new ArmFeedforward(Constants.Arm.PID.K_SVOLTS, getArmKg(),
            Constants.Arm.PID.K_WVOLT_SECOND_PER_RAD,
            Constants.Arm.PID.K_AVOLT_SECOND_SQUARED_PER_RAD);
        armMotor1.setVoltage(armPIDController1.calculate(getAngleMeasurement1())
            + m_feedforward.calculate(Math.toRadians(getAngleMeasurement1() - 90), 0));
        armMotor2.setVoltage(armPIDController2.calculate(getAngleMeasurement2())
            + m_feedforward.calculate(Math.toRadians(getAngleMeasurement2() - 90), 0));
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
    public double getAngleMeasurement1() {
        return encoder1.getPosition();
    }


    /**
     * Get the current angle that is stated by the encoder.
     *
     * @return Current angle reported by encoder (0 - 360)
     */
    public double getAngleMeasurement2() {
        return encoder2.getPosition();
    }

    /**
     * Check if aligned with a requested goal.
     *
     * @return True if properly aligned, false if not.
     */
    public boolean checkIfAligned1() {
        return armPIDController1.atSetpoint();
    }

    /**
     * Check if aligned with a requested goal.
     *
     * @return True if properly aligned, false if not.
     */
    public boolean checkIfAligned2() {
        return armPIDController2.atSetpoint();
    }

    public boolean checkArmInPosition() {
        return checkIfAligned1() && checkIfAligned2();
    }

    // ---------------- ELEVATOR ----------------------------

    /**
     * Set target position for Elevator
     *
     * @param goal Set target position for Elevator in rotations of motor
     */
    public void setElevatorGoal(double goal) {
        elevatorPIDController.setSetpoint(goal);
    }


    /**
     * Sets the elevator to go until a certain degree has been reached.
     */
    public void elevatorToPosition() {
        elevatorMotor.setVoltage(elevatorPIDController.calculate(getElevatorPosition())
            + elevatorFeedForward.calculate(Math.toRadians(getAngleMeasurement1() - 90), 0));
    }

    /**
     * Gets the encoder measurement of the elevator in rotation degrees.
     */
    public double getElevatorPosition() {
        return elevatorEncoder.getPosition();
    }


    /**
     * Check if aligned with a requested goal.
     *
     * @return True if properly aligned, false if not.
     */
    public boolean checkElevatorAligned() {
        return elevatorPIDController.atSetpoint();
    }

    /**
     * Calculate Kg based on elevator extension
     */
    public double getArmKg() {
        double slope = (Constants.Arm.PID.K_GVOLTS_MAX - Constants.Arm.PID.K_GVOLTS_MIN)
            / (elevatorMaxEncoder - 0);
        return slope * getElevatorPosition() + elevatorMaxEncoder;
    }
}
