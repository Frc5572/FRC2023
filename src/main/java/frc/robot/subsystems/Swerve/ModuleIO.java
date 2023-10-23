package frc.robot.subsystems.Swerve;

import org.littletonrobotics.junction.AutoLog;

public interface ModuleIO {

    //
    @AutoLog

    public static class moduleInputs {
        public double moduleRotationAngle;
        public double speedMetersPerSecond;
        public double distancemeters;
        public double driveVoltage;
    }

    public void updateInputs(moduleInputs inputs);

    public void setDriveVoltage(double volts);



}
