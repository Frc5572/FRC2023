package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
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
    PIDController elevatorPID;
    Encoder elevatorEncoder;
    int elevatorPos;
    double elevatorP = 0;
    double elevatorI = 0;
    double elevatorD = 0;

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


    @Override
    public void periodic() {
        // This method is called periodically by the CommandScheduler



        // Need someone to review my code here, I'm unsure that this is the correct way to go about
        // this

        // Trying to get the PID via varibles that would change
        elevatorPID = new PIDController(elevatorP, elevatorI, elevatorD);

        // Attempting to move left & right motor to the elevatorPos, the encoder tik for the state
        // we want, while also accounting for PID
        leftElevatorMotor.set(elevatorPID.calculate(elevatorEncoder.getDistance(),
            Constants.Elevator.elevatorEncoderPos[elevatorPos]));
        rightElevatorMotor.set(elevatorPID.calculate(elevatorEncoder.getDistance(),
            Constants.Elevator.elevatorEncoderPos[elevatorPos]));

    }

}
