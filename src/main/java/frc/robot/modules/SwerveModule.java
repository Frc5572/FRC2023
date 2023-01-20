package frc.robot.modules;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import frc.lib.math.SwerveModuleConstants;

public class SwerveModule {
    public TalonFX angleMotor;
    public TalonFX driveMotor;
    public CANCoder angleCANCoder;
    public int moduleNumber;
    public double angleOffset;
    public double lastAngle;
    public SwerveModuleConstants Constants;

    public SwerveModule(int moduleNumber, SwerveModuleConstants Constants) {
        this.moduleNumber = moduleNumber;
        this.Constants = Constants;
        angleMotor = new TalonFX(Constants.angleMotorID);
        driveMotor = new TalonFX(Constants.driveMotorID);
        angleCANCoder = new CANCoder(Constants.CANCoderID);

    }

}
