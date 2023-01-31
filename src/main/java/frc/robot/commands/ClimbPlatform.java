package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Swerve;

/**
 * Test April tag transform
 */
public class ClimbPlatform extends CommandBase {

    private Swerve swerve;
    boolean beenTilted = false;

    /**
     * Test April tag transform
     */
    public ClimbPlatform(Swerve swerve) {
        this.swerve = swerve;
        this.addRequirements(swerve);
    }

    @Override
    public void initialize() {
        beenTilted = false;
    }

    @Override
    public void execute() {
        double speed = -1.5;
        if (!beenTilted && Math.abs(swerve.getRoll()) > 10) {
            beenTilted = true;
        }
        if (beenTilted && Math.abs(swerve.getRoll()) < 10) {
            speed = -.5;
        }
        SmartDashboard.putBoolean("Been Tilted", beenTilted);
        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(speed, 0, 0);
        chassisSpeeds =
            ChassisSpeeds.fromFieldRelativeSpeeds(chassisSpeeds, swerve.getFieldRelativeHeading());
        SwerveModuleState[] swerveModuleStates =
            Constants.Swerve.SWERVE_KINEMATICS.toSwerveModuleStates(chassisSpeeds);
        swerve.setModuleStates(swerveModuleStates);
    }

    @Override
    public void end(boolean interrupted) {
        swerve.wheelsIn();
    }

    @Override
    public boolean isFinished() {
        return beenTilted && Math.abs(swerve.getRoll()) < 3;
    }
}
