package frc.robot.commands.leds;

import java.util.ArrayList;
import java.util.Map;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.lib.util.MorseCode;
import frc.robot.subsystems.LEDs;

/**
 * Command to flash the LED strip between 2 colors
 */
public class MorseCodeFlash extends CommandBase {
    private LEDs leds;
    private int index = 0;
    private int counter = 1;
    private ArrayList<Integer> finalWord = new ArrayList<Integer>();
    private boolean breakBool = false;
    private Color color;


    private Map<String, ArrayList<Integer>> letters = MorseCode.generateMorseCodeFlash();

    /**
     * Command to flash the LED strip between 2 colors
     *
     * @param leds LED Subsystem
     * @param word The word or sentance to flash in Morse Code
     * @param color The color to flash
     */
    public MorseCodeFlash(LEDs leds, String word, Color color) {
        this.leds = leds;
        this.color = color;
        addRequirements(leds);
        word = word.toLowerCase();
        String[] parts = word.split(" ");
        for (int a = 0; a < parts.length; a++) {
            char[] ch = parts[a].toCharArray();
            for (int x = 0; x < ch.length; x++) {
                for (int y = 0; y < letters.get(String.valueOf(ch[x])).size(); y++) {
                    finalWord.add(letters.get(String.valueOf(ch[x])).get(y));
                    if (y < letters.get(String.valueOf(ch[x])).size() - 1) {
                        finalWord.add(0);
                    }
                }
                if (x < ch.length - 1) {
                    finalWord.add(0);
                }
            }
            if (a < parts.length - 1) {
                finalWord.add(-2);
            }
        }
    }

    /**
     * Command to flash the LED strip between 2 colors
     *
     * @param leds LED Subsystem
     * @param word The word or sentance to flash in Morse Code
     */
    public MorseCodeFlash(LEDs leds, String word) {
        this(leds, word, Color.kRed);
    }

    @Override
    public void initialize() {
        index = 0;
        counter = 1;
        breakBool = false;
        leds.setColor(Color.kBlack);
    }


    @Override
    public void execute() {
        // if (index > finalWord.size()) {
        // initialize();
        // }
        int x = finalWord.get(index);
        int maxCount = x;
        if (x <= 0) {
            breakBool = true;
        }
        if (x == 0) {
            maxCount = MorseCode.intra_character;
        } else if (x == -1) {
            maxCount = MorseCode.inter_character;
        } else if (x == -2) {
            maxCount = MorseCode.word_space;
        }
        if (!breakBool && counter <= maxCount) {
            leds.setColor(Color.kRed);
        } else if (breakBool && counter <= maxCount) {
            leds.setColor(Color.kBlack);
        }
        if (counter % maxCount == 0) {
            counter = 0;
            if (breakBool) {
                index++;
                breakBool = false;
            } else {
                breakBool = true;
            }
        }
        counter++;
    }

    @Override
    public boolean isFinished() {
        return index >= finalWord.size();
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}

