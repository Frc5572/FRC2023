package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Elevator extends SubsystemBase {
    CANSparkMax leftElevatorMotor;
    CANSparkMax rightElevatorMotor;
    MotorControllerGroup elevatorMotors;

    public Elevator() {


    }
}
