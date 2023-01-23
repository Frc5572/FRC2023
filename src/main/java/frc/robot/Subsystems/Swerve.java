package frc.robot.Subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.modules.SwerveModule;

public class Swerve extends SubsystemBase {
    public SwerveDriveKinematics swerveDriveKinematics;
    public SwerveModule[] swerveModule;
    public SwerveDrivePoseEstimator swerveDrivePoseEstimator;
    public AHRS gyro = new AHRS(Constants.Swerve.navXID);

    public Swerve() {
        swerveModule = new SwerveModule[] {new SwerveModule(0, Constants.ModuleZero.Constants),
            new SwerveModule(1, Constants.ModuleOne.Constants),
            new SwerveModule(2, Constants.ModuleTwo.Constants),
            new SwerveModule(3, Constants.ModuleThree.Constants),};
    }

    public void Drive(Translation2d translation2d, Boolean fieldRelative, Double Rotation,
        Boolean openLoop) {
        SwerveModuleState[] swerveModuleState =
            Constants.swerveDriveKinematics.toSwerveModuleStates(fieldRelative
                ? ChassisSpeeds.fromFieldRelativeSpeeds(translation2d.getX(), translation2d.getY(),
                    Rotation, Rotation2d.fromDegrees(0))
                : new ChassisSpeeds(translation2d.getX(), translation2d.getY(), Rotation));
        for (SwerveModule num : swerveModule) {
            num.DesiredState(swerveModuleState[num.moduleNumber], openLoop);
        }
    }

    public void wheelsIn() {
        swerveModule[0].DesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(0)), false);
        swerveModule[1].DesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(0)), false);
        swerveModule[2].DesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(0)), false);
        swerveModule[3].DesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(0)), false);

    }

    public void setMotorZero() {
        SwerveModuleState[] swerveModuleState =
            Constants.swerveDriveKinematics.toSwerveModuleStates(new ChassisSpeeds(0, 0, 0));
    }

    public void setModuleStates(SwerveModuleState[] desiStates) {
        swerveDriveKinematics.desaturateWheelSpeeds(desiStates, Constants.maxSpeed);
    }

    public Pose2d getPose() {
        return swerveDrivePoseEstimator.getEstimatedPosition();
    }

    public void resetOdometry(Pose2d pose2d) {
        swerveDrivePoseEstimator.resetPosition(getYaw(), null, pose2d);
    }

    public void zeroGyro() {
        gyro.zeroYaw();
    }

    public Rotation2d getYaw() {
        float yaw = gyro.getYaw();
        return Rotation2d.fromDegrees(yaw);
    }

    public double getRotation() {
        return getYaw().getDegrees();
    }
    public SwerveModulePosition [] getPositions() {
        SwerveModulePosition [] positions = new SwerveModulePosition[4];

        for (SwerveModule mod : swerveModule) {
            positions[mod.moduleNumber] = mod.getPosition();
        }
        return positions;
    }


}
