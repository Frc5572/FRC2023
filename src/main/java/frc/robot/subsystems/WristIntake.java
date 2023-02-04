package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Creates the subsystem for the wrist intake.
 */
public class WristIntake extends SubsystemBase {

    private final CANSparkMax wristMotor = new CANSparkMax(
        Constants.IntakeConstants.WristConstants.INTAKE_MOTOR_ID, MotorType.kBrushless);

    private final DigitalInput coneSensor1 =
        new DigitalInput(Constants.IntakeConstants.WristConstants.CONE_SENSOR_ID);
    private final DigitalInput coneSensor2 =
        new DigitalInput(Constants.IntakeConstants.WristConstants.CONE_SENSOR_ID2);
    private final DigitalInput cubeSensor1 =
        new DigitalInput(Constants.IntakeConstants.WristConstants.CUBE_SENSOR_ID);
    private final DigitalInput cubeSensor2 =
        new DigitalInput(Constants.IntakeConstants.WristConstants.CUBE_SENSOR_ID2);

    public WristIntake() {}

    // Runs wrist intake motor to intake a game piece.
    public void intake() {
        wristMotor.set(Constants.IntakeConstants.WristConstants.INTAKE_SPEED);
    }

    // Runs wrist intake motor to gently spit out game piece.
    public void release() {
        wristMotor.set(Constants.IntakeConstants.WristConstants.INTAKE_RELEASE_SPEED);
    }

    // Stops the wrist intake motor from running.
    public void stop() {
        wristMotor.set(Constants.IntakeConstants.WristConstants.INTAKE_STOP_SPEED);
    }

    // Releases GPs in intake (if any).
    // Primarily used to prevent the accidental holding of multiple GPs penalty.
    public void panic() {
        wristMotor.set(Constants.IntakeConstants.WristConstants.INTAKE_PANIC_SPEED);
    }

    // Get and return the status of the cone sensors.
    public boolean getConeSensor() {
        return coneSensor1.get() && coneSensor2.get();
    }

    // Get and return the status of the cube sensors.
    public boolean getCubeSensor() {
        return cubeSensor1.get() && cubeSensor2.get();
    }
}
