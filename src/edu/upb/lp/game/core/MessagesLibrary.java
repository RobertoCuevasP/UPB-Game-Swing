package edu.upb.lp.game.core;

/**
 * Shows messages to the player and asks them to type text.
 */
public interface MessagesLibrary {

    /**
     * Shows a message in a dialog window. The game waits until the player
     * closes the dialog.
     *
     * @param msg the message to show
     */
    void showMessage(String msg);

    /**
     * Shows a short message at the bottom of the window for about 2.5
     * seconds. The game keeps running.
     *
     * @param msg the message to show
     */
    void showTemporaryMessage(String msg);

    /**
     * Opens a dialog where the player can type text. The game waits until the
     * player answers.
     *
     * @param title the question shown to the player
     * @return the text typed by the player, or {@code null} if they cancelled
     */
    String askText(String title);
}
