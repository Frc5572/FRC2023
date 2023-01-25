package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/*
 * Possible Functions to include: - Intakein - Intake out (Varying power levels based on
 * objectloaded) - SpitObject
 */

public class Intake extends SubsystemBase {

    // An attempt to define the motor is as follows...
    CANSparkMax intakeMotorA =
        new CANSparkMax(Constants.Pneumatics.IntakeConstants.intakeMotorNumA, MotorType.kBrushless);
    CANSparkMax intakeMotorB =
        new CANSparkMax(Constants.Pneumatics.IntakeConstants.intakeMotorNumB, MotorType.kBrushless);

    // Creation of the intake MotorControllerGroup .
    private final MotorControllerGroup intakeMotors =
        new MotorControllerGroup(intakeMotorA, intakeMotorB);


    // Runs intake to intake objects.
    public void intakeIn() {
        this.intakeMotors.set(1);
    }

    // Runs intake in the opposite direction, thus spiting out objects.
    private void intakeOut() {
        intakeMotors.set(-1);
    }

    // Stops the intake from running.
    public void stop() {
        this.intakeMotors.set(0);
    }
}

