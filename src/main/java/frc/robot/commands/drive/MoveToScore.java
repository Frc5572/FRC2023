package frc.robot.commands.drive;

import java.util.Map;
import java.util.function.Supplier;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.lib.util.FieldConstants;
import frc.robot.Robot;
import frc.robot.subsystems.Swerve;


/**
 * Move into scoring position
 */
public class MoveToScore extends SequentialCommandGroup {

    /**
     * Move into scoring position
     */
    public MoveToScore(Swerve swerve) {
        Supplier<Pose2d> supplier = () -> {
            int column = Robot.column;
            Pose2d basePos = new Pose2d();
            double xPosition = Units.inchesToMeters(50);
            Rotation2d rotation = Rotation2d.fromDegrees(180);
            if (DriverStation.getAlliance() == Alliance.Blue) {
                basePos = FieldConstants.aprilTags.get(8).toPose2d();
            } else if (DriverStation.getAlliance() == Alliance.Red) {
                basePos = FieldConstants.aprilTags.get(3).toPose2d();
            }
            Map<Integer, Translation2d> columns =
                Map.of(0, new Translation2d(xPosition, FieldConstants.Grids.nodeSeparationY * -1),
                    1, new Translation2d(xPosition, FieldConstants.Grids.nodeSeparationY * 0), 2,
                    new Translation2d(xPosition, FieldConstants.Grids.nodeSeparationY * 1), 3,
                    new Translation2d(xPosition, FieldConstants.Grids.nodeSeparationY * 2), 4,
                    new Translation2d(xPosition, FieldConstants.Grids.nodeSeparationY * 3), 5,
                    new Translation2d(xPosition, FieldConstants.Grids.nodeSeparationY * 4), 6,
                    new Translation2d(xPosition, FieldConstants.Grids.nodeSeparationY * 5), 7,
                    new Translation2d(xPosition, FieldConstants.Grids.nodeSeparationY * 6), 8,
                    new Translation2d(xPosition, FieldConstants.Grids.nodeSeparationY * 7));
            return basePos.plus(new Transform2d(columns.get(column), rotation));
        };

        MoveToPos moveToPos = new MoveToPos(swerve, supplier);
        MoveToScoreFinal moveToScoreFinal = new MoveToScoreFinal(swerve);
        addCommands(moveToPos, moveToScoreFinal);
    }
}
