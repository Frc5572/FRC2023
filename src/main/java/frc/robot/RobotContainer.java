package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.DisabledInstantCommand;
import frc.robot.commands.TeleopSwerve;
import frc.robot.commands.TestTransform;
import frc.robot.commands.WristAlignment;
import frc.robot.commands.leds.FlashingLEDColor;
import frc.robot.commands.leds.MorseCodeFlash;
import frc.robot.commands.leds.PoliceLEDs;
import frc.robot.commands.leds.RainbowLEDs;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.Wrist;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Controllers */


    private final CommandXboxController driver = new CommandXboxController(Constants.DRIVER_ID);
    private final CommandXboxController operator = new CommandXboxController(Constants.OPERATOR_ID);

    // Initialize AutoChooser Sendable
    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    // Field Relative and openLoop Variables
    boolean fieldRelative;
    boolean openLoop;
    int ledPattern = 0;

    // Subsystems
    private LEDs leds = new LEDs(Constants.LEDConstants.LEDCount, Constants.LEDConstants.PWMPort);
    /* Subsystems */
    private final Wrist wrist = new Wrist();
    private final Swerve s_Swerve = new Swerve();
    public DigitalInput testSensor = new DigitalInput(0);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        s_Swerve.setDefaultCommand(new TeleopSwerve(s_Swerve, driver,
            Constants.Swerve.IS_FIELD_RELATIVE, Constants.Swerve.IS_OPEN_LOOP));
        // autoChooser.addOption(resnickAuto, new ResnickAuto(s_Swerve));
        SmartDashboard.putData("Choose Auto: ", autoChooser);
        autoChooser.setDefaultOption("Do Nothing", new WaitCommand(1));
        // Configure the button bindings
        configureButtonBindings();

    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses
     * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
     * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        operator.y().whileTrue(new WristAlignment(wrist, 0));

        /* Driver Buttons */
        driver.y().onTrue(new InstantCommand(() -> s_Swerve.resetFieldRelativeOffset()));
        driver.x().whileTrue(new TestTransform(s_Swerve,
            new Transform2d(new Translation2d(1, 0), Rotation2d.fromDegrees(180)), 6));
        driver.a().onTrue(new InstantCommand(() -> s_Swerve.resetInitialized()));
        driver.rightTrigger().whileTrue(new RainbowLEDs(leds));
        driver.leftTrigger().whileTrue(new PoliceLEDs(leds));
        driver.start().onTrue(new DisabledInstantCommand(() -> this.ledPattern = 0));
        driver.povDown().onTrue(new DisabledInstantCommand(() -> this.ledPattern = 1));
        driver.povRight().onTrue(new DisabledInstantCommand(() -> this.ledPattern = 2));
        driver.povLeft().onTrue(new DisabledInstantCommand(() -> this.ledPattern = 3));

        /* Operator Buttons */
        operator.leftTrigger().onTrue(new FlashingLEDColor(leds, Color.kYellow)
            .until(() -> this.testSensor.get()).withTimeout(5.0));
        operator.rightTrigger().onTrue(new FlashingLEDColor(leds, Color.kPurple)
            .until(() -> this.testSensor.get()).withTimeout(5.0));

        /* Triggers */
        Trigger grabbedGamePiece = new Trigger(() -> this.testSensor.get());
        new Trigger(() -> this.ledPattern == 1).whileTrue(new RainbowLEDs(leds));
        new Trigger(() -> this.ledPattern == 2).whileTrue(new PoliceLEDs(leds));
        new Trigger(() -> this.ledPattern == 3)
            .whileTrue(new FlashingLEDColor(leds, Color.kGhostWhite, Color.kGreen));
        grabbedGamePiece.whileTrue(
            new DisabledInstantCommand(() -> leds.setColor(Color.kGreen), leds).repeatedly());
        grabbedGamePiece.negate().whileTrue(new FlashingLEDColor(leds, Color.kBlue).withTimeout(3));
        operator.povDown().whileTrue(new MorseCodeFlash(leds, "ROSBOTS"));
    }

    /**
     * Gets the user's selected autonomous command.
     *
     * @return Returns autonomous command selected.
     */
    public Command getAutonomousCommand() {
        // return new P1_3B(swerveDrive, shooter, innerMagazine, outerMagazine, intake, turret,
        // vision);
        return autoChooser.getSelected();
    }
}
