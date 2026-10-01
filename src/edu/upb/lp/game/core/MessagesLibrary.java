package edu.upb.lp.game.core;

public interface MessagesLibrary {

    void showMessage(String msg);

    void showTemporaryMessage(String msg);

    String askText(String title);
}
