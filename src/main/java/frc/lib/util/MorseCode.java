package frc.lib.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wpi.first.wpilibj.Filesystem;

/**
 * A library for using Morse Code
 */
public class MorseCode {

    public static JsonNode morseJSON =
        openJson(new File(Filesystem.getDeployDirectory(), "morse-code.json"));
    private static int unit = 5;
    public static int dot = unit;
    public static int dash = unit * 3;
    // Space between each dot or dash in a character
    public static int intra_character = unit;
    // Space between each character
    public static int inter_character = unit * 3;
    public static int word_space = unit * 7;

    /**
     * Open JSON file.
     *
     * @param file JSON File to open.
     * @return JsonNode of file.
     */
    private static JsonNode openJson(File file) {
        try {
            return new ObjectMapper().readTree(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate a Map of dots and dashes for each character in the Morse Code Alphabet
     *
     * @return A map of characters to dot-dash codes
     */
    public static Map<String, ArrayList<Integer>> generateMorseCodeFlash() {
        HashMap<String, ArrayList<Integer>> letters = new HashMap<String, ArrayList<Integer>>();
        Iterator<Entry<String, JsonNode>> iterator = morseJSON.fields();
        iterator.forEachRemaining(field -> {
            String key = field.getKey();
            char[] parts = field.getValue().textValue().toCharArray();
            ArrayList<Integer> arr = new ArrayList<Integer>();
            for (char x : parts) {
                if ('.' == x) {
                    arr.add(dot);
                } else if ('-' == x) {
                    arr.add(dash);
                }
            }
            letters.put(key, arr);
        });
        letters.put(" ", new ArrayList<Integer>(List.of(word_space)));
        return letters;
    }

}
