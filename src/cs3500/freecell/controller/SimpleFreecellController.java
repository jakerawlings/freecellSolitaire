package cs3500.freecell.controller;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * An implementation of the FreecellController interface that represents the controller for a game
 * of freecell. Serves to process user input and delegate operations to the model and view.
 */
public class SimpleFreecellController implements FreecellController<Card> {

  private final FreecellModel<Card> model;
  private final Readable rd;
  private final Appendable ap;

  /**
   * Constructs a SimpleFreecellController object.
   *
   * @param model the model used by this controller to play the game of freecell
   * @param rd    a readable object used by this controller to read user input
   * @param ap    an appendable object used by this controller to transmit the game to the view
   * @throws IllegalArgumentException if any of the arguments are null
   */
  public SimpleFreecellController(FreecellModel<Card> model, Readable rd, Appendable ap)
      throws IllegalArgumentException {
    if (model == null || rd == null || ap == null) {
      throw new IllegalArgumentException("Invalid parameter");
    }
    this.model = model;
    this.rd = rd;
    this.ap = ap;
  }

  @Override
  public void playGame(List<Card> deck, int numCascades, int numOpens, boolean shuffle)
      throws IllegalStateException, IllegalArgumentException {
    if (deck == null) {
      throw new IllegalArgumentException("Invalid deck");
    }

    try {
      model.startGame(deck, numCascades, numOpens, shuffle);
    } catch (IllegalArgumentException e) {
      printMessage("Could not start game.");
      return;
    }

    drawBoard();
    printMessage("Please enter a move.\n");

    PileType sourcePileType = null;
    int sourcePileNum = -1;
    int cardIndex = -1;
    PileType destPileType = null;
    int destPileNum = -1;
    String current;
    Scanner s = new Scanner(rd);
    int numCycles = 0;

    while (s.hasNext() && !model.isGameOver()) {
      try {
        current = s.next();
      } catch (IllegalStateException | NoSuchElementException e) {
        throw new IllegalStateException("Failed to read from the appendable");
      }

      if (Pattern.matches("q", current)
          || Pattern.matches("Q", current)) {
        printMessage("Game quit prematurely.");
        return;
      } else if (Pattern.matches("[CFO]\\d+", current)) {
        if (sourcePileType == null) {
          sourcePileType = getPileTypeFromInput(current);
          sourcePileNum = getNumFromInput(current);
          numCycles += 1;
        } else if (destPileType == null) {
          destPileType = getPileTypeFromInput(current);
          destPileNum = getNumFromInput(current);
          numCycles += 1;
        }
      } else if (Pattern.matches("\\d+", current)) {
        cardIndex = Integer.parseInt(current);
        numCycles += 1;
      }

      if (numCycles == 3) {
        checkValidInput(sourcePileType, cardIndex, destPileType);
      }

      if (sourcePileType != null && sourcePileNum != -1
          && cardIndex != -1 && destPileType != null) {
        try {
          model.move(sourcePileType, sourcePileNum, cardIndex, destPileType, destPileNum);
          drawBoard();
          printMessage("Please enter a move.\n");
        } catch (IllegalArgumentException e) {
          printMessage("Invalid move. Try again.\n");
        }
        sourcePileType = null;
        sourcePileNum = -1;
        cardIndex = -1;
        destPileType = null;
        destPileNum = -1;
        numCycles = 0;
        if (model.isGameOver()) {
          printMessage("Game over.");
          return;
        }
      }
    }

    if (!s.hasNext()) {
      throw new IllegalStateException("Failed to read from appendable");
    }
  }

  private PileType getPileTypeFromInput(String input) throws IllegalArgumentException {
    switch (input.charAt(0)) {
      case 'C':
        return PileType.CASCADE;
      case 'F':
        return PileType.FOUNDATION;
      case 'O':
        return PileType.OPEN;
      default:
        throw new IllegalArgumentException("Invalid pile type.");
    }
  }

  private int getNumFromInput(String input) {
    String num = input.substring(1);
    return Integer.parseInt(num) - 1;
  }

  private void checkValidInput(PileType sourcePileType, int cardIndex,
      PileType destPileType) {
    if (sourcePileType == null) {
      printMessage("Invalid source pile type. "
          + "Please enter a valid source pile type.\n");
    } else if (cardIndex == -1) {
      printMessage("Invalid card index. "
          + "Please enter a valid card index.\n");
    } else if (destPileType == null) {
      printMessage("Invalid destination pile type. "
          + "Please enter a valid destination pile type.\n");
    }
  }

  private void printMessage(String message) {
    FreecellView view = new FreecellTextView(model, ap);
    try {
      view.renderMessage(message);
    } catch (IOException e) {
      throw new IllegalStateException("Invalid appendable");
    }
  }

  private void drawBoard() {
    FreecellView view = new FreecellTextView(model, ap);
    try {
      view.renderBoard();
    } catch (IOException e) {
      throw new IllegalStateException("Invalid appendable");
    }
  }
}
