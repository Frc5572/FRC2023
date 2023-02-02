package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

public class armMoving extends CommandBase {
    private Arm arm;
    private double angle;

    public armMoving(Arm arm) {
        this.arm = arm;
    }

    public void goHome() {
        angle = arm.getAngleMeasurement();
        arm.armAtHome();
    }

    public void goTo2ndPosition() {
        angle = arm.getAngleMeasurement();
        arm.Arm2ndPosition();
    }

    public void goTo3rdPosition() {
        angle = arm.getAngleMeasurement();
        arm.ArmThirdPosition();
    }

    public void goTo4thPosition() {
        angle = arm.getAngleMeasurement();
        arm.ArmFourthPosition();
    }

    public void goTo5thPosition() {
        angle = arm.getAngleMeasurement();
        arm.ArmFifthPosition();
    }
}
