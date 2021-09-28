import cs3500.freecell.controller.FreecellController;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw04.MultiMoveFreecellModel;
import java.io.InputStreamReader;

/**
 * Class to hold the main method.
 */
public class Main {

  /**
   * Creates a game of Freecell and gives control to the controller.
   */
  public static void main(String[] args) {
    FreecellModel<Card> model = new MultiMoveFreecellModel();
    FreecellController<Card> controller =
        new SimpleFreecellController(model, new InputStreamReader(System.in), System.out);
    controller.playGame(model.getDeck(), 4, 1, false);
  }
}
