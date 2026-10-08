package edu.upb.lp.game.core;

/**
 * Saves values that stay after the game is closed, such as high scores.
 * <p>
 * Each value is saved under a key (a name you choose). The data is written
 * immediately to the file {@code game-data.properties}, in the folder the
 * game was started from. Use different keys for different kinds of values.
 */
public interface StorageLibrary {

    /**
     * Saves a text value.
     *
     * @param key   the name to save the value under
     * @param value the text to save; must not be {@code null}
     */
    void storeString(String key, String value);

    /**
     * Reads a text value saved with {@link #storeString(String, String)}.
     *
     * @param key the name the value was saved under
     * @return the saved text, or {@code ""} if nothing was saved under this key
     */
    String retrieveString(String key);

    /**
     * Saves a whole number.
     *
     * @param key   the name to save the value under
     * @param value the number to save
     */
    void storeInt(String key, int value);

    /**
     * Reads a number saved with {@link #storeInt(String, int)}.
     *
     * @param key the name the value was saved under
     * @return the saved number, or {@code 0} if nothing was saved under this
     *         key or the saved value is not a number
     */
    int retrieveInt(String key);

    /**
     * Saves a true/false value.
     *
     * @param key   the name to save the value under
     * @param value the value to save
     */
    void storeBoolean(String key, boolean value);

    /**
     * Reads a value saved with {@link #storeBoolean(String, boolean)}.
     *
     * @param key the name the value was saved under
     * @return the saved value, or {@code false} if nothing was saved under
     *         this key
     */
    boolean retrieveBoolean(String key);
}
