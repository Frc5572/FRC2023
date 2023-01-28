package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Pneumatics;

/*
 * Possible Functions to include: - Intakein - Intake out (Varying power levels based on
 * objectloaded) - SpitObject
 */

public class Intake extends SubsystemBase {

    // An attempt to define the motors is as follows...
    CANSparkMax intakeMotorA =
        new CANSparkMax(Constants.IntakeConstants.intakeMotorNumA, MotorType.kBrushless);
    CANSparkMax intakeMotorB =
        new CANSparkMax(Constants.IntakeConstants.intakeMotorNumB, MotorType.kBrushless);

    // Defines intake subsystem pneumatics
    DoubleSolenoid intakeSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH,
        Constants.Pneumatics.intakeFowardChannel, Pneumatics.intakeReverseChannel);

    // Hopefully lets the DoubleSolenoid know where to start.
    public Intake(PneumaticHub ph) {
        this.intakeSolenoid.set(Value.kForward);
    }


    // Creation of the intake MotorControllerGroup.
    private final MotorControllerGroup intakeMotors =
        new MotorControllerGroup(intakeMotorA, intakeMotorB);


    /*
     * Code for pottentially tracking voltage. [Note: This is untested code] // Find this value
     * through testing. public static final double intakeVNormal = 1;
     *
     * // Gets the current voltage of the motors and compares them to the normal voltage to
     * determine if a game piece is fully in. public void intakeVoltage(){ double intakeVA =
     * (this.intakeMotorA.getBusVoltage()); if (intakeVA>intakeVNormal)
     * System.out.println("Game piece is fully in! :)"); }
     */


    public static final double intakeAMPNormal = 1;

    // Gets the current voltage of the motors and compares them to the normal voltage to determine
    // if a game piece is fully in
    public void intakeAMP() {
        double intakeAMPA = (this.intakeMotorA.getBusVoltage());
        if (intakeAMPA > intakeAMPNormal)
            System.out.println("Game piece is fully in! :)");
    }

    // Runs intake motors to intake objects and reverses the solenoid.
    public void intakeIn() {
        intakeSolenoid.toggle();
        intakeMotors.set(Constants.IntakeConstants.intakeInSpeed);
    }

    // Runs intake motors in the opposite direction, thus spiting out objects.
    public void intakeOut() {
        intakeSolenoid.toggle();
        intakeMotors.set(Constants.IntakeConstants.intakeOutSpeed);
    }

    // Stops the intake motors from running.
    public void stop() {
        intakeMotors.set(Constants.IntakeConstants.intakeStopSpeed);
    }

}

