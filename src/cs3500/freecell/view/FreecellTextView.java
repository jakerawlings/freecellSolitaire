package cs3500.freecell.view;

import cs3500.freecell.model.FreecellModelState;
import cs3500.freecell.model.PileType;
import java.io.IOException;

/**
 * This is the implementation of the view for the game of FreeCell.
 */
public class FreecellTextView implements FreecellView {

  private final FreecellModelState<?> model;
  private final Appendable ap;

  /**
   * Creates a new FreeCellTextView object with the given FreecellModel object.
   * @param model a FreecellModel object that represents the model that this FreeCellTextView
   *              object will visualize
   */
  public FreecellTextView(FreecellModelState<?> model) throws IllegalArgumentException {
    // added check for null args
    if (model == null) {
      throw new IllegalArgumentException("model cannot be null");
    }
    this.model = model;
    this.ap = System.out;
  }

  /**
   * Creates a new FreeCellTextView object with the given FreecellModel and Appendable objects.
   * @param model a FreecellModel object that represents the model that this FreeCellTextView
   * @param ap an Appendable object allowing this FreeCellTextView object to write to any
   *           specified Appendable object
   */
  public FreecellTextView(FreecellModelState<?> model, Appendable ap) {
    // added check for null args
    if (model == null || ap == null) {
      throw new IllegalArgumentException("args cannot be null");
    }
    this.model = model;
    this.ap = ap;
  }

  @Override
  public String toString() {
    try {
      model.getNumCardsInFoundationPile(0);
    } catch (IllegalStateException e) {
      return "";
    }
    StringBuilder out = new StringBuilder();
    for (int i = 0; i < 4; i += 1) {
      int l = i + 1;
      out.append("F" + l + ":"
          + toStringHelper(i, PileType.FOUNDATION) + "\n");
    }
    for (int k = 0; k < model.getNumOpenPiles(); k += 1) {
      int l = k + 1;
      out.append("O" + l + ":"
          + toStringHelper(k, PileType.OPEN) + "\n");
    }
    for (int j = 0; j < model.getNumCascadePiles() - 1; j += 1) {
      int l = j + 1;
      out.append("C" + l + ":"
          + toStringHelper(j, PileType.CASCADE) + "\n");
    }
    out.append("C" + model.getNumCascadePiles() + ":");
    for (int j = 0; j < model.getNumCardsInCascadePile(
        model.getNumCascadePiles() - 1); j += 1) {
      if (j == 0) {
        out.append(" " + model.getCascadeCardAt(
            model.getNumCascadePiles() - 1, j).toString());
      } else {
        out.append(", " + model.getCascadeCardAt(
            model.getNumCascadePiles() - 1, j).toString());
      }
    }
    return out.toString();
  }

  private String toStringHelper(int pileNumber, PileType pile) {
    StringBuilder out = new StringBuilder();
    switch (pile) {
      case FOUNDATION:
        for (int i = 0; i < model.getNumCardsInFoundationPile(pileNumber); i += 1) {
          if (i == 0) {
            out.append(" " + model.getFoundationCardAt(pileNumber, i).toString());
          } else {
            out.append(", " + model.getFoundationCardAt(pileNumber, i).toString());
          }
        }
        break;
      case CASCADE:
        for (int j = 0; j < model.getNumCardsInCascadePile(pileNumber); j += 1) {
          if (j == 0) {
            out.append(" " + model.getCascadeCardAt(pileNumber, j).toString());
          } else {
            out.append(", " + model.getCascadeCardAt(pileNumber, j).toString());
          }
        }
        break;
      case OPEN:
        if (model.getNumCardsInOpenPile(pileNumber) != 0) {
          out.append(" " + model.getOpenCardAt(pileNumber).toString());
        }
        break;
      default:
        break;
    }
    return out.toString();
  }

  @Override
  public void renderBoard() throws IOException {
    ap.append(toString() + "\n");
  }

  @Override
  public void renderMessage(String message) throws IOException {
    ap.append(message);
  }
}
