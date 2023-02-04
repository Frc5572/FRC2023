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
    private int unit = 5;
    private int dot = unit;
    private int dash = unit * 3;
    // Space between each dot or dash in a character
    private int intra_character = unit;
    // Space between each character
    private int inter_character = unit * 3;
    private int word_space = unit * 7;
    private int index = 0;
    private int counter = 1;
    private String word = "SOS SOS";
    private ArrayList<Integer> finalWord = new ArrayList<Integer>();
    private boolean breakBool = false;


    private Map<String, ArrayList<Integer>> letters =
        Map.of("b", new ArrayList<Integer>(List.of(dash, dot, dot, dot)), "o",
            new ArrayList<Integer>(List.of(dash, dash, dash)), "s",
            new ArrayList<Integer>(List.of(dot, dot, dot)), " ",
            new ArrayList<Integer>(List.of(word_space)));

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

    @Override
    public void initialize() {
        index = 0;
        counter = 1;
        breakBool = false;
        System.out.println(finalWord);
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
            maxCount = intra_character;
        } else if (x == -1) {
            maxCount = inter_character;
        } else if (x == -2) {
            maxCount = word_space;
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

