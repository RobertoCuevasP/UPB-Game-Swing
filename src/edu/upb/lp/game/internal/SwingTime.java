package edu.upb.lp.game.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.swing.Timer;

import edu.upb.lp.game.core.TimeLibrary;

public class SwingTime implements TimeLibrary {

	private final Map<String, Timer> loops = new HashMap<>();

	@Override
	public void executeLater(Runnable runnable, int milliseconds) {
		Timer timer = new Timer(milliseconds, e -> runnable.run());
		timer.setRepeats(false);
		timer.start();
	}

	@Override
	public String executeRepeatedly(Runnable runnable, int milliseconds) {
		String loopId = UUID.randomUUID().toString();

		Timer timer = new Timer(milliseconds, e -> runnable.run());
		timer.start();

		loops.put(loopId, timer);

		return loopId;
	}

	@Override
	public void stopLoop(String loopId) {
		Timer timer = loops.remove(loopId);

		if (timer != null) {
			timer.stop();
		}
	}
}
