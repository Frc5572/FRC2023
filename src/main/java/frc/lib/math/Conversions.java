package frc.lib.math;

import edu.wpi.first.math.MathUtil;

/**
 * Static Conversion functions
 */
public class Conversions {

    /**
     * @param wheelRPS Wheel Velocity: (in Rotations per Second)
     * @param circumference Wheel Circumference: (in Meters)
     * @return Wheel Velocity: (in Meters per Second)
     */
    public static double RPSToMPS(double wheelRPS, double circumference) {
        double wheelMPS = wheelRPS * circumference;
        return wheelMPS;
    }

    /**
     * @param wheelMPS Wheel Velocity: (in Meters per Second)
     * @param circumference Wheel Circumference: (in Meters)
     * @return Wheel Velocity: (in Rotations per Second)
     */
    public static double MPSToRPS(double wheelMPS, double circumference) {
        double wheelRPS = wheelMPS / circumference;
        return wheelRPS;
    }

    /**
     * @param wheelRotations Wheel Position: (in Rotations)
     * @param circumference Wheel Circumference: (in Meters)
     * @return Wheel Distance: (in Meters)
     */
    public static double rotationsToMeters(double wheelRotations, double circumference) {
        double wheelMeters = wheelRotations * circumference;
        return wheelMeters;
    }

    /**
     * @param wheelMeters Wheel Distance: (in Meters)
     * @param circumference Wheel Circumference: (in Meters)
     * @return Wheel Position: (in Rotations)
     */
    public static double metersToRotations(double wheelMeters, double circumference) {
        double wheelRotations = wheelMeters / circumference;
        return wheelRotations;
    }

    /**
     * Apply a quadratic curve to swerve inputs.
     *
     * @param input Controller input
     * @param deadband Controller deadband
     * @return New curved input.
     */
    public static double applySwerveCurve(double input, double deadband) {
        // For 0.2 Joystick Deadband:
        // f(x)=0.625*x^2+0.5*x+-0.125
        // For 0.1 Joystick Deadband:
        // f(x)=0.778*x^2+0.256*x+-0.033

        double processedInput = 0.0;

        if (Math.abs(input) >= deadband) {
            processedInput =
                (0.778 * Math.pow(Math.abs(input), 2) + 0.256 * Math.abs(input) - 0.033);
            processedInput = Math.copySign(processedInput, input);
        }
        return MathUtil.clamp(processedInput, -1, 1);
    }

    /**
     * Generate a random number between and upper and lower bound
     *
     * @param upper The max value
     * @param lower the min value
     * @return The random integer
     */
    public static int random(int upper, int lower) {
        return (int) (Math.random() * (upper - lower)) + lower;
    }

    /**
     * Generate a random number between and upper value and 0
     *
     * @param upper The max value
     * @return The random integer
     */
    public static int random(int upper) {
        return random(upper, 0);
    }
}
