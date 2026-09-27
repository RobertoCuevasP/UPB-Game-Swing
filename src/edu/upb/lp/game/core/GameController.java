package edu.upb.lp.game.core;

public interface GameController {

    void onButtonPressed(String name);

    void onCellPressed(int row, int col);

    void initialiseInterface();
}