package frc.robot.commands.wrist;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.WristIntake;

public class VariableIntake extends CommandBase {
    private WristIntake intake;
    private CommandXboxController controller;

    public VariableIntake(WristIntake intake, CommandXboxController controller) {
        this.intake = intake;
        this.controller = controller;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.setMotor(controller.getLeftY());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
