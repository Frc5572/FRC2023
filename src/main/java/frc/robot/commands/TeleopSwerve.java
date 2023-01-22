// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Subsystems.Swerve;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class TeleopSwerve extends CommandBase {
    Translation2d translation2d;
    Boolean fieldRelative;
    double Rotation;
    Boolean openLoop;
    Swerve swerve;
    CommandXboxController xboxController;
    double yAxis;
    double xAxis;
    double rAxis;

    /** Creates a new TeleopSwerve. */
    public TeleopSwerve(Boolean fieldRelative, double Rotation, Boolean openloop, Swerve swerve,
        CommandXboxController xboxController) {
        this.translation2d = translation2d;
        this.fieldRelative = fieldRelative;
        this.Rotation = Rotation;
        this.openLoop = openloop;
        this.swerve = swerve;
        this.xboxController = xboxController;
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        this.yAxis = xboxController.getLeftY();
        this.xAxis = xboxController.getLeftX();
        this.rAxis = xboxController.getRightX();
        yAxis = Math.abs(yAxis) < Constants.stickDeadband ? 0 : yAxis;
        xAxis = Math.abs(xAxis) < Constants.stickDeadband ? 0 : xAxis;
        rAxis = Math.abs(rAxis) < Constants.stickDeadband ? 0 : rAxis;
        translation2d = new Translation2d(yAxis, xAxis).times(Constants.maxSpeed);
        Rotation = rAxis * Constants.maxAngularVelocity;
        swerve.Drive(translation2d, fieldRelative, Rotation, openLoop);

  }
  }
