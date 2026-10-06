package edu.upb.lp.game.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

import edu.upb.lp.game.core.SoundLibrary;

public class SwingSound implements SoundLibrary {

	private final List<Clip> activeClips = new ArrayList<>();

	@Override
	public void playSound(String soundName) {
	    if (soundName == null || soundName.trim().isEmpty()) {
	        return;
	    }

	    try {
	        URL soundUrl = getClass().getResource("/sounds/" + soundName + ".wav");

	        if (soundUrl == null) {
	            System.out.println("Sound not found: " + soundName);
	            return;
	        }

	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);
	        Clip clip = AudioSystem.getClip();

	        clip.open(audioInputStream);
	        clip.start();

	        activeClips.add(clip);

	        clip.addLineListener(event -> {
	            if (event.getType() == LineEvent.Type.STOP) {
	                clip.close();
	                activeClips.remove(clip);
	            }
	        });

	    } catch (Exception e) {
	        System.out.println("Error playing sound: " + soundName);
	        e.printStackTrace();
	    }
	}

	@Override
	public void stopSounds() {
	    for (Clip clip : new ArrayList<>(activeClips)) {
	        if (clip.isRunning()) {
	            clip.stop();
	        }
	        clip.close();
	    }

	    activeClips.clear();
	}
}