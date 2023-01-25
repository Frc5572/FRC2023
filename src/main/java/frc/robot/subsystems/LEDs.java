package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * This is the class header for the LEDs Subsystem
 */
public class LEDs extends SubsystemBase {
    private AddressableLEDBuffer controLedBuffer;
    private AddressableLED addressableLED;
    private int movingColorDelay = 0;
    private int movingLED = 0;
    private boolean movingDirection = true;
    private int flashingDelay = 0;

    /**
     * constructs a LED Subsystem
     * 
     * @param length length of the addressable LEDS
     * @param port port ID of PWM
     * 
     */

    public LEDs(int length, int port) {
        controLedBuffer = new AddressableLEDBuffer(length);
        addressableLED = new AddressableLED(port);

        addressableLED.setLength(controLedBuffer.getLength());
        addressableLED.setData(controLedBuffer);
        addressableLED.start();
    }


    /**
     * Sets RGB Color
     * 
     * @param index index of the LEDs
     * @param r - [0 - 255]
     * @param g - [0 - 255]
     * @param b - [0 - 255]
     */
    public void setRGB(int index, int r, int g, int b) {
        for (var i = 0; i < controLedBuffer.getLength(); i++) {
            controLedBuffer.setRGB(index, r, g, b);
        }
        addressableLED.setData(controLedBuffer);
    }

    /**
     * sets color of LED string to moving
     * 
     * @param color color set for the LEDs
     * 
     */
    public void setColor(Color color) {
        for (var i = 0; i < controLedBuffer.getLength(); i++) {
            controLedBuffer.setLED(i, color);
        }
    }

    /**
     * sets the LEDs to change to one color from another
     * 
     * @param color color sets for the LEDs
     * @param count the count number of LEDs to be moved
     * @param inverted whether to invert the color choices
     */
    public void movingColor(Color color, int count, boolean inverted) {
        Color theColor = inverted ? Color.kBlack : color;
        Color secondColor = inverted ? color : Color.kBlack;
        if (movingColorDelay == 0) {
            for (var i = 0; i < controLedBuffer.getLength(); i++) {
                if (Math.abs(i - movingLED) < count) {
                    controLedBuffer.setLED(i, theColor);
                } else {
                    controLedBuffer.setLED(i, secondColor);
                }
            }
            if (movingDirection) {
                movingLED++;
            } else {
                movingLED--;
            }
            if (movingLED >= controLedBuffer.getLength() - 1 || movingLED <= 0) {
                movingDirection = !movingDirection;
            }
            addressableLED.setData(controLedBuffer);
        }
        movingColorDelay += 1;
        movingColorDelay %= 2;
    }

    /**
     * flashes on and off the color white
     */
    public void flashingLED() {
        if (flashingDelay < 10) {
            for (var i = 0; i < controLedBuffer.getLength(); i++) {
                controLedBuffer.setLED(i, Color.kWhite);
            }
        } else {
            for (var i = 0; i < controLedBuffer.getLength(); i++) {
                controLedBuffer.setLED(i, Color.kBlack);
            }
        }
        addressableLED.setData(controLedBuffer);
        flashingDelay++;
        flashingDelay %= 20;
    }
}
