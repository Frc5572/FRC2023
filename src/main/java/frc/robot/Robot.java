// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.nio.file.Paths;
import java.util.Arrays;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.networktables.BooleanArrayEntry;
import edu.wpi.first.networktables.BooleanEntry;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.IntegerArrayEntry;
import edu.wpi.first.networktables.IntegerPublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.lib.util.Scoring;
import frc.lib.util.Scoring.GamePiece;
import frc.lib.util.ctre.CTREConfigs;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    public static CTREConfigs ctreConfigs;

    private Command m_autonomousCommand;

    private RobotContainer m_robotContainer;

    public static int level = 0;
    public static int column = 0;
    public boolean[] node_status = new boolean[27];
    public boolean cone_tipped = false;

    public static UsbCamera camera = CameraServer.startAutomaticCapture("Magazine Camera", 0);
    public NetworkTable table = NetworkTableInstance.getDefault().getTable("nodeselector");
    public IntegerArrayEntry nodePublisher =
        table.getIntegerArrayTopic("node_target").getEntry(new long[] {0, 0});
    public BooleanArrayEntry nodeStatusPublisher =
        table.getBooleanArrayTopic("node_status").getEntry(node_status);
    public BooleanEntry coneTippedPublisher = table.getBooleanTopic("cone_tipped").getEntry(false);
    public IntegerPublisher timePublisher = table.getIntegerTopic("match_time").publish();
    public BooleanPublisher isAutoPublisher = table.getBooleanTopic("is_auto").publish();

    // private Ultrasonic ultrasonic = new Ultrasonic();
    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        Arrays.fill(node_status, false);
        ctreConfigs = new CTREConfigs();
        // Instantiate our RobotContainer. This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        m_robotContainer = new RobotContainer();
        var app = Javalin.create(config -> {
            config.staticFiles.add(Paths
                .get(Filesystem.getDeployDirectory().getAbsolutePath().toString(), "nodeselector")
                .toString(), Location.EXTERNAL);
        });
        app.start(5800);
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>
     * This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler. This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods. This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
        // m_robotContainer.levelWidget.setDouble(level);
        // m_robotContainer.columnWidget.setDouble(column);
        m_robotContainer.gamePieceWidget.setBoolean(Scoring.getGamePiece() == GamePiece.CONE);
        timePublisher.set((long) Math.ceil(Math.max(0.0, DriverStation.getMatchTime())));
        isAutoPublisher.set(DriverStation.isAutonomous());
        level = (int) nodePublisher.get()[0];
        column = (int) nodePublisher.get()[1];
        nodePublisher.set(new long[] {level, column});
        node_status = nodeStatusPublisher.get();
        nodeStatusPublisher.set(node_status);
        cone_tipped = coneTippedPublisher.get(false);
        coneTippedPublisher.set(cone_tipped);
    }

    /** This function is called once each time the robot enters Disabled mode. */
    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}

    /**
     * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
     */
    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }
    }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
        m_robotContainer.s_Swerve.resetFieldRelativeOffset();
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {}

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {}
}
