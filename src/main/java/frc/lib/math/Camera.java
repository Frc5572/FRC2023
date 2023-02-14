package frc.lib.math;

import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;
import org.ejml.data.DMatrix2;
import org.ejml.data.DMatrix2x2;
import org.ejml.data.DMatrix3x3;
import org.ejml.dense.fixed.CommonOps_DDF2;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.TargetCorner;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Camera {

    /** Pose of camera with respect to the Robot Frame */
    public final Pose3d poseRobotFrame;

    /** Photonvision camera internals */
    private final PhotonCamera photonCamera;

    /** Pixel size, in meters */
    public final double pixelSize;

    /** 2x2 error covariance matrix for the Gaussian image noise, in pixels^2 */
    public final DMatrix2x2 Rc;

    /** Distance of the image plane along the camera z-axis, in meters */
    public final double f;

    /** Camera intrinsic matrix */
    public final DMatrix3x3 K;

    /** Image plane size (i.e., width and height), in meters along the camera x and y axes */
    public final DMatrix2 imagePlaneSize;


    private static final double square(double x) {
        return x * x;
    }

    public Camera(NetworkTableInstance inst, String name, Translation3d rc, Rotation3d RCB,
        double stdDev, double pixelSize, double f, int resoW, int resoH) {
        this.poseRobotFrame = new Pose3d(rc, RCB);
        this.photonCamera = new PhotonCamera(inst, name);

        // diag([20^2,20^2]);
        double tmp = square(stdDev);
        DMatrix2x2 tmp2 = new DMatrix2x2();
        CommonOps_DDF2.setIdentity(tmp2);
        CommonOps_DDF2.scale(tmp, tmp2);
        this.Rc = tmp2;

        this.pixelSize = pixelSize;
        this.f = f;

        // diag([sensorParams.f,sensorParams.f,1]);
        this.K = new DMatrix3x3(f, 0, 0, 0, f, 0, 0, 0, 1.0);

        // sensorParams.pixelSize * [resoW, resoH]
        this.imagePlaneSize = new DMatrix2(pixelSize * resoW, pixelSize * resoH);
    }

    public static class TargetCorners {
        public double time;
        public List<TargetCorner> corners;
        public int id;

        public TargetCorners(double time, List<TargetCorner> corners, int id) {
            this.time = time;
            this.corners = corners;
            this.id = id;
        }
    }

    public AbstractMap.SimpleEntry<Double, List<TargetCorners>> aprilTagBearings() {
        var res = photonCamera.getLatestResult();
        var corners = res.targets.stream().map((x) -> {
            return new TargetCorners(res.getTimestampSeconds(), x.getDetectedCorners(),
                x.getFiducialId());
        }).collect(Collectors.toList());
        var time = res.getTimestampSeconds();
        return new AbstractMap.SimpleEntry<Double, List<TargetCorners>>(time, corners);
    }

}
