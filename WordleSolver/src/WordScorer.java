import java.io.*;
import java.util.*;

public class WordScorer {
    private static final Map<Integer, Map<Character, Integer>> letterFrequencyMap = new HashMap<>();
    private static final String OUTPUT_FILE_PATH = "inOrderWords.txt";

    public static void main(String[] args) throws FileNotFoundException {
        loadLetterFrequencyMap();

        List<String> words = new ArrayList<>();

        File file = new File("src/words.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));


        String word;
        while (true) {
            try {
                if ((word = br.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            words.add(word);
        }

        Map<String, Integer> wordScores = calculateWordScores(words);
        List<Map.Entry<String, Integer>> sortedEntries = sortScoresDescending(wordScores);

        writeOutputToFile(sortedEntries);
        System.out.println("Output written to " + OUTPUT_FILE_PATH);
    }

    public static void loadLetterFrequencyMap() {
        // Add the provided letter frequency map
        letterFrequencyMap.put(0, createFrequencyMap("a=737, b=909, c=922, d=685, e=303, f=598, g=638, h=489, i=165, j=202, k=376, l=577, m=693, n=325, o=262, p=859, q=78, r=628, s=1564, t=815, u=189, v=242, w=413, x=16, y=181, z=105"));
        letterFrequencyMap.put(1, createFrequencyMap("a=2263, b=81, c=176, d=84, e=1628, f=24, g=76, h=545, i=1383, j=11, k=95, l=699, m=188, n=345, o=2096, p=231, q=15, r=940, s=93, t=239, u=1187, v=52, w=163, x=57, y=271, z=29"));
        letterFrequencyMap.put(2, createFrequencyMap("a=1235, b=335, c=392, d=390, e=882, f=178, g=364, h=120, i=1051, j=46, k=272, l=848, m=511, n=964, o=993, p=364, q=13, r=1198, s=533, t=616, u=667, v=240, w=271, x=133, y=213, z=142"));
        letterFrequencyMap.put(3, createFrequencyMap("a=1074, b=243, c=411, d=471, e=2327, f=233, g=423, h=235, i=880, j=29, k=503, l=771, m=402, n=788, o=698, p=418, q=2, r=719, s=516, t=898, u=401, v=155, w=128, x=12, y=108, z=126"));
        letterFrequencyMap.put(4, createFrequencyMap("a=680, b=59, c=127, d=823, e=1521, f=82, g=143, h=370, i=280, j=3, k=259, l=476, m=182, n=530, o=389, p=147, q=4, r=673, s=3958, t=727, u=67, v=4, w=64, x=70, y=1301, z=32"));
    }

    public static Map<Character, Integer> createFrequencyMap(String frequencyMapString) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        String[] keyValuePairs = frequencyMapString.split(", ");
        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split("=");
            char letter = keyValue[0].charAt(0);
            int frequency = Integer.parseInt(keyValue[1]);
            frequencyMap.put(letter, frequency);
        }
        return frequencyMap;
    }

    public static Map<String, Integer> calculateWordScores(List<String> words) {
        Map<String, Integer> wordScores = new HashMap<>();
        for (String word : words) {
            int score = calculateWordScore(word);
            wordScores.put(word, score);
        }
        return wordScores;
    }

    public static int calculateWordScore(String word) {
        int score = 0;
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            Map<Character, Integer> frequencyMap = letterFrequencyMap.get(i);
            if (frequencyMap != null) {
                int frequency = frequencyMap.getOrDefault(letter, 0);
                score += frequency;
            }
        }
        return score;
    }

    public static List<Map.Entry<String, Integer>> sortScoresDescending(Map<String, Integer> wordScores) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(wordScores.entrySet());
        entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return entries;
    }

    public static void writeOutputToFile(List<Map.Entry<String, Integer>> sortedEntries) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH))) {
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                writer.write(entry.getKey());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing the output to file.");
            e.printStackTrace();
        }
    }
}
