package cs3500.freecell.model;

import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw04.MultiMoveFreecellModel;

/**
 * Class to house factory method for creating different instances of models for a
 * game of freecell.
 */
public class FreecellModelCreator {

  /**
   * Enumeration to represent a type of Freecell game.
   */
  public enum GameType {
    SINGLEMOVE, MULTIMOVE
  }

  /**
   * Factory method for creating different instances of freecell model implementations.
   * @param type a GameType object that determines what type of freecell model this method returns
   * @return the type of freecell model object specified by the parameter
   */
  public static FreecellModel<Card> create(GameType type) {
    if (type == GameType.SINGLEMOVE) {
      return new SimpleFreecellModel();
    } else {
      return new MultiMoveFreecellModel();
    }
  }
}
