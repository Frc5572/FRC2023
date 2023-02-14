package frc.lib.math;

import java.util.Map;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;

/**
 * Pose Estimator incoporating pre-integrated IMU measurements, pre-integrated encoder measurements,
 * and vision landmark estimates
 */
public class CustomSwervePoseEstimator {


    private final CustomSwerveKinematics kinematics;
    private final Camera cameras[];
    private final GyroParams imuParams;
    private final Map<Integer, Pose3d> aprilTagPoses;

    public CustomSwervePoseEstimator(CustomSwerveKinematics kinematics, Camera[] cameras,
        GyroParams imuParams, Map<Integer, Pose3d> aprilTagPoses) {
        this.kinematics = kinematics;
        this.cameras = cameras;
        this.imuParams = imuParams;
        this.aprilTagPoses = aprilTagPoses;
    }

    public void update(Rotation3d theta) {
        // TODO
    }

    public Pose3d getEstimatedPose() {
        // TODO
        return null;
    }

}
