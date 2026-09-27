package edu.upb.lp.game.core;

public interface GameUI {

    void configureGrid(int rows, int cols);

    void setCellText(int row, int col, String text);

    void setCellBackgroundImage(int row, int col, String imageName);

    void setCellObjectImage(int row, int col, String imageName);

    void clearCellObjectImage(int row, int col);

    void addButton(String name);

    void removeButton(String name);

    void setLabel(String key, String value);

    void showMessage(String msg);

    void showTemporaryMessage(String msg);

    String askText(String title);

    void executeLater(Runnable runnable, int milliseconds);

    String executeRepeatedly(Runnable runnable, int milliseconds);

    void stopLoop(String loopId);
    
    void playSound(String soundName);
    
    void stopSounds();
}