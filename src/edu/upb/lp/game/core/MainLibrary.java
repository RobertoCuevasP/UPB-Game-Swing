package edu.upb.lp.game.core;

/**
 * Gives access to all the parts of the library. Your game receives it in
 * {@link GameController#setLibrary(MainLibrary)}.
 * <p>
 * Each method always returns the same object, so you can call them as often
 * as you like or keep the results in fields.
 */
public interface MainLibrary {

    /**
     * Returns the library used to draw the grid, buttons and labels.
     *
     * @return the graphics library
     */
    GraphicsLibrary getGraphics();

    /**
     * Returns the library used to show messages and ask the player for text.
     *
     * @return the messages library
     */
    MessagesLibrary getMessages();

    /**
     * Returns the library used to run code later or repeatedly.
     *
     * @return the time library
     */
    TimeLibrary getTime();

    /**
     * Returns the library used to play sounds.
     *
     * @return the sound library
     */
    SoundLibrary getSound();

    /**
     * Returns the library used to save data between games, such as high
     * scores.
     *
     * @return the storage library
     */
    StorageLibrary getStorage();
}
