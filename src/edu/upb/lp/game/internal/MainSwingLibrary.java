package edu.upb.lp.game.internal;

import edu.upb.lp.game.core.GameController;
import edu.upb.lp.game.core.GraphicsLibrary;
import edu.upb.lp.game.core.MainLibrary;
import edu.upb.lp.game.core.MessagesLibrary;
import edu.upb.lp.game.core.SoundLibrary;
import edu.upb.lp.game.core.StorageLibrary;
import edu.upb.lp.game.core.TimeLibrary;

public class MainSwingLibrary implements MainLibrary {

	private final SwingWindow window = new SwingWindow();
	private final TimeLibrary time = new SwingTime();
	private final SoundLibrary sound = new SwingSound();
	private final StorageLibrary storage = new FileStorage();

	public MainSwingLibrary(GameController controller) {
		window.setController(controller);
	}

	@Override
	public GraphicsLibrary getGraphics() {
		return window;
	}

	@Override
	public MessagesLibrary getMessages() {
		return window;
	}

	@Override
	public TimeLibrary getTime() {
		return time;
	}

	@Override
	public SoundLibrary getSound() {
		return sound;
	}

	@Override
	public StorageLibrary getStorage() {
		return storage;
	}
}
