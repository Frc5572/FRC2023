package frc.robot.commands.leds;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LEDs;

/**
 * Command to move LEDs back and forth like a Cylon eye
 */
public class BouncingBalls extends CommandBase {
    private final LEDs leds;
    private int end, start;
    private int red = 255;
    private int blue = 255;
    private int green = 255;
    private int ballCount = 3;
    private double Gravity = -9.81;
    private int StartHeight = 1;
    private double[] Height = new double[ballCount];
    private double ImpactVelocityStart = Math.sqrt(-2 * Gravity * StartHeight);
    private double[] ImpactVelocity = new double[ballCount];
    private double[] TimeSinceLastBounce = new double[ballCount];
    private int[] Position = new int[ballCount];
    private double[] ClockTimeSinceLastBounce = new double[ballCount];
    private double[] Dampening = new double[ballCount];

    /**
     * Command to move LEDs back and forth like a Cylon eye
     *
     * @param leds LED subsystem
     * @param color Color to which to set the LEDs
     * @param count The number of LEDs in the moving dot. Best is an odd number.
     * @param inverted Whether to invert the Color that moves.
     */
    public BouncingBalls(LEDs leds) {
        this.leds = leds;
        this.end = leds.getEnd();
        this.start = leds.getStart();
        addRequirements(leds);
    }

    @Override
    public void initialize() {
        for (int i = 0; i < ballCount; i++) {
            ClockTimeSinceLastBounce[i] = Timer.getFPGATimestamp();
            Height[i] = StartHeight;
            Position[i] = 0;
            ImpactVelocity[i] = ImpactVelocityStart;
            TimeSinceLastBounce[i] = 0;
            Dampening[i] = 0.90 - i / Math.pow(ballCount, 2);
        }
    }

    @Override
    public void execute() {
        for (int i = 0; i < ballCount; i++) {
            TimeSinceLastBounce[i] = Timer.getFPGATimestamp() - ClockTimeSinceLastBounce[i];
            Height[i] = 0.5 * Gravity * Math.pow(TimeSinceLastBounce[i] / 1000, 2.0)
                + ImpactVelocity[i] * TimeSinceLastBounce[i] / 1000;

            if (Height[i] < 0) {
                Height[i] = 0;
                ImpactVelocity[i] = Dampening[i] * ImpactVelocity[i];
                ClockTimeSinceLastBounce[i] = Timer.getFPGATimestamp();

                if (ImpactVelocity[i] < 0.01) {
                    ImpactVelocity[i] = ImpactVelocityStart;
                }
            }
            Position[i] = (int) Math.round(Height[i] * (leds.getLength() - 1) / StartHeight);
        }

        for (int i = 0; i < ballCount; i++) {
            leds.setRGB(Position[i], red, green, blue);
        }
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

}
