package frc.lib.util;

public class ArmPosition {
    double armAngle;
    double elevatorPosition;
    double wristAngle = -10;

    public ArmPosition(double armAngle, double elevatorPosition, double wristAngle) {
        this.armAngle = armAngle;
        this.elevatorPosition = elevatorPosition;
        this.wristAngle = wristAngle;
    }

    public ArmPosition(double armAngle, double elevatorPosition) {
        this(armAngle, elevatorPosition, 0);
    }

    public ArmPosition(double armAngle) {
        this(armAngle, 0, 0);
    }

    public double getArmAngle() {
        return armAngle;
    }

    public double getElevatorPosition() {
        return elevatorPosition;
    }

    public double getWristAngle() {
        return wristAngle;
    }

}
