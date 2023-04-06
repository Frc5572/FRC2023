package frc.robot.commands.arm;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Arm;

public class VariableArm extends CommandBase {

    private Arm arm;
    private CommandXboxController controller;

    public VariableArm(Arm arm, CommandXboxController controller) {
        this.arm = arm;
        this.controller = controller;
    }

    @Override
    public void execute() {
        double controllerValue = controller.getRightY();
        arm.adjust(-(0.02 * 0.5 * controllerValue));
    }
}
