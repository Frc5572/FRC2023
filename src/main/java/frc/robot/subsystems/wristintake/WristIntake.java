package frc.robot.subsystems.wristintake;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class WristIntake implements Subsystem {

    private WristIntakeIO io;
    private WristIntakeInputsAutoLogged inputs = new WristIntakeInputsAutoLogged();

    public WristIntake(WristIntakeIO io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("WristIntake", inputs);
    }

}
