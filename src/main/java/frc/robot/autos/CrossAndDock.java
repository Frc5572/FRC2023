package frc.robot.autos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.lib.util.FieldConstants;
import frc.robot.commands.drive.MoveToPos;
import frc.robot.subsystems.Swerve;

/**
 * Leaves community, and docks.
 */
public class CrossAndDock extends SequentialCommandGroup {


    /**
     * Sequential command group to leave community area, and dock.
     * 
     * @param swerve
     */
    public CrossAndDock(Swerve swerve) {
        LeaveCommunity leaveCommunity = new LeaveCommunity(swerve);
        MoveToPos centerWithDock = new MoveToPos(swerve,
            new Pose2d(
                new Translation2d(Units.inchesToMeters(LeaveCommunity.distanceFromTape),
                    FieldConstants.Grids.lowTranslations[4].getY()),
                swerve.getPose().getRotation()));


        addCommands(leaveCommunity, centerWithDock);
    }
}
