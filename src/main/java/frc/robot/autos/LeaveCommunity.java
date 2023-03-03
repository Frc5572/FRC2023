package frc.robot.autos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.commands.drive.MoveToPos;
import frc.robot.subsystems.Swerve;

/**
 * Leaves community area.
 */
public class LeaveCommunity extends MoveToPos {

    public static double distanceFromTape = 230.0;

    /**
     * Runs MoveToPos to leave community area.
     * 
     * @param swerve
     */
    public LeaveCommunity(Swerve swerve) {
        super(swerve,
            () -> new Pose2d(
                new Translation2d(Units.inchesToMeters(distanceFromTape), swerve.getPose().getY()),
                swerve.getPose().getRotation()));
    }
}
