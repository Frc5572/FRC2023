package frc.robot.subsystems.swerve2;

import frc.lib.util.swerve.SwerveModule;
import frc.lib.util.swerve.SwerveModuleConstants;
import frc.lib.util.swerve.SwerveModuleReal;

public class SwerveReal implements SwerveIO {

    @Override
    public void updateInputs(SwerveInputs inputs) {

    }

    @Override
    public SwerveModule createSwerveModule(int moduleNumber, SwerveModuleConstants constants) {
        return new SwerveModule(moduleNumber, constants, new SwerveModuleReal(constants));
    }

}
