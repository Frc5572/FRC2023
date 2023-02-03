package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Creates the subsystem for the dropdown intake.
 */
public class DropIntake extends SubsystemBase {

    // An attempt to define the motors is as follows...
    private final CANSparkMax dropMotor =
        new CANSparkMax(Constants.IntakeConstants.DROP_MOTOR_ID, MotorType.kBrushless);
    private final CANSparkMax intakeMotor =
        new CANSparkMax(Constants.IntakeConstants.INTAKE_MOTOR_ID, MotorType.kBrushless);

    public DropIntake() {}

    // Runs intake motors to intake objects and reverses the solenoid.
    public void intakeDeploy() {
        dropMotor.set(Constants.IntakeConstants.M_DROP_SPEED);
        intakeMotor.set(Constants.IntakeConstants.INTAKE_SPEED);
    }

    // Runs intake motors in the opposite direction to spit out a cone.
    public void intakeRetract() {
        dropMotor.set(Constants.IntakeConstants.M_RETRACT_SPEED);
        intakeMotor.set(Constants.IntakeConstants.INTAKE_STOP_SPEED);
    }

    // Runs intake motors in the opposite direction to spit out an unintentional game piece.
    public void spitObject() {
        intakeMotor.set(-Constants.IntakeConstants.INTAKE_SPEED);
    }
}

