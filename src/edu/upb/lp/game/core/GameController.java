package edu.upb.lp.game.core;

/**
 * The class you write to create your game. The library calls these methods
 * when something happens, such as a click or a key press.
 * <p>
 * At startup, the library first calls {@link #setLibrary(MainLibrary)}, then
 * {@link #initialiseInterface()}. All methods are called on the Swing event
 * thread, one at a time.
 */
public interface GameController {

    /**
     * Called when the player clicks a button created with
     * {@link GraphicsLibrary#addButton(String)}.
     *
     * @param name the name of the button that was clicked (the same text
     *             passed to {@code addButton})
     */
    void onButtonPressed(String name);

    /**
     * Called when the player clicks a cell of the grid.
     *
     * @param row the row of the cell, starting at 0 at the top
     * @param col the column of the cell, starting at 0 on the left
     */
    void onCellPressed(int row, int col);

    /**
     * Called when the player presses a key while the game window is active.
     * Holding a key down may call this method several times.
     *
     * @param key the name of the key in upper case, for example {@code "UP"},
     *            {@code "DOWN"}, {@code "LEFT"}, {@code "RIGHT"}, {@code "A"},
     *            {@code "1"}, {@code "SPACE"} or {@code "ENTER"}
     */
    void onKeyPressed(String key);

    /**
     * Called once at startup, after {@link #setLibrary(MainLibrary)}. Build
     * the starting screen here: configure the grid, add buttons and labels,
     * and so on.
     */
    void initialiseInterface();

    /**
     * Called once at startup, before {@link #initialiseInterface()}. Keep the
     * library in a field so the rest of your game can use it.
     *
     * @param library gives access to graphics, messages, time, sound and
     *                storage
     */
    void setLibrary(MainLibrary library);
}