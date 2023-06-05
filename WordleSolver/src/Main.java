import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static final int wordLength = 5;
    public static final int attempts = 5;

    public static final int numOfSuggestions = 10;

    public static void main(String[] args) throws IOException {
        List<String> wordList = getWords();
        List<String> regexList = initializeRegex();
        List<Character> detectedLetters = new ArrayList<>();

        for (int i = 0; i < attempts; i++) {
            if (i == 0) {
                System.out.println("Enter your starting word: ");
                String startingWord = readInput();

                System.out.println("Enter Feedback (X--> Gray, Y--> Yellow, G--> Green): ");
                String feedback = readInput();

                updateRegex(startingWord, feedback, detectedLetters, regexList);

                System.out.println("Suggestion: ");
                printTopMatches(detectedLetters, regexList, wordList);
            }
            else {
                String startingWord = topMatch(detectedLetters, regexList, wordList);

                System.out.println("Enter Feedback (X--> Gray, Y--> Yellow, G--> Green): ");
                String feedback = readInput();

                updateRegex(startingWord, feedback, detectedLetters, regexList);

                System.out.println("Suggestion: ");
                printTopMatches(detectedLetters, regexList, wordList);
            }
        }
    }

    private static List<String> getWords() throws IOException {
        List<String> wordList = new ArrayList<>();

        File file = new File("inOrderWords.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String word;
        while ((word = br.readLine()) != null) {
            wordList.add(word.toUpperCase());
        }

        return wordList;
    }

    private static String readInput() {
        Scanner myObj = new Scanner(System.in);
        String input = myObj.nextLine();
        return input.toUpperCase();
    }

    private static List<String> initializeRegex() {
        List<String> regexList = new ArrayList<>();
        for (int i = 0; i < wordLength; i++) {
            regexList.add("[ABCDEFGHIJKLMNOPQRSTUVWXYZ]");
        }
        return regexList;
    }

    private static void updateRegex(String guess, String feedback, List<Character> detectedLetters, List<String> regexList) {
        for (int pos = 0; pos < wordLength; pos++) {
            char currentLetter = guess.charAt(pos);
            char feedbackForCurrentLetter = feedback.charAt(pos);

            if (isGray(feedbackForCurrentLetter)) {
                if (detectedLetters.contains(currentLetter)) {
                    String currRegex = regexList.get(pos);
                    String updatedRegex = currRegex.replace(currentLetter, '\0');
                    regexList.set(pos, updatedRegex);
                } else {
                    for (int regexIndex = 0; regexIndex < wordLength; regexIndex++) {
                        String currRegex = regexList.get(regexIndex);
                        String updatedRegex = currRegex.replace(currentLetter, '\0');
                        regexList.set(regexIndex, updatedRegex);
                    }
                }
            } else if (isYellow(feedbackForCurrentLetter)) {
                String currRegex = regexList.get(pos);
                String updatedRegex = currRegex.replace(currentLetter, '\0');
                regexList.set(pos, updatedRegex);
                detectedLetters.add(guess.charAt(pos));
            } else if (isGreen(feedbackForCurrentLetter)) {
                regexList.set(pos, String.valueOf(currentLetter));
                detectedLetters.add(guess.charAt(pos));
            } else {
                System.out.println("Invalid feedback");
            }
        }
    }

    private static void printTopMatches(List<Character> detectedLetters, List<String> regexList, List<String> wordList) {
        String regex = regexList.stream().collect(Collectors.joining());
        List<String> filteredWords = wordList.stream()
                .filter(word -> word.matches(regex) && allDetectedLettersPresent(word, detectedLetters))
                .collect(Collectors.toList());

        List<String> sortedWords = filteredWords.stream()
                .sorted(Comparator.comparingDouble(Main::calculateEntropy).reversed())
                .limit(numOfSuggestions)
                .collect(Collectors.toList());

        System.out.println(sortedWords.get(0));
    }

    private static String topMatch(List<Character> detectedLetters, List<String> regexList, List<String> wordList) {
            String regex = regexList.stream().collect(Collectors.joining());
            List<String> filteredWords = wordList.stream()
                    .filter(word -> word.matches(regex) && allDetectedLettersPresent(word, detectedLetters))
                    .collect(Collectors.toList());

            List<String> sortedWords = filteredWords.stream()
                    .sorted(Comparator.comparingDouble(Main::calculateEntropy).reversed())
                    .limit(numOfSuggestions)
                    .collect(Collectors.toList());

            return sortedWords.get(0);
    }


    private static boolean allDetectedLettersPresent(String currentWord, List<Character> detectedLetters) {
        for (int i = 0; i < detectedLetters.size(); i++) {
            if (!currentWord.contains(detectedLetters.get(i).toString())) {
                return false;
            }
        }
        return true;
    }

    private static boolean isGray(char c) {
        return c == 'X' || c == 'x';
    }

    private static boolean isYellow(char c) {
        return c == 'Y' || c == 'y';
    }

    private static boolean isGreen(char c) {
        return c == 'G' || c == 'g';
    }

    private static double calculateEntropy(String word) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        int totalCharacters = 0;
        for (char c : word.toCharArray()) {
            if (Character.isLetter(c)) {
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
                totalCharacters++;
            }
        }

        double entropy = 0.0;
        for (int frequency : frequencyMap.values()) {
            double probability = (double) frequency / totalCharacters;
            entropy -= probability * Math.log(probability);
        }

        return entropy;
    }
}



