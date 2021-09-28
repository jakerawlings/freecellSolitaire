package cs3500.freecell.model.hw02;

/**
 * Represents a standard playing card.
 */
public interface Card {

  /**
   * Gets the number of this card, with 1 representing ace and 11, 12, and 13 representing jack,
   * queen, and king respectively.
   *
   * @return this cards number
   */
  public int getCardNumber();

  /**
   * Determines if this card is red.
   *
   * @return true if this card is red, false if this card is black
   */
  public boolean isRed();

  /**
   * Gets this cards suit.
   *
   * @return this cards suit
   */
  public Suit getSuit();

  /**
   * Prints this cards number and suit.
   *
   * @return this cards number and suit as a string
   */
  public String toString();
}
