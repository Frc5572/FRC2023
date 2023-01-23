package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDs extends SubsystemBase {
    private AddressableLEDBuffer controLedBuffer;
    private AddressableLED addressableLED;


    // constructs a LED Subsystem
    public LEDs(int length, int port) {
        controLedBuffer = new AddressableLEDBuffer(length);
        addressableLED = new AddressableLED(port);

        addressableLED.setLength(controLedBuffer.getLength());
        addressableLED.setData(controLedBuffer);
        addressableLED.start();
    }

    // sets RGB Color
    /*
     * r - [0 - 255] g - [0 - 255] b - [0 - 255]
     */
    public void setRGB(int index, int r, int g, int b) {
        for (var i = 0; i < controLedBuffer.getLength(); i++) {
            controLedBuffer.setRGB(index, r, g, b);
        }
        addressableLED.setData(controLedBuffer);
    }

    // sets color of LED strip to moving
    public void setColor(Color color) {
        for (var i = 0; i < controLedBuffer.getLength(); i++) {
            controLedBuffer.setLED(i, color);
        }
    }

    public void PieceLoaded() {

    }

    public void defaultLED() {

    }

    public void balancing() {

    }

    public void ConeRequest() {

    }

    public void CubeRequest() {

    }

    public void VisionAligned() {

    }


}
