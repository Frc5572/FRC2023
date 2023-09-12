package frc.robot.subsystems.wristintake;

import org.littletonrobotics.junction.AutoLog;

public interface WristIntakeIO {

    @AutoLog
    public static class WristIntakeInputs {

    }

    public default void updateInputs(WristIntakeInputs inputs) {}

}
