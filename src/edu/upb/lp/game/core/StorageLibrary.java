package edu.upb.lp.game.core;

public interface StorageLibrary {

    void storeString(String key, String value);

    String retrieveString(String key);

    void storeInt(String key, int value);

    int retrieveInt(String key);

    void storeBoolean(String key, boolean value);

    boolean retrieveBoolean(String key);
}
