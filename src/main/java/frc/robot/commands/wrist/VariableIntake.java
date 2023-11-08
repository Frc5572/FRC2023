package frc.robot.commands.wrist;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.lib.util.KeyboardAndMouse;
import frc.robot.subsystems.WristIntake;

/**
 * Variable controlled intake speed.
 */
public class VariableIntake extends CommandBase {
    private WristIntake intake;
    private CommandXboxController controller;
    private Trigger intakeIn;
    private Trigger intakeOut;

    /**
     * Variable intake constructor.
     *
     * @param intake intake
     * @param controller controller
     */
    public VariableIntake(WristIntake intake, CommandXboxController controller) {
        this.intake = intake;
        this.controller = controller;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(intake);
        intakeIn = KeyboardAndMouse.getInstance().mouse(0);
        intakeOut = KeyboardAndMouse.getInstance().mouse(2);
    }

    @Override
    public void execute() {
        double intake_in = intakeIn.getAsBoolean() ? 1.0 : 0.0;
        double intake_out = intakeOut.getAsBoolean() ? 1.0 : 0.0;
        intake.setMotor((intake_in - intake_out) * 0.4);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
