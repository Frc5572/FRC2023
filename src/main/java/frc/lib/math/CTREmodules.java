package frc.lib.math;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class CTREmodules {
    /**
     * Minimize the change in heading the desired swerve module state would require by potentially
     * reversing the direction the wheel spins. Customized from WPILib's version to include placing
     * in appropriate scope for CTRE onboard control.
     *
     * @param desiredState The desired state.
     * @param currentAngle The current module angle.
     */
    public static SwerveModuleState optimize(SwerveModuleState DesiredState,
        Rotation2d currentAngle) {
        double targetAngle =
            placeInAppropriate(currentAngle.getDegrees(), DesiredState.angle.getDegrees());
        double targetSpeed = DesiredState.speedMetersPerSecond;
        double delta = targetAngle - currentAngle.getDegrees();
        if (Math.abs(delta) > 90) {
            targetSpeed = -targetSpeed;
            targetAngle = delta > 90 ? (targetAngle -= 180) : (targetAngle += 180);
        }
        return new SwerveModuleState(targetSpeed, Rotation2d.fromDegrees(targetAngle));
    }

    /**
     * @param scopeReference Current Angle
     * @param newAngle Target Angle
     * @return Closest angle within scope
     */
    public static double placeInAppropriate(double referenceScope, double newAngle) {
        double lowerBound;
        double upperBound;
        double lowerOffset = referenceScope % 360;
        if (lowerOffset >= 0) {
            lowerBound = referenceScope - lowerOffset;
            upperBound = referenceScope + (360 - lowerOffset);
        } else {
            upperBound = referenceScope - lowerOffset;
            lowerBound = referenceScope - (360 + lowerOffset);
        }
        while (newAngle < lowerBound) {
            newAngle += 360;
        }
        while (newAngle > upperBound) {
            newAngle -= 360;
        }
        if (newAngle - referenceScope > 180) {
            newAngle -= 360;
        } else if (newAngle - referenceScope < -180) {
            newAngle += 360;
        }
        return newAngle;
    }
}
