package frc.robot.subsystems.swerve;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;

public class Swerve implements Subsystem {

    private SwerveIO io;
    private SwerveModIO[] ioMods;
    private SwerveInputsAutoLogged inputs = new SwerveInputsAutoLogged();
    private SwerveModInputsAutoLogged[] modInputs;

    public Swerve(SwerveIO io) {
        this.io = io;
        this.ioMods = new SwerveModIO[4];
        this.modInputs = new SwerveModInputsAutoLogged[4];
        for (int i = 0; i < 4; i++) {
            this.ioMods[i] = this.io.newSwerveMod(i, Constants.Swerve.MODULES[i]);
            this.modInputs[i] = new SwerveModInputsAutoLogged();
        }
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Swerve", inputs);
        for (int i = 0; i < ioMods.length; i++) {
            ioMods[i].updateInputs(modInputs[i]);
            Logger.getInstance().processInputs("SwerveMod[" + i + "]", modInputs[i]);
        }
    }

}
