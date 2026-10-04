package edu.upb.lp.game.core;

/**
 * Plays sounds. Sounds are given by name, without extension. For example,
 * {@code "click"} plays {@code /sounds/click.wav}. Only {@code .wav} files in
 * the {@code sounds} folder at the root of the classpath are found.
 */
public interface SoundLibrary {

    /**
     * Starts playing a sound and returns immediately, without waiting for the
     * sound to finish. Several sounds can play at the same time. If the sound
     * is not found, a message is printed in the console.
     *
     * @param soundName the name of the sound, without extension
     */
    void playSound(String soundName);

    /**
     * Stops all the sounds that are currently playing.
     */
    void stopSounds();
}
