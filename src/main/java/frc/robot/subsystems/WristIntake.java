package frc.robot.subsystems;

import java.util.Map;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

/**
 * Creates the subsystem for the wrist intake.
 */
public class WristIntake extends SubsystemBase {

    private final WPI_TalonFX wristIntakeMotor = new WPI_TalonFX(Constants.Wrist.WRIST_INTAKE_ID);


    private final DigitalInput coneSensor1 = new DigitalInput(Constants.Wrist.CONE_SENSOR_ID);
    private final DigitalInput cubeSensor1 = new DigitalInput(Constants.Wrist.CUBE_SENSOR_ID_LEFT);
    private final DigitalInput cubeSensor2 = new DigitalInput(Constants.Wrist.CUBE_SENSOR_ID_RIGHT);

    private GenericEntry coneGrabbed = RobotContainer.mainDriverTab
        .add("Cone Grabbed", getConeSensor()).withWidget(BuiltInWidgets.kBooleanBox)
        .withProperties(Map.of("Color when true", "#FFFF00", "Color when false", "#FFFFFF"))
        .withPosition(6, 3).withSize(2, 1).getEntry();
    private GenericEntry cubeGrabbed = RobotContainer.mainDriverTab
        .add("Cube Grabbed", getConeSensor()).withWidget(BuiltInWidgets.kBooleanBox)
        .withProperties(Map.of("Color when true", "#A020F0", "Color when false", "#FFFFFF"))
        .withPosition(6, 2).withSize(2, 1).getEntry();


    /**
     * Create Wrist Intake Subsystem
     */
    public WristIntake() {
        wristIntakeMotor.setInverted(false);
        wristIntakeMotor.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    public void periodic() {
        coneGrabbed.setBoolean(getConeSensor());
        cubeGrabbed.setBoolean(getCubeSensor());
    }

    // Runs wrist intake motor to intake a game piece.
    public void intake() {
        wristIntakeMotor.set(ControlMode.PercentOutput, Constants.Wrist.INTAKE_SPEED);
    }

    // Runs wrist intake motor to intake a game piece.
    public void holdPiece() {
        wristIntakeMotor.setVoltage(Constants.Wrist.HOLD_VOLTS);
    }

    /**
     * Set power of intake motors
     *
     * @param power power of motors from -1 to 1
     */
    public void setMotor(double power) {
        wristIntakeMotor.set(ControlMode.PercentOutput, power);
    }

    /**
     * Releases GPs in intake (if any). Primarily used to prevent the accidental holding of multiple
     * GPs penalty.
     */
    public void panic() {
        setMotor(Constants.Wrist.INTAKE_PANIC_SPEED);
    }

    /**
     * Get and return the status of the cone sensors.
     *
     * @return status of cone touch sensor
     */
    public boolean getConeSensor() {
        return coneSensor1.get();
    }

    /**
     * Get and return the status of the cube sensors.
     *
     * @return status of cube touch sensors
     */
    public boolean getCubeSensor() {
        return cubeSensor1.get() && cubeSensor2.get();
    }
}
