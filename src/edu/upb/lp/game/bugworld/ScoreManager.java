package edu.upb.lp.game.bugworld;

import edu.upb.lp.game.core.GameUI;
import edu.upb.lp.game.core.StorageManager;

public class ScoreManager {

    private static final String HIGH_SCORE_KEY = "highScore";
    private static final String HIGH_SCORE_NAME_KEY = "highScoreName";

    private final GameUI ui;
    private final StorageManager storage;

    public ScoreManager(GameUI ui, StorageManager storage) {
        this.ui = ui;
        this.storage = storage;
    }

    public int getHighScore() {
        return storage.retrieveInt(HIGH_SCORE_KEY);
    }

    public String getHighScoreName() {
        String name = storage.retrieveString(HIGH_SCORE_NAME_KEY);

        if (name.isBlank()) {
            return "No player";
        } else {
        		return name;	
        }
    }

    public void checkHighScore(int currentScore) {
        int highScore = getHighScore();

        if (currentScore > highScore) {
            String playerName = ui.askText("New high score! Enter your name:");

            if (playerName == null || playerName.isBlank()) {
                playerName = "Anonymous";
            }

            storage.storeInt(HIGH_SCORE_KEY, currentScore);
            storage.storeString(HIGH_SCORE_NAME_KEY, playerName);

            ui.showTemporaryMessage("New high score: " + currentScore + " by " + playerName);
        } else {
            ui.showTemporaryMessage("High score: " + highScore + " by " + getHighScoreName());
        }
    }
}