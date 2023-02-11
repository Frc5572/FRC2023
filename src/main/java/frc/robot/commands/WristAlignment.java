// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Wrist;

/**
 * Creates a new Command for Wrist Alignment. NOTE!!!!!!!! (Wrist alignment uses the method
 * getAlignement) This will align the robot.
 */
public class WristAlignment extends CommandBase {
    /** Creates a new WristAlignment. */
    Wrist wrist;
    double angle;

    /** Creates the constructor of the wrist alignment */
    public WristAlignment(Wrist wrist, double angle) {
        this.wrist = wrist;
        this.angle = angle;
        addRequirements(wrist);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (!this.wrist.getAlignment(this.angle)) {
            this.wrist.setWrist(Constants.Wrist.PID.TargetEncoderDegree);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        this.wrist.stopWrist();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return this.wrist.getAlignment(this.angle);
    }
}
