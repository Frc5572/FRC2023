// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Wrist;

/**
 * Creates a new Command for Wrist Alignment
 */
public class WristAlignment extends CommandBase {
    /** Creates a new WristAlignment. */
    Wrist wristAlignment;

    public WristAlignment(Wrist wristAlignment) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.wristAlignment = wristAlignment;
    }


    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (!(this.wristAlignment.getAlignment())) {
            this.wristAlignment.setWrist(Constants.WristPID.TargetEncoderDegree);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        this.wristAlignment.stopWrist();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {

        return this.wristAlignment.getAlignment();
    }
}
