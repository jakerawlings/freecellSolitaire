package cs3500.freecell.model.hw02;

import java.util.Objects;

/**
 * This is the implementation of the Card interface.
 */
public class CardImpl implements Card {

  private final int index;
  private final Suit suit;

  /**
   * Constructs a Card object.
   *
   * @param index a number 0 through 51 representing which card this object represents. The first 13
   *              numbers represents diamonds, the second represent hearts, the third represent
   *              clubs, and the final 13 numbers represent spades.
   * @throws IllegalArgumentException if the given number is less than 0 or greater than 51
   */
  public CardImpl(int index) throws IllegalArgumentException {
    if (index > -1 && index <= 12) {
      this.suit = Suit.DIAMONDS;
    } else if (index > 12 && index <= 25) {
      this.suit = Suit.HEARTS;
    } else if (index > 25 && index <= 38) {
      this.suit = Suit.CLUBS;
    } else if (index > 38 && index <= 51) {
      this.suit = Suit.SPADES;
    } else {
      throw new IllegalArgumentException("Invalid index");
    }
    this.index = index;
  }

  @Override
  public int getCardNumber() {
    return index % 13 + 1;
  }

  @Override
  public boolean isRed() {
    return index < 26;
  }

  @Override
  public Suit getSuit() {
    return suit;
  }

  @Override
  public String toString() {
    String out = "";
    int number = getCardNumber();
    if (number >= 2 && number <= 10) {
      out += getCardNumber();
    } else {
      switch (number) {
        case 1:
          out += "A";
          break;
        case 11:
          out += "J";
          break;
        case 12:
          out += "Q";
          break;
        case 13:
          out += "K";
          break;
        default:
          break;
      }
    }
    switch (suit) {
      case DIAMONDS:
        out += "♦";
        break;
      case HEARTS:
        out += "♥";
        break;
      case CLUBS:
        out += "♣";
        break;
      case SPADES:
        out += "♠";
        break;
      default:
        break;
    }
    return out;
  }

  /**
   * Determines if this CardImpl is equal to the given object; In order for the two objects to be
   * equal, other must be a CardImpl and must have the same index and suit as this CardImpl.
   *
   * @param other an object
   * @return true of this CardImpl is equal to the given object, false otherwise
   */
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (other instanceof Card) {
      return ((CardImpl) other).index == this.index
          && ((CardImpl) other).suit == this.suit;
    } else {
      return false;
    }
  }

  /**
   * Returns an integer unique to this CardImpl based on its index and suit.
   *
   * @return a hash code value for this CardImpl
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.index, this.suit);
  }
}
