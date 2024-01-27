package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Creates the subsystem for the wrist intake.
 */
public class WristIntake extends SubsystemBase {

    private TalonFXConfiguration wristMotorConfig = new TalonFXConfiguration();
    private final TalonFX wristIntakeMotor = new TalonFX(Constants.Wrist.WRIST_INTAKE_ID);

    private boolean invertForCone = true;

    /**
     * Create Wrist Intake Subsystem
     */
    public WristIntake() {
        wristIntakeMotor.setInverted(false);
        wristMotorConfig.MotorOutput.NeutralMode = Constants.Swerve.driveNeutralMode;
        wristIntakeMotor.getConfigurator().apply(wristMotorConfig);

    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Wrist Intake Current",
            wristIntakeMotor.getStatorCurrent().getValueAsDouble());
    }

    // Runs wrist intake motor to intake a game piece.
    public void intake() {
        wristIntakeMotor.set(Constants.Wrist.INTAKE_SPEED);
    }

    /**
     * Set power of intake motors
     *
     * @param power power of motors from -1 to 1
     */
    public void setMotor(double power) {
        wristIntakeMotor.set((invertForCone ? -1 : 1) * power);
    }

    /**
     * Set power of intake motors ignoring direction
     *
     * @param power power of motors from -1 to 1
     */
    public void setMotorRaw(double power) {
        wristIntakeMotor.set(power);
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
        return wristIntakeMotor.getStatorCurrent()
            .getValueAsDouble() > Constants.Wrist.STALL_CURRENT;
    }

    public void setInvert(boolean invert) {
        invertForCone = invert;
    }
}
