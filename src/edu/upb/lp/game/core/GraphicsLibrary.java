package edu.upb.lp.game.core;

/**
 * Draws the game window. The window has labels at the top, a grid of cells
 * in the middle, and buttons at the bottom.
 * <p>
 * Rows and columns start at 0. Row 0 is at the top and column 0 is on the
 * left. A method called with a cell outside the grid does nothing.
 * <p>
 * Images are given by name, without extension. For example, {@code "food"}
 * loads {@code /images/food.png}. Files ending in {@code .png},
 * {@code .jpg} or {@code .jpeg} are found in the {@code images} folder at the
 * root of the classpath. If no image is found, a message is printed in the
 * console and the cell shows no image.
 */
public interface GraphicsLibrary {

    /**
     * Creates a new grid of empty cells, replacing the previous grid (its
     * contents are lost). Also resizes the window.
     *
     * @param rows            the number of rows
     * @param cols            the number of columns
     * @param sizeX           the width of the whole window, in pixels (not the
     *                        width of a cell)
     * @param sizeY           the height of the whole window, in pixels (not the
     *                        height of a cell)
     * @param showCellBorders {@code true} to draw a black border around each cell
     */
    void configureGrid(int rows, int cols, int sizeX, int sizeY, boolean showCellBorders);

    /**
     * Writes text in the middle of a cell, in white bold letters, on top of
     * any images.
     *
     * @param row  the row of the cell
     * @param col  the column of the cell
     * @param text the text to show; {@code null} or {@code ""} removes the text
     */
    void setCellText(int row, int col, String text);

    /**
     * Sets the background image of a cell. The image is stretched to fill the
     * whole cell.
     *
     * @param row       the row of the cell
     * @param col       the column of the cell
     * @param imageName the name of the image, without extension; {@code null}
     *                  or {@code ""} removes the background image
     */
    void setCellBackgroundImage(int row, int col, String imageName);

    /**
     * Sets the object image of a cell, for example a character or an item.
     * It is drawn centred on top of the background image and keeps its
     * proportions.
     *
     * @param row       the row of the cell
     * @param col       the column of the cell
     * @param imageName the name of the image, without extension; {@code null}
     *                  or {@code ""} removes the object image
     */
    void setCellObjectImage(int row, int col, String imageName);

    /**
     * Removes the object image of a cell. The background image and the text
     * stay.
     *
     * @param row the row of the cell
     * @param col the column of the cell
     */
    void clearCellObjectImage(int row, int col);

    /**
     * Adds a button at the bottom of the window. When it is clicked,
     * {@link GameController#onButtonPressed(String)} is called with this name.
     * Does nothing if a button with this name already exists.
     *
     * @param name the text shown on the button, which also identifies it
     */
    void addButton(String name);

    /**
     * Removes a button. Does nothing if no button has this name.
     *
     * @param name the name used in {@link #addButton(String)}
     */
    void removeButton(String name);

    /**
     * Shows a piece of information at the top of the window, such as the
     * score. The first call with a key creates the label. Later calls with
     * the same key change its text.
     *
     * @param key   identifies the label; it is not shown on screen
     * @param value the text shown, for example {@code "Score: 10"}
     */
    void setLabel(String key, String value);
}
