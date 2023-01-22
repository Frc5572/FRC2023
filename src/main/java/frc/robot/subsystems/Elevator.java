package frc.robot.subsystems;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import frc.robot.Constants;

/**
 * Elevator subsystem.
 */

public class Elevator extends PIDSubsystem {
    private final CANSparkMax leftElevatorMotor =
        new CANSparkMax(Constants.Motors.leftElevatorMotorID, MotorType.kBrushless);
    private final CANSparkMax rightElevatorMotor =
        new CANSparkMax(Constants.Motors.rightElevatorMotorID, MotorType.kBrushless);
    private final CANCoder elevatorCANCoder = new CANCoder(0);


    /**
     * Elevator subsystem.
     */

    public Elevator() {
        super(new PIDController(Constants.ElevatorPID.kP, Constants.ElevatorPID.kI,
            Constants.ElevatorPID.kD));
    }


    @Override
    public void periodic() {
        // This method is called periodically by the CommandScheduler

    }


    @Override
    protected void useOutput(double output, double setpoint) {
        // TODO Auto-generated method stub

    }


    @Override
    protected double getMeasurement() {
        // TODO Auto-generated method stub
        return 0;
    }

}
