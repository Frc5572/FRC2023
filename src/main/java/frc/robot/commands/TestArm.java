package frc.robot.commands;

import java.util.Map;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.Arm;

/**
 * Test April tag transform
 */
public class TestArm extends CommandBase {

    Arm arm;
    String gamePiece = "";

    public TestArm(Arm arm) {
        this.arm = arm;
    }

    @Override
    public void initialize() {
        SmartDashboard.putNumber("Targeted Level", Robot.level);
        SmartDashboard.putNumber("Targeted Column", Robot.column);
        if (Robot.column == 1 || Robot.column == 4 || Robot.column == 7) {
            gamePiece = "CUBE";
        } else {
            gamePiece = "CONE";
        }
        SmartDashboard.putString("Targeted Game Piece", gamePiece);
        Map<Integer, Double> armExtension = Map.of(0, 1.0, 1, 1.0, 2, 1.0);
        Map<Integer, Double> armAngle = Map.of(0, 45.0, 1, 90.0, 2, 100.0);
        arm.enablePID();
        arm.setGoal(armAngle.get(Robot.level));
        // elevator.enablePID()
        // elevator.setGoal(armExtension.get(Robot.level));
    }

    @Override
    public void execute() {}

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            // arm.setGoal(arm.getAngleMeasurement2());
            // Set elevator to stop and hold
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
