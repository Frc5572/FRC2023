package frc.lib.math;

import org.ejml.data.DMatrix3x3;
import org.ejml.dense.fixed.CommonOps_DDF3;

public class GyroParams {

    /** Gyro white noise from spec. sheet, in (deg/s)^2/Hz */
    public final double Sg;

    /** Error covariance matrix for gyro noise, in (rad/s)^2 */
    public final DMatrix3x3 Qg;

    /** Gyro bias time constant, in seconds */
    public final double taug;

    /** Gyro bias from spec. sheet, in (deg/h) */
    public final double sigmag;

    /** Error covariance matrix for gyro bias noise, in (rad/s)^2 */
    public final DMatrix3x3 Qg2;

    private static final double square(double x) {
        return x * x;
    }

    public GyroParams(double Sg, double taug, double sigmag, double imuDelt) {
        this.Sg = Sg;

        // (pi/180)^2 * (sensorParams.Sg/sensorParams.IMUdelt)*eye(3);
        double tmp = square(Math.PI / 180.0) * (Sg / imuDelt);
        DMatrix3x3 tmp2 = new DMatrix3x3();
        CommonOps_DDF3.setIdentity(tmp2);
        CommonOps_DDF3.scale(tmp, tmp2);
        this.Qg = tmp2;

        this.taug = taug;
        this.sigmag = sigmag;

        // exp(-sensorParams.IMUdelt/sensorParams.taug);
        double alphag = Math.exp(-imuDelt / taug);

        // (pi/(3600*180))^2*(sensorParams.sigmag)^2*(1-sensorParams.alphag^2)*eye(3);
        double tmp3 = square(Math.PI / (3600.0 * 180.0)) * square(sigmag) * (1.0 - square(alphag));
        DMatrix3x3 tmp4 = new DMatrix3x3();
        CommonOps_DDF3.setIdentity(tmp4);
        CommonOps_DDF3.scale(tmp3, tmp4);
        this.Qg2 = tmp4;
    }
}
