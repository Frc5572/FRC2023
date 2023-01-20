package frc.robot.Subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import frc.robot.modules.SwerveModule;

public class Swerve {
    public SwerveDriveKinematics swerveDriveKinematics;
    public SwerveModule[] swerveModule;

    public Swerve() {
        swerveModule = new SwerveModule[] {new SwerveModule(0, Constants.ModuleZero.Constants),
            new SwerveModule(1, Constants.ModuleOne.Constants),
            new SwerveModule(2, Constants.ModuleTwo.Constants),
            new SwerveModule(3, Constants.ModuleThree.Constants),};
    }

    public void Drive(Translation2d translation2d, Boolean fieldRelative, Double Rotation, Boolean openLoop) {
        SwerveModuleState [] swerveModuleState = Constants.swerveDriveKinematics.toSwerveModuleStates(fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(translation2d.getX(),translation2d.getY(), Rotation, Rotation2d.fromDegrees(0))
        : new ChassisSpeeds(translation2d.getX(), translation2d.getY(), Rotation));
        for(SwerveModule num : swerveModule) {
            num.s
        }
    }
}
