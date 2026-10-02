package edu.upb.lp.game.internal;

import java.io.*;
import java.util.Properties;

import edu.upb.lp.game.core.StorageLibrary;

public class FileStorage implements StorageLibrary {

	private static final String FILE_NAME = "game-data.properties";
	private final Properties properties = new Properties();

	public FileStorage() {
		load();
	}

	private void load() {
		File file = new File(FILE_NAME);

		if (file.exists()) {
			try (FileInputStream input = new FileInputStream(file)) {
				properties.load(input);
			} catch (IOException e) {
				System.out.println("Could not load saved data.");
			}
		}
	}

	private void save() {
		try (FileOutputStream output = new FileOutputStream(FILE_NAME)) {
			properties.store(output, "UPBGame saved data");
		} catch (IOException e) {
			System.out.println("Could not save data.");
		}
	}

	@Override
	public void storeString(String key, String value) {
		properties.setProperty(key, value);
		save();
	}

	@Override
	public String retrieveString(String key) {
		return properties.getProperty(key, "");
	}

	@Override
	public void storeInt(String key, int value) {
		properties.setProperty(key, String.valueOf(value));
		save();
	}

	@Override
	public int retrieveInt(String key) {
		String value = properties.getProperty(key, "0");

		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public void storeBoolean(String key, boolean value) {
		properties.setProperty(key, String.valueOf(value));
		save();
	}

	@Override
	public boolean retrieveBoolean(String key) {
		return Boolean.parseBoolean(properties.getProperty(key, "false"));
	}
}