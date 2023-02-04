package frc.robot.commands.leds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LEDs;

/**
 * Command to flash the LED strip between 2 colors
 */
public class MorseCode extends CommandBase {
    private LEDs leds;
    private int unit = 10;
    private int dot = unit;
    private int dash = unit * 3;
    // Space between each dot or dash in a character
    private int intra_character = unit;
    // Space between each character
    private int inter_character = unit * 3;
    private int word_space = unit * 7;
    private int index = 0;
    private int counter = 1;
    private String word = "SOS";
    private ArrayList<Integer> finalWord = new ArrayList<Integer>();
    private boolean breakBool = false;


    private Map<String, ArrayList<Integer>> letters =
        Map.of("b", new ArrayList<Integer>(List.of(dash, dot, dot, dot)), "o",
            new ArrayList<Integer>(List.of(dash, dash, dash)), "s",
            new ArrayList<Integer>(List.of(dot, dot, dot)));

    /**
     * Command to flash the LED strip between 2 colors
     *
     * @param leds LED Subsystem
     * @param color The first color
     * @param altColor The second color
     */
    public MorseCode(LEDs leds) {
        this.leds = leds;
        addRequirements(leds);
        word = word.toLowerCase();
        char[] ch = word.toCharArray();
        for (char x : ch) {
            finalWord.addAll(letters.get(String.valueOf(x)));
            finalWord.add(0);
        }
    }

    @Override
    public void initialize() {
        index = 0;
        counter = 1;
        breakBool = false;
    }


    @Override
    public void execute() {
        if (index > finalWord.size()) {
            initialize();
        }
        int maxCount = intra_character;
        int x = finalWord.get(index);
        if (x == 0) {
            maxCount = inter_character;
            breakBool = true;
        } else if (!breakBool) {
            maxCount = x;
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
        return index == finalWord.size();
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}

