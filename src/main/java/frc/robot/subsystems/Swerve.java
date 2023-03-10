package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.Map;
import org.photonvision.PhotonCamera;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.util.FieldConstants;
import frc.lib.util.swerve.SwerveModule;
import frc.robot.Constants;
import frc.robot.RobotContainer;

/**
 * Creates swerve drive and commands for drive.
 */
public class Swerve extends SubsystemBase {
    private AHRS gyro = new AHRS(Constants.Swerve.navXID);
    private SwerveDrivePoseEstimator swerveOdometry;
    private PhotonCamera cam = new PhotonCamera(Constants.CameraConstants.CAMERA_NAME);
    private SwerveModule[] swerveMods;
    private double fieldOffset = gyro.getYaw();
    private final Field2d field = new Field2d();
    private boolean hasInitialized = false;
    // private ComplexWidget fieldWidget = RobotContainer.mainDriverTab.add("Field Pos", field)
    // .withWidget(BuiltInWidgets.kField).withSize(8, 4) // make the widget 2x1
    // .withPosition(0, 0); // place it in the top-left corner
    private ComplexWidget fieldWidget = RobotContainer.autoTab.add("Field Pos", field)
        .withWidget(BuiltInWidgets.kField).withSize(8, 6) // make the widget 2x1
        .withPosition(0, 0); // place it in the top-left corner
    private GenericEntry aprilTagTarget = RobotContainer.autoTab
        .add("Currently Seeing April Tag", false).withWidget(BuiltInWidgets.kBooleanBox)
        .withProperties(Map.of("Color when true", "green", "Color when false", "red"))
        .withPosition(8, 4).withSize(2, 2).getEntry();

    /**
     * Initializes swerve modules.
     */
    public Swerve() {
        swerveMods = new SwerveModule[] {new SwerveModule(0, Constants.Swerve.Mod0.constants),
            new SwerveModule(1, Constants.Swerve.Mod1.constants),
            new SwerveModule(2, Constants.Swerve.Mod2.constants),
            new SwerveModule(3, Constants.Swerve.Mod3.constants)};

        swerveOdometry = new SwerveDrivePoseEstimator(Constants.Swerve.SWERVE_KINEMATICS, getYaw(),
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
        this.setMotorsZero();
    }

    /**
     * Moves the swerve drive train, creates twist2d that accounts for the fact that positions are
     * not updated continuously, (updated every .02 seconds.)
     *
     * @param translation The 2d translation in the X-Y plane
     * @param rotation The amount of rotation in the Z axis
     * @param fieldRelative Whether the movement is relative to the field or absolute
     * @param isOpenLoop Open or closed loop system
     */
    public void driveWithTwist(Translation2d translation, double rotation, boolean fieldRelative,
        boolean isOpenLoop) {

        double dt = TimedRobot.kDefaultPeriod;

        ChassisSpeeds chassisSpeeds = fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(translation.getX(), translation.getY(),
                rotation, getFieldRelativeHeading())
            : new ChassisSpeeds(translation.getX(), translation.getY(), rotation);

        Pose2d robot_pose_vel =
            new Pose2d(chassisSpeeds.vxMetersPerSecond * dt, chassisSpeeds.vyMetersPerSecond * dt,
                Rotation2d.fromRadians(chassisSpeeds.omegaRadiansPerSecond * dt));
        Twist2d twist_vel = new Pose2d().log(robot_pose_vel);
        ChassisSpeeds updated =
            new ChassisSpeeds(twist_vel.dx / dt, twist_vel.dy / dt, twist_vel.dtheta / dt);

        setModuleStates(updated);
    }

    /**
     * Sets motors to 0 or inactive.
     */
    public void setMotorsZero() {
        setModuleStates(new ChassisSpeeds(0, 0, 0));
    }

    /**
     * Used by SwerveControllerCommand in Auto
     *
     * @param desiredStates The desired states of the swerve modules
     */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.Swerve.MAX_SPEED);

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
            Constants.Swerve.SWERVE_KINEMATICS.toSwerveModuleStates(chassisSpeeds);
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
        fieldOffset = getYaw().getDegrees();
    }

    public Rotation2d getFieldRelativeHeading() {
        return Rotation2d.fromDegrees(getYaw().getDegrees() - fieldOffset);
    }

    /**
     * Gets the rotation degree from swerve modules.
     */
    public Rotation2d getYaw() {
        float yaw = gyro.getYaw();
        return (Constants.Swerve.INVERT_GYRO) ? Rotation2d.fromDegrees(-yaw)
            : Rotation2d.fromDegrees(yaw);
    }

    /**
     * Gets the roll (pitch) degree from swerve modules.
     */
    public float getRoll() {
        return gyro.getRoll();
    }

    @Override
    public void periodic() {
        Rotation2d yaw = getYaw();
        swerveOdometry.update(yaw, getPositions());
        var res = cam.getLatestResult();
        if (res.hasTargets()) {
            var imageCaptureTime = res.getTimestampSeconds();
            if (!hasInitialized || DriverStation.isDisabled()) {
                var target = res.getBestTarget();
                var camToTargetTrans = target.getBestCameraToTarget();
                var aprilTagPose = FieldConstants.aprilTags.get(target.getFiducialId());
                if (aprilTagPose != null) {
                    var camPose = aprilTagPose.transformBy(camToTargetTrans.inverse());
                    var robotPose =
                        camPose.transformBy(Constants.CameraConstants.KCAMERA_TO_ROBOT).toPose2d();
                    swerveOdometry.resetPosition(getYaw(), getPositions(), robotPose);
                    SmartDashboard.putNumberArray("Initial Position",
                        new double[] {robotPose.getX(), robotPose.getY()});
                    hasInitialized = true;
                }

            }
            var pose2dList = new ArrayList<Pose2d>();
            var target = res.getBestTarget();

            var camToTargetTrans = target.getBestCameraToTarget();
            var aprilTagPose = FieldConstants.aprilTags.get(target.getFiducialId());
            if (aprilTagPose != null) {
                var camPose = aprilTagPose.transformBy(camToTargetTrans.inverse());
                var robotPose =
                    camPose.transformBy(Constants.CameraConstants.KCAMERA_TO_ROBOT).toPose2d();
                pose2dList.add(robotPose);
                if (robotPose.minus(getPose()).getTranslation()
                    .getNorm() < Constants.CameraConstants.LARGEST_DISTANCE) {
                    swerveOdometry.addVisionMeasurement(robotPose, imageCaptureTime,
                        VecBuilder.fill(Constants.SwerveTransformPID.STD_DEV_MOD / target.getArea(),
                            Constants.SwerveTransformPID.STD_DEV_MOD / target.getArea(),
                            Constants.SwerveTransformPID.STD_DEV_MOD / target.getArea()));
                }
            }
        }

        field.setRobotPose(getPose());

        aprilTagTarget.setBoolean(res.hasTargets());

        SmartDashboard.putBoolean("Has Initialized", hasInitialized);
        SmartDashboard.putNumber("Robot X", getPose().getX());
        SmartDashboard.putNumber("Robot Y", getPose().getY());
        SmartDashboard.putNumber("Robot Rotation", getPose().getRotation().getDegrees());
        SmartDashboard.putNumber("Gyro Yaw", yaw.getDegrees());
        SmartDashboard.putNumber("Field Offset", fieldOffset);
        SmartDashboard.putNumber("Gyro Yaw - Offset", getFieldRelativeHeading().getDegrees());
        SmartDashboard.putNumber("Gyro roll", getRoll());


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

    /**
     * Reset April Tag position initialization
     */
    public void resetInitialized() {
        this.hasInitialized = false;
    }
}
