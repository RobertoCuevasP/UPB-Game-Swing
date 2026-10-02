package edu.upb.lp.game.core;

public interface GraphicsLibrary {

    void configureGrid(int rows, int cols);

    void setCellText(int row, int col, String text);

    void setCellBackgroundImage(int row, int col, String imageName);

    void setCellObjectImage(int row, int col, String imageName);

    void clearCellObjectImage(int row, int col);

    void addButton(String name);

    void removeButton(String name);

    void setLabel(String key, String value);
}
