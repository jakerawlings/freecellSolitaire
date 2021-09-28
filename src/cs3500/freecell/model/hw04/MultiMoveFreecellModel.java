package cs3500.freecell.model.hw04;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.SimpleFreecellModel;

/**
 * Implementation of the model for a game of freecell that allows for the movement of multiple cards
 * at a time.
 */
public class MultiMoveFreecellModel extends SimpleFreecellModel implements FreecellModel<Card> {

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
      int destPileNumber) throws IllegalArgumentException, IllegalStateException {
    if (!hasStarted) {
      throw new IllegalStateException("Game has not started");
    } else if ((source == PileType.OPEN)
        || (destination == PileType.OPEN)
        || (source == PileType.CASCADE
        && cardIndex == (getNumCardsInCascadePile(pileNumber)) - 1)) {
      super.move(source, pileNumber, cardIndex, destination, destPileNumber);
    } else if ((destination == PileType.CASCADE)
        && super.isValidMove(source, pileNumber, cardIndex, destination, destPileNumber)) {
      isValidBuild(pileNumber, cardIndex);
      enoughIntermediateSlots(pileNumber, cardIndex);
      moveBuild(pileNumber, cardIndex, destPileNumber);
    } else {
      throw new IllegalArgumentException("Invalid move");
    }
  }

  private void isValidBuild(int pileNumber, int cardIndex) {
    for (int i = cardIndex + 1; i < getNumCardsInCascadePile(pileNumber); i += 1) {
      Card prevCard = getCascadeCardAt(pileNumber, i - 1);
      Card curCard = getCascadeCardAt(pileNumber, i);
      if ((prevCard.isRed() == curCard.isRed())
          || (prevCard.getCardNumber() != curCard.getCardNumber() + 1)) {
        throw new IllegalArgumentException("Invalid build");
      }
    }
  }

  private void moveBuild(int pileNumber, int cardIndex, int destPileNumber) {
    for (int i = cardIndex; i < getNumCardsInCascadePile(pileNumber); i += 1) {
      Card toMove = getCascadeCardAt(pileNumber, i);
      cascadePiles.get(destPileNumber).add(toMove);
    }
    int numCardsToMove = getNumCardsInCascadePile(pileNumber) - cardIndex;
    for (int j = 0; j < numCardsToMove; j += 1) {
      Card toRemove = getCascadeCardAt(pileNumber, cardIndex);
      cascadePiles.get(pileNumber).remove(toRemove);
    }
  }

  private void enoughIntermediateSlots(int pileNumber, int cardIndex) {
    int numCardsToMove = getNumCardsInCascadePile(pileNumber) - cardIndex;
    int numFreeOpenPiles = 0;
    int numFreeCascadePiles = 0;
    for (int i = 0; i < getNumOpenPiles(); i += 1) {
      if (getNumCardsInOpenPile(i) == 0) {
        numFreeOpenPiles += 1;
      }
    }
    for (int j = 0; j < getNumCascadePiles(); j += 1) {
      if (getNumCardsInCascadePile(j) == 0) {
        numFreeCascadePiles += 1;
      }
    }
    if (numCardsToMove > ((numFreeOpenPiles + 1) * (int) Math.pow(2, numFreeCascadePiles))) {
      throw new IllegalArgumentException("Not enough intermediate slots to do multi-move");
    }
  }
}

