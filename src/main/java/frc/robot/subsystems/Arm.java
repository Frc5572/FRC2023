package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.PIDSubsystem;

public class Arm {
    public class Arm extends PIDSubsystem {
    private final WPI_TalonFX  = new WPI_TalonFX(Constants.Motors.motorID, "amog");
    public Arm(){
        super (new PIDContorller());
    }
}
