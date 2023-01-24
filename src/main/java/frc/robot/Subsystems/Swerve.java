package frc.robot.subsystems;

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

/**
 * Creates swerve drive and commands for drive.
 */
public class Swerve extends SubsystemBase {
    public SwerveDriveKinematics swerveDriveKinematics;
    public SwerveModule[] swerveModule;
    public SwerveDrivePoseEstimator swerveDrivePoseEstimator;
    public AHRS gyro = new AHRS(Constants.Swerve.navXID);

    /**
     * Initializes swerve modules.
     */
    public Swerve() {
        swerveModule = new SwerveModule[] {new SwerveModule(0, Constants.ModuleZero.Constants),
            new SwerveModule(1, Constants.ModuleOne.Constants),
            new SwerveModule(2, Constants.ModuleTwo.Constants),
            new SwerveModule(3, Constants.ModuleThree.Constants)};
    }

    /**
     * Moves the swerve drive train
     *
     * @param translation2d The 2d translation in the X-Y plane
     * @param rotation The amount of rotation in the Z axis
     * @param fieldRelative Whether the movement is relative to the field or absolute
     * @param openLoop Open or closed loop system
     */
    public void drive(Translation2d translation2d, Boolean fieldRelative, Double rotation,
        Boolean openLoop) {
        SwerveModuleState[] swerveModuleState =
            Constants.swerveDriveKinematics.toSwerveModuleStates(fieldRelative
                ? ChassisSpeeds.fromFieldRelativeSpeeds(translation2d.getX(), translation2d.getY(),
                    rotation, Rotation2d.fromDegrees(0))
                : new ChassisSpeeds(translation2d.getX(), translation2d.getY(), rotation));
        for (SwerveModule num : swerveModule) {
            num.setDesiredState(swerveModuleState[num.moduleNumber], openLoop);
        }
    }

    /**
     * New command to set wheels inward.
     */
    public void wheelsIn() {
        swerveModule[0].setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(0)), false);
        swerveModule[1].setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(0)), false);
        swerveModule[2].setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(0)), false);
        swerveModule[3].setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(0)), false);

    }

    /**
     * Sets motors to 0 or inactive.
     *
     * @param isOpenLoop Open or closed loop system
     * @param fieldRelative Whether the movement is relative to the field or absolute
     */
    public void setMotorZero(boolean isOpenLoop, boolean fieldRelative) {
        SwerveModuleState[] swerveModuleState =
            Constants.swerveDriveKinematics.toSwerveModuleStates(
                fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(0, 0, 0, getYaw())
                    : new ChassisSpeeds(0, 0, 0));
        for (SwerveModule mod : swerveModule) {
            mod.setDesiredState(swerveModuleState[mod.moduleNumber], isOpenLoop);
        }
    }

    /**
     * Used by SwerveControllerCommand in Auto
     *
     * @param desiStates The desired states of the swerve modules
     */
    public void setModuleStates(SwerveModuleState[] desiStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiStates, Constants.maxSpeed);

        for (SwerveModule mod : swerveModule) {
            mod.setDesiredState(desiStates[mod.moduleNumber], false);
        }

    }

    /**
     * Returns the position of the robot on the field.
     *
     * @return The pose of the robot (x and y are in meters).
     */
    public Pose2d getPose() {
        return swerveDrivePoseEstimator.getEstimatedPosition();
    }

    /**
     * Resets the robot's position on the field.
     *
     * @param pose2d The position on the field that your robot is at.
     */
    public void resetOdometry(Pose2d pose2d) {
        swerveDrivePoseEstimator.resetPosition(getYaw(), getPositions(), pose2d);
    }

    /**
     * Resets the gryo to 0 offset
     */
    public void zeroGyro() {
        gyro.zeroYaw();
    }

    /**
     * Gets the rotation degree from swerve modules.
     */
    public Rotation2d getYaw() {
        float yaw = gyro.getYaw();
        return Rotation2d.fromDegrees(yaw);
    }

    /**
     * Get rotation in Degrees of Gyro
     *
     * @return Rotation of gyro in Degrees
     */
    public double getRotation() {
        return getYaw().getDegrees();
    }

    /**
     * Get position of all swerve modules
     *
     * @return Array of Swerve Module Positions
     */
    public SwerveModulePosition[] getPositions() {
        SwerveModulePosition[] positions = new SwerveModulePosition[4];

        for (SwerveModule mod : swerveModule) {
            positions[mod.moduleNumber] = mod.getPosition();
        }
        return positions;
    }


}
