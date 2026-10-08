package edu.upb.lp.game.bugworld;

import edu.upb.lp.game.core.MessagesLibrary;
import edu.upb.lp.game.core.StorageLibrary;

public class ScoreManager {

    private static final String HIGH_SCORE_KEY = "highScore";
    private static final String HIGH_SCORE_NAME_KEY = "highScoreName";

    private final MessagesLibrary messages;
    private final StorageLibrary storage;

    public ScoreManager(MessagesLibrary messages, StorageLibrary storage) {
        this.messages = messages;
        this.storage = storage;
    }

    public int getHighScore() {
        return storage.retrieveInt(HIGH_SCORE_KEY);
    }

    public String getHighScoreName() {
        String name = storage.retrieveString(HIGH_SCORE_NAME_KEY);

        if (name.trim().isEmpty()) {
            return "No player";
        } else {
        		return name;	
        }
    }

    public void checkHighScore(int currentScore) {
        int highScore = getHighScore();

        if (currentScore > highScore) {
            String playerName = messages.askText("New high score! Enter your name:");

            if (playerName == null || playerName.trim().isEmpty()) {
                playerName = "Anonymous";
            }

            storage.storeInt(HIGH_SCORE_KEY, currentScore);
            storage.storeString(HIGH_SCORE_NAME_KEY, playerName);

            messages.showTemporaryMessage("New high score: " + currentScore + " by " + playerName);
        } else {
            messages.showTemporaryMessage("High score: " + highScore + " by " + getHighScoreName());
        }
    }
}