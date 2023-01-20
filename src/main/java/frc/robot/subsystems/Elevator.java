package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


/**
 * Elevator subsystem.
 */

public class Elevator extends SubsystemBase {
    CANSparkMax leftElevatorMotor;
    CANSparkMax rightElevatorMotor;
    MotorControllerGroup elevatorMotors;

    /**
     * Elevator subsystem.
     */

    public Elevator() {
        leftElevatorMotor =
            new CANSparkMax(Constants.Motors.leftElevatorMotorID, MotorType.kBrushless);
        rightElevatorMotor =
            new CANSparkMax(Constants.Motors.rightElevatorMotorID, MotorType.kBrushless);
        elevatorMotors = new MotorControllerGroup(leftElevatorMotor, rightElevatorMotor);

        leftElevatorMotor.setIdleMode(IdleMode.kBrake);
        rightElevatorMotor.setIdleMode(IdleMode.kBrake);

        leftElevatorMotor.setInverted(false);
        rightElevatorMotor.setInverted(false);
    }



}
