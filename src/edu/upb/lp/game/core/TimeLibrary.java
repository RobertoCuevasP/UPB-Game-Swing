package edu.upb.lp.game.core;

/**
 * Runs code after a delay or at regular intervals, for example to move
 * enemies or count down a timer.
 * <p>
 * The code runs on the Swing event thread, the same thread as the
 * {@link GameController} methods. It can safely update the graphics, and it
 * never runs at the same time as other game code.
 */
public interface TimeLibrary {

    /**
     * Runs code once, after a delay. This cannot be cancelled.
     *
     * @param runnable     the code to run, for example {@code () -> moveEnemy()}
     * @param milliseconds how long to wait before running it (1000 = 1 second)
     */
    void executeLater(Runnable runnable, int milliseconds);

    /**
     * Runs code again and again, with a fixed delay between runs. The first
     * run happens after one delay. It continues until
     * {@link #stopLoop(String)} is called with the returned id.
     *
     * @param runnable     the code to run, for example {@code () -> nextTurn()}
     * @param milliseconds the delay between runs (1000 = 1 second)
     * @return an id for this loop; keep it to stop the loop later
     */
    String executeRepeatedly(Runnable runnable, int milliseconds);

    /**
     * Stops a loop started with {@link #executeRepeatedly(Runnable, int)}.
     * Does nothing if the loop was already stopped or the id is unknown.
     *
     * @param loopId the id returned by {@code executeRepeatedly}
     */
    void stopLoop(String loopId);
}
