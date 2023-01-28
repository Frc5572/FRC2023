// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants;
import frc.robot.subsystems.Wrist;

/**
 * Creates a new command for WristMovement
 */
public class WristMovement extends CommandBase {

    Wrist wristMovement;
    CommandXboxController xboxController;

    /** Creates a new WristMovement. */
    public WristMovement(Wrist wristMovement, CommandXboxController xboxController) {
        this.wristMovement = wristMovement;
        this.xboxController = xboxController;
        addRequirements(wristMovement);
    }

    @Override
    public void execute() {
        double rotation = -xboxController.getLeftY();
        rotation = (Math.abs(rotation) < Constants.stickDeadband) ? 0 : rotation;
        this.wristMovement.setWrist(rotation);
    }

}
