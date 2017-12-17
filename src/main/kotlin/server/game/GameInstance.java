package server.game;

import java.util.Arrays;
import java.util.HashSet;

class GameInstance {
  private char[] word;
  private char[] wordGuess;
  private boolean correctWord;
  private HashSet<String> guesses = new HashSet<>();
  private int tries = 0;
  private int totalTries;

  GameInstance(String word) {
    System.out.println("A new game was started and the word is: " + word);

    this.word = word.toLowerCase().toCharArray();
    wordGuess = new char[word.length()];

    for (int i = 0; i < wordGuess.length; i++) wordGuess[i] = '_';

    totalTries = word.length();
  }

  void guess(String guess) {
    if (tries >= totalTries)
      throw new IllegalStateException("There are no tries left!" +
        "You have to start a new game!");
    if (correctWord)
      throw new IllegalStateException("You have already guessed the correct Guess word.\n" +
        "You have to start a new game in order to play more :)");
    if (wordAlreadyTried(guess))
      throw new IllegalStateException("You have already tried that " + (guess.length() == 1 ? "character" : "string") + "...");

    char[] guessAsCharArr = guess.toLowerCase().toCharArray();

    if (guess.length() == 1) tryChar(guessAsCharArr[0]);
    else tryWord(guessAsCharArr);
  }

  private void tryChar(char guess) {
    guesses.add(String.valueOf(guess));

    boolean correct = false;
    correctWord = true;

    for (int i = 0; i < word.length; i++) {
      if (word[i] == guess) {
        correct = true;
        wordGuess[i] = guess;
      }

      if (wordGuess[i] == '_') correctWord = false;
    }

    if (!correct) tries++;
  }

  private void tryWord(char[] guess) {
    guesses.add(String.valueOf(guess));

    if (Arrays.equals(word, guess)) {
      correctWord = true;
      wordGuess = word;
    } else {
      tries++;
    }
  }

  private boolean wordAlreadyTried(String guess) {
    return guesses.contains(guess);
  }

  boolean valid() {
    return !correctWord && tries < totalTries;
  }

  boolean correctGuess() {
    return correctWord;
  }

  String getWordGuess() {
    return new String(wordGuess);
  }

  int getTries() {
    return tries;
  }

  int getTotalTries() {
    return totalTries;
  }
}
