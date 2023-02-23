package frc.robot.subsystems;

import java.util.Map;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

/**
 * Creates the subsystem for the wrist intake.
 */
public class WristIntake extends SubsystemBase {

    private final CANSparkMax leftWristMotor =
        new CANSparkMax(Constants.Wrist.LEFT_MOTOR_ID, MotorType.kBrushless);
    private final CANSparkMax rightWristMotor =
        new CANSparkMax(Constants.Wrist.RIGHT_MOTOR_ID, MotorType.kBrushless);
    private final MotorControllerGroup wristIntakeMotors =
        new MotorControllerGroup(leftWristMotor, rightWristMotor);
    private final DoubleSolenoid wristSolenoid;


    private final DigitalInput coneSensor1 = new DigitalInput(Constants.Wrist.CONE_SENSOR_ID_UPPER);
    private final DigitalInput coneSensor2 = new DigitalInput(Constants.Wrist.CONE_SENSOR_ID_LOWER);
    private final DigitalInput cubeSensor1 = new DigitalInput(Constants.Wrist.CUBE_SENSOR_ID_UPPER);
    private final DigitalInput cubeSensor2 = new DigitalInput(Constants.Wrist.CUBE_SENSOR_ID_LOWER);

    private GenericEntry coneGrabbed = RobotContainer.mainDriverTab
        .add("Cone Grabbed", getConeSensor()).withWidget(BuiltInWidgets.kBooleanBox)
        .withProperties(Map.of("Color when true", "#FFFF00", "Color when false", "#FFFFFF"))
        .withPosition(10, 2).withSize(2, 1).getEntry();
    private GenericEntry cubeGrabbed = RobotContainer.mainDriverTab
        .add("Cube Grabbed", getConeSensor()).withWidget(BuiltInWidgets.kBooleanBox)
        .withProperties(Map.of("Color when true", "#A020F0", "Color when false", "#FFFFFF"))
        .withPosition(10, 3).withSize(2, 1).getEntry();
    private GenericEntry solenoidStatus = RobotContainer.mainDriverTab.add("Solenoid Status", false)
        .withWidget(BuiltInWidgets.kBooleanBox)
        .withProperties(Map.of("Color when true", "green", "Color when false", "red"))
        .withPosition(0, 4).withSize(2, 1).getEntry();

    /**
     * Create Wrist Intake Subsystem
     *
     * @param ph Pnuematic Hub instance
     */
    public WristIntake(PneumaticHub ph) {
        leftWristMotor.setInverted(true);
        this.wristSolenoid = ph.makeDoubleSolenoid(Constants.Wrist.SOLENOID_FORWARD_CHANNEL,
            Constants.Wrist.SOLENOID_REVERSE_CHANNEL);
        this.wristSolenoid.set(Value.kForward);
    }

    @Override
    public void periodic() {
        coneGrabbed.setBoolean(getConeSensor());
        cubeGrabbed.setBoolean(getCubeSensor());
        solenoidStatus.setBoolean(this.wristSolenoid.get() == Value.kForward);
    }

    // Runs wrist intake motor to intake a game piece.
    public void intake() {
        wristIntakeMotors.set(Constants.Wrist.INTAKE_SPEED);
    }

    // Runs wrist intake motor to gently spit out game piece.
    public void release() {
        wristIntakeMotors.set(Constants.Wrist.INTAKE_RELEASE_SPEED);
    }

    // Stops the wrist intake motor from running.
    public void stop() {
        wristIntakeMotors.set(Constants.Wrist.INTAKE_STOP_SPEED);
    }

    // Releases GPs in intake (if any).
    // Primarily used to prevent the accidental holding of multiple GPs penalty.
    public void panic() {
        wristIntakeMotors.set(Constants.Wrist.INTAKE_PANIC_SPEED);
    }

    // Get and return the status of the cone sensors.
    public boolean getConeSensor() {
        return coneSensor1.get() && coneSensor2.get();
    }

    // Get and return the status of the cube sensors.
    public boolean getCubeSensor() {
        return cubeSensor1.get() && cubeSensor2.get();
    }

    /**
     * Actuate solenoids
     */
    public void toggleSolenoid() {
        this.wristSolenoid.toggle();
    }

    /**
     * Open grabber
     */
    public void openGrabber() {
        this.wristSolenoid.set(Value.kForward);
    }

    /**
     * Close grabber
     */
    public void closeGrabber() {
        this.wristSolenoid.set(Value.kReverse);
    }
}
