// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Wrist;

/**
 * Creates a new command for WristMovement
 */
public class WristMovement extends CommandBase {

    Wrist wrist;

    /** Creates a new WristMovement. */
    public WristMovement(Wrist wrist) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.wrist = wrist;

        addRequirements(wrist);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        // double rotation = -xboxController.getLeftY();
        // rotation = (Math.abs(rotation) < Constants.stickDeadband) ? 0 : rotation;
        // this.wrist.setWrist(rotation);
        if (!(this.wrist.atGoal())) {
            this.wrist.setWrist(Constants.WristPID.TargetEncoderDegree);
        }
    }

    @Override
    public void end(boolean interrupted) {
        wrist.stopWrist();
    }

    @Override
    public boolean isFinished() {
        return wrist.atGoal();
    }



}
