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

    // LED turns on once a piece in detected in the intake system
    public void PieceLoaded() {
        for (var i = 0; i < controLedBuffer.getLength(); i++) {
            controLedBuffer.setLED(i, Color.kBrown);
        }
    }

    // The LEDD that is always on
    // Red?
    public void defaultLED() {
        for (var i = 0; i < controLedBuffer.getLength(); i++) {
            controLedBuffer.setLED(i, Color.kRed);
        }

    }

    // Light turns on when the robot goes to balance on the ramp to alert other teams of intentions
    // maybe green?
    public void balancing() {
        for (var i = 0; i < controLedBuffer.getLength(); i++) {
            controLedBuffer.setLED(i, Color.kGreen);
        }
    }

    // Depicts a color coordinated with a cone to alert human players at the feeding station.
    public void ConeRequest() {
        for (var i = 0; i < controLedBuffer.getLength(); i++) {
            controLedBuffer.setLED(i, Color.kYellow);
        }
    }

    // Depicts a color coordinated with a cube to alert human players at the feeding station.
    public void CubeRequest() {
        for (var i = 0; i < controLedBuffer.getLength(); i++) {
            controLedBuffer.setLED(i, Color.kBlueViolet);
        }
    }

    // if the camera aligned with the target signaling that we are good to place our game piece.
    public void VisionAligned() {
        for (var i = 0; i < controLedBuffer.getLength(); i++) {
            controLedBuffer.setLED(i, Color.kAntiqueWhite);
        }
    }


}
