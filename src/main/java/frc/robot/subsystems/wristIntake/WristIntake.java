package frc.robot.subsystems.wristIntake;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Creates the subsystem for the wrist intake.
 */
public class WristIntake extends SubsystemBase {

    private boolean invertForCone = true;
    private WristIntakeIOFalcon500 io;

    private WristIntakeInputsAutoLogged wristIntakeAutoLogged = new WristIntakeInputsAutoLogged();


    /**
     * Create Wrist Intake Subsystem
     */
    public WristIntake() {
        io = new WristIntakeIOFalcon500();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Wrist Intake Current", io.getOutputCurrentAmps());
    }

    // Runs wrist intake motor to intake a game piece.
    public void intake() {
        io.setMotorPercentage(Constants.Wrist.INTAKE_SPEED);
    }

    /**
     * Set power of intake motors
     *
     * @param power power of motors from -1 to 1
     */
    public void setMotor(double power) {
        io.setMotorPercentage((invertForCone ? -1 : 1) * power);
    }

    /**
     * Set power of intake motors ignoring direction
     *
     * @param power power of motors from -1 to 1
     */
    public void setMotorRaw(double power) {
        io.setMotorPercentage(power);
    }

    /**
     * Releases GPs in intake (if any). Primarily used to prevent the accidental holding of multiple
     * GPs penalty.
     */
    public void panic() {
        setMotor(Constants.Wrist.INTAKE_PANIC_SPEED);
    }

    /*
     * Get if motor is stalling by checking if the stator current exceeds a constant
     */
    public boolean getVoltageSpike(boolean passedTime) {
        return io.getOutputCurrentAmps() > Constants.Wrist.STALL_CURRENT;
    }

    public void setInvert(boolean invert) {
        invertForCone = invert;
    }
}
