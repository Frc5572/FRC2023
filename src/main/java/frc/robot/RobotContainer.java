// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Map;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.DisabledInstantCommand;
import frc.robot.commands.MoveToScore;
import frc.robot.commands.TeleopSwerve;
import frc.robot.commands.TestArm;
import frc.robot.commands.arm.ArmMoving;
import frc.robot.commands.dropintake.MoveDDIntake;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DropIntake;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.WristIntake;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Shuffleboard */
    public static ShuffleboardTab mainDriverTab = Shuffleboard.getTab("Main Driver");
    private ShuffleboardLayout targetGrid =
        RobotContainer.mainDriverTab.getLayout("Next Position", BuiltInLayouts.kGrid)
            .withPosition(8, 2).withSize(2, 2).withProperties(
                Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));
    public GenericEntry levelWidget = targetGrid.add("Level", Robot.level)
        .withWidget(BuiltInWidgets.kNumberBar).withProperties(Map.of("Min", 0, "Max", 2, "Center",
            0, "Num tick marks", 3, "Show Text", false, "Orientation", "VERTICAL"))
        .getEntry();
    public GenericEntry columnWidet = targetGrid.add("Column", Robot.column)
        .withWidget(BuiltInWidgets.kNumberBar).withProperties(Map.of("Min", 0, "Max", 8, "Center",
            0, "Num tick marks", 5, "Show Text", false, "Orientation", "VERTICAL"))
        .getEntry();

    /* Controllers */
    private final CommandXboxController driver = new CommandXboxController(Constants.DRIVER_ID);
    private final CommandXboxController operator = new CommandXboxController(Constants.OPERATOR_ID);

    // Initialize AutoChooser Sendable
    private final SendableChooser<Command> autoChooser = new SendableChooser<>();
    private final PneumaticHub ph = new PneumaticHub();

    // Field Relative and openLoop Variables
    boolean fieldRelative;
    boolean openLoop;
    int ledPattern = 0;

    // Subsystems
    private LEDs leds = new LEDs(Constants.LEDConstants.LED_COUNT, Constants.LEDConstants.PWM_PORT);
    /* Subsystems */
    private final Swerve s_Swerve = new Swerve();
    private final DropIntake dIntake = new DropIntake();
    private final Arm s_Arm = new Arm();
    private final WristIntake wrist = new WristIntake(ph);
    // public DigitalInput testSensor = new DigitalInput(0);

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        ph.enableCompressorAnalog(90, 120);
        s_Swerve.setDefaultCommand(new TeleopSwerve(s_Swerve, driver,
            Constants.Swerve.IS_FIELD_RELATIVE, Constants.Swerve.IS_OPEN_LOOP));
        // autoChooser.addOption(resnickAuto, new ResnickAuto(s_Swerve));
        SmartDashboard.putData("Choose Auto: ", autoChooser);
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
        /* Driver Buttons */
        // driver.y().onTrue(new InstantCommand(() -> s_Swerve.resetFieldRelativeOffset()));
        // driver.x().whileTrue(new TestTransform(s_Swerve,
        // new Transform2d(new Translation2d(1, 0), Rotation2d.fromDegrees(180)), 6));
        // driver.a().onTrue(new InstantCommand(() -> s_Swerve.resetInitialized()));
        // driver.rightTrigger().whileTrue(new RainbowLEDs(leds));
        // driver.leftTrigger().whileTrue(new PoliceLEDs(leds));
        // driver.start().onTrue(new DisabledInstantCommand(() -> this.ledPattern = 0));
        // driver.povDown().onTrue(new DisabledInstantCommand(() -> this.ledPattern = 1));
        // driver.povRight().onTrue(new DisabledInstantCommand(() -> this.ledPattern = 2));
        // driver.povLeft().onTrue(new DisabledInstantCommand(() -> this.ledPattern = 3));

        // /* Operator Buttons */
        // operator.leftTrigger().onTrue(new FlashingLEDColor(leds, Color.kYellow)
        // .until(() -> this.testSensor.get()).withTimeout(5.0));
        // operator.rightTrigger().onTrue(new FlashingLEDColor(leds, Color.kPurple)
        // .until(() -> this.testSensor.get()).withTimeout(5.0));

        /* Triggers */
        // Trigger grabbedGamePiece = new Trigger(() -> this.testSensor.get());
        // new Trigger(() -> this.ledPattern == 1).whileTrue(new RainbowLEDs(leds));
        // new Trigger(() -> this.ledPattern == 2).whileTrue(new PoliceLEDs(leds));
        // new Trigger(() -> this.ledPattern == 3)
        // .whileTrue(new FlashingLEDColor(leds, Color.kGhostWhite, Color.kGreen));
        // grabbedGamePiece.whileTrue(
        // new DisabledInstantCommand(() -> leds.setColor(Color.kGreen), leds).repeatedly());
        // grabbedGamePiece.negate().whileTrue(new FlashingLEDColor(leds,
        // Color.kBlue).withTimeout(3));
        // operator.povDown().whileTrue(new MorseCodeFlash(leds, "ROSBOTS"));

        driver.y().onTrue(new InstantCommand(
            () -> SmartDashboard.putString(" .get ABS: ", dIntake.getAngleMeasurement() + " ")));

        driver.b().whileTrue(new MoveDDIntake(dIntake, dIntake.position1));
        driver.a().whileTrue(new MoveDDIntake(dIntake, dIntake.position2));
        driver.x().whileTrue(new MoveDDIntake(dIntake, dIntake.position3));
        // driver.x().whileTrue(new WristIntakeIn(wrist));

        operator.a().whileTrue(new ArmMoving(s_Arm, 90));
        operator.b().whileTrue(new ArmMoving(s_Arm, 3));
        operator.x().whileTrue(new ArmMoving(s_Arm, 120));

        operator.povUp().onTrue(
            new DisabledInstantCommand(() -> Robot.level = MathUtil.clamp(Robot.level + 1, 0, 2)));
        operator.povDown().onTrue(
            new DisabledInstantCommand(() -> Robot.level = MathUtil.clamp(Robot.level - 1, 0, 2)));
        operator.povRight().onTrue(new DisabledInstantCommand(
            () -> Robot.column = MathUtil.clamp(Robot.column + 1, 0, 8)));
        operator.povLeft().onTrue(new DisabledInstantCommand(
            () -> Robot.column = MathUtil.clamp(Robot.column - 1, 0, 8)));
        operator.leftTrigger().and(operator.rightTrigger()).onTrue(new TestArm(s_Arm));
        driver.leftBumper().and(operator.rightBumper()).whileTrue(new MoveToScore(s_Swerve));

    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}
