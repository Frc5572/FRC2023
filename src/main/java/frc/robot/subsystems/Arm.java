package frc.robot.subsystems;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import frc.robot.Constants;

public class Arm extends ProfiledPIDSubsystem {
    private final CANSparkMax armMotor =
        new CANSparkMax(Constants.Motors.armID, MotorType.kBrushless);
    private final CANCoder armEncoder = new CANCoder(0);

    public Arm() {
        super(
            new ProfiledPIDController(Constants.ArmPID.kP, Constants.ArmPID.kI, Constants.ArmPID.kD,
                new TrapezoidProfile.Constraints(Constants.ArmPID.kMaxVelocityRadPerSecond,
                    Constants.ArmPID.kMaxAccelerationRadPerSecSquared),
                Constants.ArmPID.kF));
    }

    public void useOutput(double output){
        armMotor.setVoltage(getSetpoint(output+));
    }

    @Override
    protected double getMeasurement() {
        // TODO Auto-generated method stub

        return 0;
    }

}
