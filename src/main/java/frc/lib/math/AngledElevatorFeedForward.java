package frc.lib.math;

import edu.wpi.first.math.controller.ElevatorFeedforward;

/**
 * Class with methods for creating elevator feedforward.
 */
public class AngledElevatorFeedForward extends ElevatorFeedforward {

    public AngledElevatorFeedForward(double ks, double kg, double kv) {
        super(ks, kg, kv);
    }


    public double calculate(double positionRadians, double velocity, double acceleration) {
        return ks * Math.signum(velocity) + kg * Math.sin(positionRadians) + kv * velocity
            + ka * acceleration;
    }

    public double calculate(double positionRadians, double velocity) {
        return calculate(positionRadians, velocity, 0);
    }
}
