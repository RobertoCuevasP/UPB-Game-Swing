package edu.upb.lp.game.core;

public interface TimeLibrary {

    void executeLater(Runnable runnable, int milliseconds);

    String executeRepeatedly(Runnable runnable, int milliseconds);

    void stopLoop(String loopId);
}
