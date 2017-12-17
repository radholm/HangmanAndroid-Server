package server.game;

import common.GameState;
import server.controller.Controller;

import java.util.StringJoiner;

/**
 * Represents the Hangman game and its state.
 */
public class Game {
  private int score = 0;
  private GameInstance gameInstance;
  private Controller controller;

  public Game(Controller controller) {
    this.controller = controller;
  }

  /**
   * Starts a new Game instance.
   *
   * @throws IllegalArgumentException If there already is a valid game instance in use.
   */
  public GameState newGameInstance() throws IllegalStateException {
    gameInstance = new GameInstance(controller.randomWord());

    return gameStateFactory();
  }

  /**
   * Calls the corresponding game instance guess and tries the guess against the chosen word.
   *
   * @param msgToGuess The <code>String</code> to guess.
   * @return The current state of the game as a <code>GameState</code> object.
   */
  public GameState guess(String msgToGuess) {
    if (gameInstance == null)
      throw new IllegalStateException("There is no game instance, start a new instance in order to play!");

    try {
      gameInstance.guess(msgToGuess);
    } catch (IllegalStateException e) {
      return gameStateFactory();
    }

    if (gameInstance.correctGuess()) {
      score++;

      return newGameInstance();
    } else if (!gameInstance.valid()) {
      score--;

      return newGameInstance();
    }

    return gameStateFactory();
  }

  private GameState gameStateFactory() {
    StringJoiner state = new StringJoiner(" ");

    for (char c : gameInstance.getWordGuess().toCharArray())
      state.add(c + "");

    return new GameState(score, gameInstance.getTries(), gameInstance.getTotalTries(), state.toString());
  }
}
