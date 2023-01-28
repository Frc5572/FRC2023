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

/*
 * Possible Functions to include: - SpitObject
 */

public class Intake extends SubsystemBase {

    // An attempt to define the motors is as follows...
    CANSparkMax intakeMotorA =
        new CANSparkMax(Constants.IntakeConstants.intakeMotorNumA, MotorType.kBrushless);
    CANSparkMax intakeMotorB =
        new CANSparkMax(Constants.IntakeConstants.intakeMotorNumB, MotorType.kBrushless);

    // Defines intake subsystem pneumatics.
    DoubleSolenoid intakeSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH,
        Constants.Pneumatics.intakeFowardChannel, Constants.Pneumatics.intakeReverseChannel);

    // Hopefully lets the DoubleSolenoid know where to start.
    public Intake(PneumaticHub ph) {
        this.intakeSolenoid.set(Value.kForward);
    }


    // Creation of the intake MotorControllerGroup.
    private final MotorControllerGroup intakeMotors =
        new MotorControllerGroup(intakeMotorA, intakeMotorB);

    public static final double intakeAMPNormal = 1;
    // Use this code to determine the Normal intake motor AMP (AKA: the AMP of a motor that is not
    // stalling).
    /*
     * public void intakeAMPN(){ double intakeAMPAN = (this.intakeMotorA.getAppliedOutput());
     * System.out.println("The AMP of the motor is "+ intakeAMPAN); }
     */

    // Gets the current voltage of the motors and compares them to the normal voltage to determine
    // if a game piece is fully in.
    public void intakeAMP() {
        double intakeAMPA = (this.intakeMotorA.getAppliedOutput());
        if (intakeAMPA > intakeAMPNormal)
            System.out.println("Game piece is fully in! :)");
    }

    // Runs intake motors to intake objects and reverses the solenoid.
    public void intakeIn() {
        intakeSolenoid.toggle();
        intakeMotors.set(Constants.IntakeConstants.intakeInSpeed);
    }

    // Runs intake motors in the opposite direction to spit out a cone.
    public void intakeOutCone() {
        intakeSolenoid.toggle();
        intakeMotors.set(Constants.IntakeConstants.intakeOutSpeedCone);
    }

    // Runs intake motors in the opposite direction to spit out a cube.
    public void intakeOutCube() {
        intakeSolenoid.toggle();
        intakeMotors.set(Constants.IntakeConstants.intakeOutSpeedCube);
    }

    // Runs intake motors in the opposite direction to gently spit out an unintentional game piece.
    public void SpitObject() {
        intakeSolenoid.toggle();
        intakeMotors.set(Constants.IntakeConstants.intakeSpitSpeed);
    }

    // Stops the intake motors from running.
    public void stop() {
        intakeMotors.set(Constants.IntakeConstants.intakeStopSpeed);
    }

}

