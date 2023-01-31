package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

public class armMoving extends CommandBase {
    private Arm arm;

    public armMoving(Arm arm) {
        this.arm = arm;
    }

    public void goTo2ndPosition() {
        double angle = arm.getAngleMeasurement();
        arm.Arm2ndPosition();
    }
}
