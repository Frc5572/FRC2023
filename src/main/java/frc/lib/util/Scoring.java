package frc.lib.util;

import java.util.Map;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class Scoring {


    public static enum GamePiece {
        CONE, CUBE
    }

    public static GamePiece getGamePiece() {
        return (Robot.column == 1 || Robot.column == 4 || Robot.column == 7) ? GamePiece.CUBE
            : GamePiece.CUBE;
    }

    public static double getScorePosition() {
        GamePiece gamePiece = getGamePiece();
        Map<Integer, Double> xCoord = Map.of();
        if (gamePiece == GamePiece.CUBE) {
            xCoord = Map.of(0, 32.0, 1, 49.5, 2, 40.5);
        } else if (gamePiece == GamePiece.CONE) {
            xCoord = Map.of(0, 41.0, 1, 37.0, 2, 32.0);
        }
        return xCoord.get(Robot.level);
    }

    public static ArmPosition getScoreParameters() {
        GamePiece gamePiece = getGamePiece();
        Map<Integer, Double> armExtensionValues = Map.of();
        Map<Integer, Double> armAngleValues = Map.of();
        // SmartDashboard.putNumber("Targeted Level", Robot.level);
        // SmartDashboard.putNumber("Targeted Column", Robot.column);
        SmartDashboard.putString("Targeted Game Piece", gamePiece.toString());

        if (gamePiece == GamePiece.CUBE) {
            armExtensionValues = Map.of(0, 0.0, 1, 0.0, 2, 0.0);
            armAngleValues = Map.of(0, 20.0, 1, 90.0, 2, 90.0);
        } else if (gamePiece == GamePiece.CONE) {
            armExtensionValues = Map.of(0, 0.0, 1, 0.0, 2, 0.0);
            armAngleValues = Map.of(0, 45.0, 1, 100.0, 2, 110.0);
        }
        return new ArmPosition(armAngleValues.get(Robot.level),
            armExtensionValues.get(Robot.level));
    }
}
