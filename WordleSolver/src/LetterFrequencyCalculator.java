import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LetterFrequencyCalculator {
    public static void main(String[] args) {
        String filePath = "src/words.txt";
        Map<Integer, Map<Character, Integer>> letterFrequencyMap = calculateLetterFrequency(filePath);

        // Display the letter frequency at each position
        for (Map.Entry<Integer, Map<Character, Integer>> entry : letterFrequencyMap.entrySet()) {
            int position = entry.getKey();
            Map<Character, Integer> frequencyMap = entry.getValue();
            System.out.println("Position " + position + ": " + frequencyMap);
        }
    }

    public static Map<Integer, Map<Character, Integer>> calculateLetterFrequency(String filePath) {
        Map<Integer, Map<Character, Integer>> letterFrequencyMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String word;
            while ((word = reader.readLine()) != null) {
                if (word.length() == 5) {
                    for (int i = 0; i < 5; i++) {
                        char letter = word.charAt(i);
                        Map<Character, Integer> frequencyMap = letterFrequencyMap.getOrDefault(i, new HashMap<>());
                        frequencyMap.put(letter, frequencyMap.getOrDefault(letter, 0) + 1);
                        letterFrequencyMap.put(i, frequencyMap);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return letterFrequencyMap;
    }
}

