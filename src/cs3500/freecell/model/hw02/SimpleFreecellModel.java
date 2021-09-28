package cs3500.freecell.model.hw02;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the model for a game of freecell in which only single moves are allowed.
 */
public class SimpleFreecellModel implements FreecellModel<Card> {

  // changed all fields from private to protected in order to allow the MultiMoveFreecellModel
  // subclass to have access to them
  protected ArrayList<ArrayList<Card>> foundationPiles;
  protected ArrayList<ArrayList<Card>> cascadePiles;
  protected ArrayList<ArrayList<Card>> openPiles;
  protected boolean hasStarted;

  @Override
  public List<Card> getDeck() {
    List<Card> deck = new ArrayList<Card>();
    for (int i = 0; i < 52; i += 1) {
      deck.add(new CardImpl(i));
    }
    return deck;
  }

  @Override
  public void startGame(List<Card> deck, int numCascadePiles, int numOpenPiles, boolean shuffle)
      throws IllegalArgumentException {
    if (!isValidDeck(deck) || numCascadePiles < 4 || numOpenPiles < 1) {
      throw new IllegalArgumentException("Invalid parameter");
    } else if (shuffle) {
      Collections.shuffle(deck);
    }

    foundationPiles = new ArrayList<ArrayList<Card>>();
    cascadePiles = new ArrayList<ArrayList<Card>>();
    openPiles = new ArrayList<ArrayList<Card>>();

    for (int i = 0; i < 4; i += 1) {
      foundationPiles.add(new ArrayList<Card>());
    }
    for (int j = 0; j < numCascadePiles; j += 1) {
      cascadePiles.add(new ArrayList<Card>());
    }
    for (int k = 0; k < numOpenPiles; k += 1) {
      openPiles.add(new ArrayList<Card>());
    }
    for (int i = 0, j = 0; j < 52; i += 1, j += 1) {
      if (i >= numCascadePiles) {
        i = 0;
      }
      cascadePiles.get(i).add(deck.get(j));
    }
    hasStarted = true;
  }

  private boolean isValidDeck(List<Card> deck) {
    Set<Card> set = new HashSet<Card>(deck);
    return (deck.size() == 52 && set.size() == 52);
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
      int destPileNumber) throws IllegalArgumentException, IllegalStateException {
    if (!hasStarted) {
      throw new IllegalStateException("Game has not started");
    } else if (!isValidMove(source, pileNumber, cardIndex, destination, destPileNumber)) {
      throw new IllegalArgumentException("Invalid move");
    }
    Card toMove;
    if (source == PileType.OPEN) {
      toMove = getOpenCardAt(pileNumber);
      moveHelper(destination, destPileNumber, toMove);
      openPiles.get(pileNumber).remove(toMove);
    } else {
      toMove = getCascadeCardAt(pileNumber, cardIndex);
      moveHelper(destination, destPileNumber, toMove);
      cascadePiles.get(pileNumber).remove(toMove);
    }
  }

  private void moveHelper(PileType destination, int destPileNumber, Card toMove) {
    switch (destination) {
      case OPEN:
        openPiles.get(destPileNumber).add(toMove);
        break;
      case CASCADE:
        cascadePiles.get(destPileNumber).add(toMove);
        break;
      case FOUNDATION:
        foundationPiles.get(destPileNumber).add(toMove);
        break;
      default:
        break;
    }
  }

  // changed this method from private to protected in order to allow the MultiMoveFreecellModel
  // subclass to have access to it
  protected boolean isValidMove(PileType source, int pileNumber, int cardIndex,
      PileType destination,
      int destPileNumber) {
    Card toMove;
    switch (source) {
      case OPEN:
        if (pileNumber >= 0 && pileNumber < getNumOpenPiles() && cardIndex >= 0
            && cardIndex < getNumCardsInOpenPile(pileNumber)) {
          toMove = getOpenCardAt(pileNumber);
          return isValidMoveHelper(toMove, destination, destPileNumber);
        } else {
          return false;
        }
      case CASCADE:
        if (pileNumber >= 0 && pileNumber < getNumCascadePiles() && cardIndex >= 0
            && cardIndex < getNumCardsInCascadePile(pileNumber)) {
          toMove = getCascadeCardAt(pileNumber, cardIndex);
          return isValidMoveHelper(toMove, destination, destPileNumber);
        } else {
          return false;
        }
      case FOUNDATION:
        return false;
      default:
        break;
    }
    return false;
  }

  private boolean isValidMoveHelper(Card toMove, PileType destination, int destPileNumber) {
    Card beforeDestCard;
    switch (destination) {
      case OPEN:
        return getNumCardsInOpenPile(destPileNumber) == 0;
      case CASCADE:
        if (getNumCardsInCascadePile(destPileNumber) == 0) {
          return true;
        } else {
          beforeDestCard = getCascadeCardAt(destPileNumber,
              getNumCardsInCascadePile(destPileNumber) - 1);
          return toMove.isRed() != beforeDestCard.isRed()
              && toMove.getCardNumber() + 1 == beforeDestCard.getCardNumber();
        }
      case FOUNDATION:
        if (foundationPiles.get(destPileNumber).size() == 0 && toMove.getCardNumber() == 1) {
          return true;
        } else if (foundationPiles.get(destPileNumber).size() != 0) {
          beforeDestCard = getFoundationCardAt(destPileNumber,
              getNumCardsInFoundationPile(destPileNumber) - 1);
          return toMove.getCardNumber() - 1 == beforeDestCard.getCardNumber()
              && toMove.getSuit() == beforeDestCard.getSuit();
        } else {
          return false;
        }
      default:
        break;
    }
    return false;
  }

  @Override
  public boolean isGameOver() {
    if (!hasStarted) {
      return true;
    }
    for (int i = 0; i < 4; i += 1) {
      if (getNumCardsInFoundationPile(i) != 13) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int getNumCardsInFoundationPile(int index)
      throws IllegalArgumentException, IllegalStateException {
    if (!hasStarted) {
      throw new IllegalStateException("Game has not started");
    } else if (index < 0 || index >= 4) {
      throw new IllegalArgumentException("Invalid index");
    }
    return foundationPiles.get(index).size();
  }

  @Override
  public int getNumCascadePiles() {
    if (!hasStarted) {
      return -1;
    } else {
      return cascadePiles.size();
    }
  }

  @Override
  public int getNumCardsInCascadePile(int index)
      throws IllegalArgumentException, IllegalStateException {
    if (!hasStarted) {
      throw new IllegalStateException("Game has not started");
    } else if (index < 0 || index >= getNumCascadePiles()) {
      throw new IllegalArgumentException("Invalid index");
    }
    return cascadePiles.get(index).size();
  }

  @Override
  public int getNumCardsInOpenPile(int index)
      throws IllegalArgumentException, IllegalStateException {
    if (!hasStarted) {
      throw new IllegalStateException("Game has not started");
    } else if (index < 0 || index >= getNumOpenPiles()) {
      throw new IllegalArgumentException("Invalid index");
    }
    return openPiles.get(index).size();
  }

  @Override
  public int getNumOpenPiles() {
    if (!hasStarted) {
      return -1;
    } else {
      return openPiles.size();
    }
  }

  @Override
  public Card getFoundationCardAt(int pileIndex, int cardIndex)
      throws IllegalArgumentException, IllegalStateException {
    if (!hasStarted) {
      throw new IllegalStateException("Game has not started");
    } else if (pileIndex < 0 || pileIndex >= 4
        || cardIndex < 0 || cardIndex >= getNumCardsInFoundationPile(pileIndex)) {
      throw new IllegalArgumentException("Invalid index");
    }
    return foundationPiles.get(pileIndex).get(cardIndex);
  }

  @Override
  public Card getCascadeCardAt(int pileIndex, int cardIndex)
      throws IllegalArgumentException, IllegalStateException {
    if (!hasStarted) {
      throw new IllegalStateException("Game has not started");
    } else if (pileIndex < 0 || pileIndex >= getNumCascadePiles()
        || cardIndex < 0 || cardIndex >= getNumCardsInCascadePile(pileIndex)) {
      throw new IllegalArgumentException("Invalid index");
    }
    return cascadePiles.get(pileIndex).get(cardIndex);
  }

  @Override
  public Card getOpenCardAt(int pileIndex) throws
      IllegalArgumentException, IllegalStateException {
    if (!hasStarted) {
      throw new IllegalStateException("Game has not started");
    } else if (pileIndex < 0 || pileIndex >= getNumOpenPiles()) {
      throw new IllegalArgumentException("Invalid index");
    }
    if (openPiles.get(pileIndex).size() != 0) {
      return openPiles.get(pileIndex).get(0);
    } else {
      return null;
    }
  }
}