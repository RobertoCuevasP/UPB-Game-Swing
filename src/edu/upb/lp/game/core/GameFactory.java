package edu.upb.lp.game.core;

import edu.upb.lp.game.bugworld.BugWorldController;

public class GameFactory {

    public static GameController createGame(GameUI ui) {
        return new BugWorldController(ui);
    }
}