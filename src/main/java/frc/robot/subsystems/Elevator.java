package frc.robot.subsystems;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import frc.robot.Constants;

/**
 * Elevator subsystem.
 */

public class Elevator extends ProfiledPIDSubsystem {
    private final CANSparkMax leftElevatorMotor =
        new CANSparkMax(Constants.Motors.leftElevatorMotorID, MotorType.kBrushless);
    private final CANSparkMax rightElevatorMotor =
        new CANSparkMax(Constants.Motors.rightElevatorMotorID, MotorType.kBrushless);
    private final CANCoder elevatorCANCoder = new CANCoder(0);


    /**
     * Elevator subsystem.
     */

    public Elevator() {
        super(new ProfiledPIDController(Constants.ElevatorPID.kP, Constants.ElevatorPID.kI,
            Constants.ElevatorPID.kD,
            new TrapezoidProfile.Constraints(Constants.ElevatorPID.kMaxVelocityRadPerSecond,
                Constants.ElevatorPID.kMaxAccelerationRadPerSecSquared)));

    }

    @Override
    protected double getMeasurement() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected void useOutput(double output, State setpoint) {
        // TODO Auto-generated method stub

    }

}
