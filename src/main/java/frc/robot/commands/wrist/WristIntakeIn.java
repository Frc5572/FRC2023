package frc.robot.commands.wrist;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.WristIntake;

/**
 * Command to raise the Drop Down Intake to the top position
 */
public class WristIntakeIn extends CommandBase {
    private WristIntake intake;
    private Timer timer;

    /**
     * Command to lower the Drop Down Intake to the bottom position
     *
     * @param intake Drop Down Intake
     */
    public WristIntakeIn(WristIntake intake) {
        this.intake = intake;
        addRequirements(intake);
        timer = new Timer();
    }

    @Override
    public void initialize() {
        timer.start();
        intake.setMotor(Constants.Wrist.INTAKE_SPEED);
    }

    @Override
    public void end(boolean interrupt) {
        intake.setMotor(Constants.Wrist.INTAKE_STOP_SPEED);
    }

    @Override
    public boolean isFinished() {
        return intake.getVoltageSpike(true);
        // return false;
    }
}
