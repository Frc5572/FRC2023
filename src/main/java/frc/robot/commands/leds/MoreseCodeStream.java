package frc.robot.commands.leds;

import java.util.ArrayList;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.lib.util.MorseCode;
import frc.robot.subsystems.LEDs;

/**
 * Command to flash the LED strip between 2 colors
 *
 * <p>
 * DOES NOT WORK YET
 */
public class MoreseCodeStream extends CommandBase {
    private LEDs leds;
    private int index = 0;
    private ArrayList<Boolean> finalWord = new ArrayList<Boolean>();
    private ArrayList<Color> finalStrip = new ArrayList<Color>();

    /**
     * Command to flash the LED strip between 2 colors
     *
     * @param leds LED Subsystem
     * @param word The word or sentence to flash in Morse Code
     * @param color The color to flash
     */
    public MoreseCodeStream(LEDs leds, String word, Color color) {
        this.leds = leds;
        finalWord = MorseCode.generateDitArray(word, 1);
        finalStrip = MorseCode.generateDirColorArray(finalWord, color);
        addRequirements(leds);
    }

    /**
     * Command to flash the LED strip between 2 colors
     *
     * @param leds LED Subsystem
     * @param word The word or sentence to flash in Morse Code
     */
    public MoreseCodeStream(LEDs leds, String word) {
        this(leds, word, Color.kRed);
    }

    @Override
    public void initialize() {
        index = 0;
        leds.setColor(Color.kBlack);
        System.out.println(finalStrip);
    }


    @Override
    public void execute() {
        leds.setColor(Color.kBlack);
        for (int i = index; i >= 0; i--) {
            if (index >= 0 && index < leds.getLength()) {
                leds.setColor(i, finalStrip.get(index - i));
            } else if (index >= leds.getLength()) {
                leds.setColor(i - (leds.getLength() - index),
                    finalStrip.get(i - (leds.getLength() - index)));
            }
        }
        index++;
        // for (int i = index; i >= 0; i--) {
        // int q = index - i;
        // Color x = finalStrip.get(q);
        // leds.setColor(q, x);
        // }
    }

    // @Override
    // public boolean isFinished() {
    // return index >= finalWord.size();
    // }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}

