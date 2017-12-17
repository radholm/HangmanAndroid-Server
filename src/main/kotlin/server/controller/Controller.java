package server.controller;

import common.GameState;
import server.game.Game;
import server.word.WordList;

/**
 * Controller for the server.
 */
public class Controller {
  private Game game;

  /**
   * @see Game#Constructor()
   */
  public GameState newHangmanGame() {
    game = new Game(this);

    return game.newGameInstance();
  }

  /**
   * @see Game#guess(String)
   */
  public GameState guess(String guess) {
    return game.guess(guess);
  }

  /**
   * @see WordList#getRandomWord()
   */
  public String randomWord() {
    return WordList.getRandomWord();
  }
}
