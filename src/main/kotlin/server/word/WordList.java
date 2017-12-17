package server.word;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Reads random words from a file.
 */
public class WordList {
  private static WordList instance = new WordList();
  private static List<String> words;
  private Random random = new Random();

  private WordList() {
    String wordPath = "./words.txt";

    try (BufferedReader reader = Files.newBufferedReader(Paths.get(wordPath))) {
      words = reader.lines().collect(Collectors.toList());
    } catch (IOException e) {
      System.err.printf("Failed to read the file \"%s\"", wordPath);
      e.printStackTrace();
    }
  }

  private static WordList getInstance() {
    return instance;
  }

  private String randomWord() {
    return words.get(random.nextInt(words.size()));
  }

  /**
   * @return A random word
   */
  public static String getRandomWord() {
    return getInstance().randomWord();
  }
}
