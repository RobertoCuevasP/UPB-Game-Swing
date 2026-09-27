package edu.upb.lp.game;

import edu.upb.lp.game.bugworld.BugWorldController;
import edu.upb.lp.game.core.GameController;
import edu.upb.lp.game.internal.SwingUI;

public class Main {
    public static void main(String[] args) {
        SwingUI ui = new SwingUI();
        GameController controller = new BugWorldController(ui);

        ui.setController(controller);
        controller.initialiseInterface();
    }
}