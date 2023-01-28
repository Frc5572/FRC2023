package frc.robot.subsystems;

import java.util.ArrayList;
import org.photonvision.PhotonCamera;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.util.FieldConstants;
import frc.lib.util.swerve.SwerveModule;
import frc.robot.Constants;

/**
 * Creates swerve drive and commands for drive.
 */
public class Swerve extends SubsystemBase {
    public AHRS gyro = new AHRS(Constants.Swerve.navXID);
    public SwerveDrivePoseEstimator swerveOdometry;
    private PhotonCamera cam = new PhotonCamera(Constants.CameraConstants.cameraName);
    public SwerveModule[] swerveMods;
    private double fieldOffset = gyro.getYaw();
    ChassisSpeeds chassisSpeeds;
    private final Field2d field = new Field2d();
    private boolean hasInitialized = false;

    /**
     * Initializes swerve modules.
     */
    public Swerve() {
        SmartDashboard.putData("Field Pos", field);


        swerveMods = new SwerveModule[] {new SwerveModule(0, Constants.Swerve.Mod0.constants),
            new SwerveModule(1, Constants.Swerve.Mod1.constants),
            new SwerveModule(2, Constants.Swerve.Mod2.constants),
            new SwerveModule(3, Constants.Swerve.Mod3.constants)};

        swerveOdometry = new SwerveDrivePoseEstimator(Constants.Swerve.swerveKinematics, getYaw(),
            getPositions(), new Pose2d());
    }

    /**
     * New command to set wheels inward.
     */
    public void wheelsIn() {
        swerveMods[0].setDesiredState(new SwerveModuleState(2, Rotation2d.fromDegrees(45)), false);
        swerveMods[1].setDesiredState(new SwerveModuleState(2, Rotation2d.fromDegrees(135)), false);
        swerveMods[2].setDesiredState(new SwerveModuleState(2, Rotation2d.fromDegrees(-45)), false);
        swerveMods[3].setDesiredState(new SwerveModuleState(2, Rotation2d.fromDegrees(-135)),
            false);
        this.setMotorsZero(Constants.Swerve.isOpenLoop, Constants.Swerve.isFieldRelative);
    }

    /**
     * Moves the swerve drive train
     *
     * @param translation The 2d translation in the X-Y plane
     * @param rotation The amount of rotation in the Z axis
     * @param fieldRelative Whether the movement is relative to the field or absolute
     * @param isOpenLoop Open or closed loop system
     */
    public void drive(Translation2d translation, double rotation, boolean fieldRelative,
        boolean isOpenLoop) {
        ChassisSpeeds chassisSpeeds = fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(translation.getX(), translation.getY(),
                rotation, Rotation2d.fromDegrees(getYaw().getDegrees() - fieldOffset))
            : new ChassisSpeeds(translation.getX(), translation.getY(), rotation);
        setModuleStates(chassisSpeeds);
    }

    /**
     * Sets motors to 0 or inactive.
     *
     * @param isOpenLoop Open or closed loop system
     * @param fieldRelative Whether the movement is relative to the field or absolute
     */
    public void setMotorsZero(boolean isOpenLoop, boolean fieldRelative) {
        System.out.println("Setting Zero!!!!!!");
        setModuleStates(new ChassisSpeeds(0, 0, 0));
    }

    /**
     * Used by SwerveControllerCommand in Auto
     *
     * @param desiredStates The desired states of the swerve modules
     */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.Swerve.maxSpeed);

        for (SwerveModule mod : swerveMods) {
            mod.setDesiredState(desiredStates[mod.moduleNumber], false);
        }
    }

    /**
     * Sets swerve module states using Chassis Speeds.
     *
     * @param chassisSpeeds The desired Chassis Speeds
     */
    public void setModuleStates(ChassisSpeeds chassisSpeeds) {
        SwerveModuleState[] swerveModuleStates =
            Constants.Swerve.swerveKinematics.toSwerveModuleStates(chassisSpeeds);
        setModuleStates(swerveModuleStates);
    }

    /**
     * Returns the position of the robot on the field.
     *
     * @return The pose of the robot (x and y are in meters).
     */
    public Pose2d getPose() {
        return swerveOdometry.getEstimatedPosition();
    }

    /**
     * Resets the robot's position on the field.
     *
     * @param pose The position on the field that your robot is at.
     */
    public void resetOdometry(Pose2d pose) {
        swerveOdometry.resetPosition(getYaw(), getPositions(), pose);
    }

    /**
     * Gets the states of each swerve module.
     *
     * @return Swerve module state
     */
    public SwerveModuleState[] getStates() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for (SwerveModule mod : swerveMods) {
            states[mod.moduleNumber] = mod.getState();
        }
        return states;
    }

    /**
     * Resets the gyro field relative driving offset
     */
    public void resetFieldRelativeOffset() {
        // gyro.zeroYaw();
        fieldOffset = getYaw().getDegrees();
    }

    /**
     * Gets the rotation degree from swerve modules.
     */
    public Rotation2d getYaw() {
        float yaw = gyro.getYaw();
        return (Constants.Swerve.invertGyro) ? Rotation2d.fromDegrees(-yaw)
            : Rotation2d.fromDegrees(yaw);
    }

    public String getStringYaw() {
        float yaw = gyro.getYaw();
        return (Constants.Swerve.invertGyro) ? "Yaw: " + (360 - yaw) : "Yaw: " + yaw;
    }

    @Override
    public void periodic() {
        Rotation2d yaw = getYaw();
        swerveOdometry.update(yaw, getPositions());
        var res = cam.getLatestResult();
        if (res.hasTargets()) {
            var imageCaptureTime = res.getTimestampSeconds();
            if (!hasInitialized) {
                var target = res.getBestTarget();
                var camToTargetTrans = target.getBestCameraToTarget();
                var aprilTagPose = FieldConstants.aprilTags.get(target.getFiducialId());
                if (aprilTagPose != null) {
                    var camPose = aprilTagPose.transformBy(camToTargetTrans.inverse());
                    var robotPose =
                        camPose.transformBy(Constants.CameraConstants.kCameraToRobot).toPose2d();
                    swerveOdometry.resetPosition(getYaw(), getPositions(), robotPose);
                    SmartDashboard.putNumberArray("Initial Position",
                        new double[] {robotPose.getX(), robotPose.getY()});
                    hasInitialized = true;
                }

            }
            var pose2dList = new ArrayList<Pose2d>();
            var target = res.getBestTarget();
            // for (var Target : res.targets) {

            var camToTargetTrans = target.getBestCameraToTarget();
            var aprilTagPose = FieldConstants.aprilTags.get(target.getFiducialId());
            if (aprilTagPose != null) {
                var camPose = aprilTagPose.transformBy(camToTargetTrans.inverse());
                var robotPose =
                    camPose.transformBy(Constants.CameraConstants.kCameraToRobot).toPose2d();
                pose2dList.add(robotPose);
                // swerveOdometry.resetPosition(getYaw(), getPositions(), robotPose);
                if (robotPose.minus(getPose()).getTranslation()
                    .getNorm() < Constants.CameraConstants.largestDistance) {
                    swerveOdometry.addVisionMeasurement(robotPose, imageCaptureTime,
                        VecBuilder.fill(Constants.SwerveTransformPID.stdDevMod / target.getArea(),
                            Constants.SwerveTransformPID.stdDevMod / target.getArea(),
                            Constants.SwerveTransformPID.stdDevMod / target.getArea()));
                }
            }
            // }


            outer: for (int i = 0; i < pose2dList.size(); i++) {
                for (int j = i + 1; j < pose2dList.size(); j++) {
                    var diff = pose2dList.get(i).minus(pose2dList.get(j));
                    if (diff.getTranslation()
                        .getNorm() < Constants.CameraConstants.largestDistance) {
                        swerveOdometry.resetPosition(getYaw(), getPositions(), pose2dList.get(i));
                        break outer;
                    }
                }
            }
        }


        field.setRobotPose(swerveOdometry.getEstimatedPosition());

        SmartDashboard.putBoolean("Has Initialized", hasInitialized);
        SmartDashboard.putNumber("Robot X", swerveOdometry.getEstimatedPosition().getX());
        SmartDashboard.putNumber("Robot Y", swerveOdometry.getEstimatedPosition().getY());
        SmartDashboard.putNumber("Robot Rotation",
            swerveOdometry.getEstimatedPosition().getRotation().getDegrees());
        SmartDashboard.putNumber("Gyro Yaw", yaw.getDegrees());
        SmartDashboard.putNumber("Field Offset", fieldOffset);
        SmartDashboard.putNumber("Gyro Yaw - Offset", yaw.getDegrees() - fieldOffset);
        SmartDashboard.putNumber("Gyro roll", gyro.getRoll());


        for (SwerveModule mod : swerveMods) {
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Cancoder",
                mod.getCanCoder().getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Integrated",
                mod.getState().angle.getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity",
                mod.getState().speedMetersPerSecond);
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Position",
                mod.getPosition().distanceMeters);
        }
    }

    /**
     * Get position of all swerve modules
     *
     * @return Array of Swerve Module Positions
     */
    public SwerveModulePosition[] getPositions() {
        SwerveModulePosition[] positions = new SwerveModulePosition[4];

        for (SwerveModule mod : swerveMods) {
            positions[mod.moduleNumber] = mod.getPosition();

        }
        return positions;
    }

    public void resetInitialized() {
        this.hasInitialized = false;
    }
}
