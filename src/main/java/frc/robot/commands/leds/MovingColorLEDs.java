package frc.robot.commands.leds;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LEDs;

public class MovingColorLEDs extends CommandBase {
    private final LEDs leds;
    private int ledLength;
    private Color color;
    private int count;
    private boolean inverted = false;
    private int movingColorDelay = 0;
    private int movingLED = 0;
    private boolean movingDirection = true;

    public MovingColorLEDs(LEDs leds, Color color, int count, boolean inverted) {
        this.leds = leds;
        this.color = color;
        this.count = count;
        this.inverted = inverted;
        ledLength = leds.getLength();
        addRequirements(leds);
    }

    @Override
    public void execute() {
        Color theColor = inverted ? Color.kBlack : color;
        Color secondColor = inverted ? color : Color.kBlack;
        if (movingColorDelay == 0) {
            for (var i = 0; i < ledLength; i++) {
                if (Math.abs(i - movingLED) < count) {
                    leds.setColor(i, theColor);
                } else {
                    leds.setColor(i, secondColor);
                }
            }
            if (movingDirection) {
                movingLED++;
            } else {
                movingLED--;
            }
            if (movingLED >= ledLength - 1 || movingLED <= 0) {
                movingDirection = !movingDirection;
            }
            leds.setData();
        }
        movingColorDelay += 1;
        movingColorDelay %= 2;
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
