package frc.robot.commands.arm;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.lib.util.KeyboardAndMouse;
import frc.robot.subsystems.Arm;

/**
 * Command to move the arm slightly
 */
public class VariableArm extends CommandBase {

    private Arm arm;
    private CommandXboxController controller;

    /**
     * Command to move the arm slightly
     *
     * @param arm Arm Subsystem
     * @param controller Opeprator Controller
     */
    public VariableArm(Arm arm, CommandXboxController controller) {
        this.arm = arm;
        this.controller = controller;
    }

    @Override
    public void execute() {
        double raxis = KeyboardAndMouse.getInstance().getY() * 0.01;
        arm.adjust(-(0.02 * 0.5 * raxis));
    }
}
