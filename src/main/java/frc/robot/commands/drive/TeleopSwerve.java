package frc.robot.commands.drive;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.lib.util.KeyboardAndMouse;
import frc.lib.util.KeyboardAndMouse.LowPassKey;
import frc.lib.util.KeyboardAndMouse.WASD;
import frc.robot.Constants;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Swerve;

/**
 * Creates an command for driving the swerve drive during tele-op
 */
public class TeleopSwerve extends CommandBase {

    private boolean fieldRelative;
    private boolean openLoop;
    private Swerve swerveDrive;
    private CommandXboxController controller;
    private Arm arm;
    private WASD wasd;
    private LowPassKey shift;
    private LowPassKey ctrl;

    private double desiredAngle = 0.0;
    private ProfiledPIDController thetaController =
        new ProfiledPIDController(Constants.SwerveTransformPID.PID_TKP,
            Constants.SwerveTransformPID.PID_TKI, Constants.SwerveTransformPID.PID_TKD,
            new TrapezoidProfile.Constraints(Constants.SwerveTransformPID.MAX_ANGULAR_VELOCITY,
                Constants.SwerveTransformPID.MAX_ANGULAR_ACCELERATION));

    /**
     * Creates an command for driving the swerve drive during tele-op
     *
     * @param swerveDrive The instance of the swerve drive subsystem
     * @param fieldRelative Whether the movement is relative to the field or absolute
     * @param openLoop Open or closed loop system
     */
    public TeleopSwerve(Swerve swerveDrive, CommandXboxController controller, boolean fieldRelative,
        boolean openLoop, Arm arm) {
        KeyboardAndMouse kam = KeyboardAndMouse.getInstance();
        this.wasd = kam.wasd("w", "a", "s", "d");
        this.shift = kam.lowPassKey("shift");
        this.ctrl = kam.lowPassKey("control");
        this.swerveDrive = swerveDrive;
        addRequirements(swerveDrive);
        this.fieldRelative = fieldRelative;
        this.openLoop = openLoop;
        this.controller = controller;
        this.arm = arm;
        thetaController.enableContinuousInput(0, Units.degreesToRadians(360.0));
    }

    @Override
    public void execute() {
        double yaxis = wasd.getY() * 5.0;
        double xaxis = -wasd.getX() * 5.0;
        double raxis = -KeyboardAndMouse.getInstance().getX() * 0.005;
        desiredAngle += raxis;

        SmartDashboard.putNumber("desiredAngle", desiredAngle);

        /* Deadbands */
        // yaxis = MathUtil.applyDeadband(yaxis, Constants.STICK_DEADBAND);
        // yaxis = Conversions.applySwerveCurve(yaxis, Constants.STICK_DEADBAND);
        // xaxis = MathUtil.applyDeadband(xaxis, Constants.STICK_DEADBAND);
        // xaxis = Conversions.applySwerveCurve(xaxis, Constants.STICK_DEADBAND);
        // raxis = MathUtil.applyDeadband(raxis, Constants.STICK_DEADBAND);

        double angle_speed = Constants.Swerve.MAX_ANGULAR_VELOCITY;
        double speed = Constants.Swerve.MAX_SPEED;

        speed *= (1.0 - shift.get()) * 0.2;
        speed *= (1.0 - ctrl.get()) * 0.4;

        if (arm.getArmAngle() > -70) {
            angle_speed /= 3;
            speed *= 0.80;
        }

        thetaController.calculate(swerveDrive.getYaw().getRadians(), desiredAngle);

        Translation2d translation = new Translation2d(yaxis, xaxis).times(speed);
        double rotation = raxis * angle_speed;
        swerveDrive.driveWithTwist(translation, rotation, fieldRelative, openLoop);

    }
}
