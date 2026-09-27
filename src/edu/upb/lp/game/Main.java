package edu.upb.lp.game;

import edu.upb.lp.game.core.GameController;
import edu.upb.lp.game.core.GameFactory;
import edu.upb.lp.game.internal.SwingUI;

public class Main {
    public static void main(String[] args) {
        SwingUI ui = new SwingUI();
        GameController controller = GameFactory.createGame(ui);

        ui.setController(controller);
        controller.initialiseInterface();
    }
}