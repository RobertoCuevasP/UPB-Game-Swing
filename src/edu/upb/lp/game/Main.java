package edu.upb.lp.game;

import edu.upb.lp.game.bugworld.BugWorldController;
import edu.upb.lp.game.core.GameController;
import edu.upb.lp.game.core.MainLibrary;
import edu.upb.lp.game.internal.MainSwingLibrary;

public class Main {
    public static void main(String[] args) {
        GameController controller = new BugWorldController();
        MainLibrary lib = new MainSwingLibrary(controller);

        controller.setLibrary(lib);
        controller.initialiseInterface();
    }
}
