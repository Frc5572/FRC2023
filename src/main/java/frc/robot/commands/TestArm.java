package frc.robot.commands;

import java.util.Map;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.GamePiece;
import frc.robot.Robot;
import frc.robot.subsystems.Arm;

/**
 * Test April tag transform
 */
public class TestArm extends CommandBase {

    Arm arm;
    GamePiece gamePiece;
    double armAngle;
    double armExtension;
    Map<Integer, Double> armExtensionValues;
    Map<Integer, Double> armAngleValues;

    public TestArm(Arm arm) {
        this.arm = arm;
    }

    @Override
    public void initialize() {
        SmartDashboard.putNumber("Targeted Level", Robot.level);
        SmartDashboard.putNumber("Targeted Column", Robot.column);
        if (Robot.column == 1 || Robot.column == 4 || Robot.column == 7) {
            gamePiece = GamePiece.CUBE;
        } else {
            gamePiece = GamePiece.CUBE;
        }
        SmartDashboard.putString("Targeted Game Piece", gamePiece.toString());

        if (gamePiece == GamePiece.CUBE) {
            armExtensionValues = Map.of(0, 0.0, 1, 0.0, 2, 0.0);
            armAngleValues = Map.of(0, 20.0, 1, 90.0, 2, 90.0);
        } else if (gamePiece == GamePiece.CONE) {
            armExtensionValues = Map.of(0, 0.0, 1, 0.0, 2, 0.0);
            armAngleValues = Map.of(0, 45.0, 1, 100.0, 2, 110.0);
        }

        arm.enablePID();
        arm.setArmGoal(armAngleValues.get(Robot.level));
        arm.setElevatorGoal(armExtensionValues.get(Robot.level));
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
