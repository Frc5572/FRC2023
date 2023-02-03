package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/*
 * Possible Functions to include: - SpitObject
 */

public class WristIntake extends SubsystemBase {

    private final CANSparkMax wristMotor =
        new CANSparkMax(Constants.IntakeConstants.W_INTAKE_MOTOR_ID, MotorType.kBrushless);

    private final DigitalInput coneSensor1 =
        new DigitalInput(Constants.IntakeConstants.W_CONE_SENSOR_ID);
    private final DigitalInput coneSensor2 =
        new DigitalInput(Constants.IntakeConstants.W_CONE_SENSOR_ID2);
    private final DigitalInput cubeSensor1 =
        new DigitalInput(Constants.IntakeConstants.W_CUBE_SENSOR_ID);
    private final DigitalInput cubeSensor2 =
        new DigitalInput(Constants.IntakeConstants.W_CUBE_SENSOR_ID2);

    public WristIntake() {}

    // Intakes a cube. Does not run if all touch sensors are triggered and runs panic.
    // Runs until cube is detected in wrist.
    public void intakeCone() {
        if (coneSensor1.get() && coneSensor2.get() && cubeSensor1.get() && cubeSensor2.get()) {
            System.out.println("2 objects detected, abort!");
            panic();
        } else {
            if (!(coneSensor1.get() && coneSensor2.get())) {
                wristMotor.set(Constants.IntakeConstants.INTAKE_SPEED);
            } else {
                stop();
            }
        }
    }

    // Intakes a cube. Does not run if all touch sensors are triggered and runs panic.
    // Runs until cube is detected in wrist.
    public void intakeCube() {
        if (coneSensor1.get() && coneSensor2.get() && cubeSensor1.get() && cubeSensor2.get()) {
            System.out.println("2 objects detected, abort!");
            panic();
        } else {
            if (!(cubeSensor1.get() && cubeSensor2.get())) {
                wristMotor.set(Constants.IntakeConstants.INTAKE_SPEED);
            } else {
                stop();
            }
        }
    }

    // Runs wrist intake motor to gently spit out game piece.
    public void release() {
        wristMotor.set(Constants.IntakeConstants.W_INTAKE_RELEASE_SPEED);
    }

    // Stops the wrist intake motor from running.
    public void stop() {
        wristMotor.set(Constants.IntakeConstants.INTAKE_STOP_SPEED);
    }

    // Releases GPs in intake (if any).
    // Primarily used to prevent the accidental holding of multiple GPs penalty.
    public void panic() {
        wristMotor.set(Constants.IntakeConstants.W_INTAKE_PANIC_SPEED);
    }
}
