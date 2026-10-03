package edu.upb.lp.game.bugworld;

import edu.upb.lp.game.core.GameController;
import edu.upb.lp.game.core.GraphicsLibrary;
import edu.upb.lp.game.core.MessagesLibrary;
import edu.upb.lp.game.core.SoundLibrary;
import edu.upb.lp.game.core.StorageLibrary;
import edu.upb.lp.game.core.TimeLibrary;
import edu.upb.lp.game.core.MainLibrary;

public class BugWorldController implements GameController {

	private static final String BTN_RESTART = "Restart";
	private static final String BTN_PASS_DAY = "Pass Day";
	private static final String BTN_BUY_FOOD = "Buy Food";
	private static final String BTN_SELL_BUG = "Sell Bug";
	private static final String BTN_CLEAN_CELL = "Clean Cell";

	private GraphicsLibrary graphics;
	private MessagesLibrary messages;
	private TimeLibrary time;
	private SoundLibrary sound;
	private StorageLibrary storage;
	private BugWorldGame world;
	private ScoreManager scoreManager;

	private int selectedRow = -1;
	private int selectedCol = -1;

	private String dayLoopId;

	public BugWorldController() {
		this.world = new BugWorldGame(this);
	}

	@Override
	public void setLibrary(MainLibrary lib) {
		this.graphics = lib.getGraphics();
		this.messages = lib.getMessages();
		this.time = lib.getTime();
		this.sound = lib.getSound();
		this.storage = lib.getStorage();
		this.scoreManager = new ScoreManager(messages, storage);
	}

	@Override
	public void initialiseInterface() {
		graphics.configureGrid(world.getRows(), world.getCols());

		graphics.addButton(BTN_RESTART);
		graphics.addButton(BTN_PASS_DAY);
		graphics.addButton(BTN_BUY_FOOD);
		startLoop();
		updateInterface();
	}

	private void startLoop() {
		dayLoopId = time.executeRepeatedly(new AutomaticDayPasser(this), 10000);
		/*
		 * dayLoopId = time.executeRepeatedly(() -> { automaticDay(); }, 1000);
		 */
	}

	private void stopLoop() {
		time.stopLoop(dayLoopId);
	}

	public void automaticDay() {
		if (world.automaticDay()) {
			showDayPassed();
			updateInterface();
		}
	}

	@Override
	public void onButtonPressed(String name) {
		sound.playSound("click");
		switch (name) {
		case BTN_RESTART:
			restartGame();
			break;

		case BTN_PASS_DAY:
			world.day();
			showDayPassed();
			break;

		case BTN_BUY_FOOD:
			if (!world.canAffordFood()) {
				messages.showTemporaryMessage("Not enough money.");
			} else if (world.buyFood()) {
				showDayPassed();
			} else {
				messages.showTemporaryMessage("No more room for food!");
			}
			break;

		case BTN_SELL_BUG:
			if (hasSelectedCell() && world.sellBug(selectedRow, selectedCol)) {
				showDayPassed();
			}
			break;

		case BTN_CLEAN_CELL:
			if (hasSelectedCell()) {
				world.cleanCell(selectedRow, selectedCol);
				showDayPassed();
			}
			break;
		}

		updateInterface();
	}

	public void bugDied(int row, int col, String reason) {
		messages.showTemporaryMessage("Bug at (" + row + "," + col + ") died: " + reason);
	}

	public void bugBorn(int row, int col) {
		messages.showTemporaryMessage("A bug was born in the position (" + row + "," + col + ")");
	}

	public void noRoomForBaby(int row, int col) {
		messages.showTemporaryMessage("The bug in position (" + row + "," + col + ") is trying to have a baby, but there is no room!");
	}

	private void showDayPassed() {
		messages.showTemporaryMessage("A day has passed.");
	}

	@Override
	public void onCellPressed(int row, int col) {
		if (selectedRow == row && selectedCol == col) {
			clearSelection();
		} else {
			if (canMoveSelectedBugTo(row, col) && world.moveBug(selectedRow, selectedCol, row, col)) {
				showDayPassed();
			}

			selectedRow = row;
			selectedCol = col;
		}

		updateInterface();
	}

	@Override
	public void onKeyPressed(String key) {
		int row = selectedRow;
		int col = selectedCol;

		switch (key) {
		case "UP":
			row--;
			break;
		case "DOWN":
			row++;
			break;
		case "LEFT":
			col--;
			break;
		case "RIGHT":
			col++;
			break;
		default:
			return;
		}

		boolean insideGrid = row >= 0 && row < world.getRows() && col >= 0 && col < world.getCols();

		if (insideGrid && canMoveSelectedBugTo(row, col) && world.moveBug(selectedRow, selectedCol, row, col)) {
			selectedRow = row;
			selectedCol = col;
			showDayPassed();
			updateInterface();
		}
	}

	private void restartGame() {
		stopLoop();
		scoreManager.checkHighScore(world.getScore());

		world = new BugWorldGame(this);
		startLoop();
		clearSelection();
		updateInterface();
	}
	
	

	private boolean canMoveSelectedBugTo(int row, int col) {
		if (!hasSelectedCell()) {
			return false;
		}

		Cell selectedCell = world.getCell(selectedRow, selectedCol);

		boolean selectedCellHasBug = selectedCell.isBugAlive();
		boolean destinationIsClose = Math.abs(selectedRow - row) <= 1 && Math.abs(selectedCol - col) <= 1;

		boolean sameCell = selectedRow == row && selectedCol == col;

		return selectedCellHasBug && destinationIsClose && !sameCell;
	}

	private boolean hasSelectedCell() {
		return selectedRow >= 0 && selectedCol >= 0;
	}

	private void clearSelection() {
		selectedRow = -1;
		selectedCol = -1;
	}

	public void updateInterface() {
		updateLabels();
		updateCells();
		updateActionButtons();
	}

	private void updateLabels() {
		graphics.setLabel("score", "Score: " + world.getScore());
		graphics.setLabel("money", "Money: " + world.getMoney());
		graphics.setLabel("foodPrice", "Food price: " + world.getFoodPrice());
		graphics.setLabel("highScore", "High score: " + scoreManager.getHighScore() + " (" + scoreManager.getHighScoreName() + ")");
		if (hasSelectedCell()) {
			Cell cell = world.getCell(selectedRow, selectedCol);

			if (cell.isBugAlive()) {
				Bug bug = cell.getBug();
				graphics.setLabel("selected", "Selected: Bug");
				graphics.setLabel("age", "Age: " + bug.getAge());
				graphics.setLabel("hunger", "Hunger: " + bug.getHunger());
				graphics.setLabel("fun", "Fun: " + bug.getFun());
			} else if (cell.hasBug()) {
				graphics.setLabel("selected", "Selected: Dead bug");
				graphics.setLabel("age", "Age: -");
				graphics.setLabel("hunger", "Hunger: -");
				graphics.setLabel("fun", "Fun: -");
			} else if (cell.getFood() > 0) {
				graphics.setLabel("selected", "Selected: Food");
				graphics.setLabel("age", "Food: " + cell.getFood());
				graphics.setLabel("hunger", "Hunger: -");
				graphics.setLabel("fun", "Fun: -");
			} else {
				graphics.setLabel("selected", "Selected: Empty cell");
				graphics.setLabel("age", "Age: -");
				graphics.setLabel("hunger", "Hunger: -");
				graphics.setLabel("fun", "Fun: -");
			}
		} else {
			graphics.setLabel("selected", "Selected: none");
			graphics.setLabel("age", "Age: -");
			graphics.setLabel("hunger", "Hunger: -");
			graphics.setLabel("fun", "Fun: -");
		}
	}

	private void updateCells() {
		for (int row = 0; row < world.getRows(); row++) {
			for (int col = 0; col < world.getCols(); col++) {
				renderCell(row, col);
			}
		}
	}

	private void renderCell(int row, int col) {
		Cell cell = world.getCell(row, col);
		boolean selected = selectedRow == row && selectedCol == col;

		if (selected) {
			graphics.setCellBackgroundImage(row, col, "colors_blue");
		} else {
			graphics.setCellBackgroundImage(row, col, "colors_grey");
		}

		graphics.clearCellObjectImage(row, col);
		graphics.setCellText(row, col, "");

		if (cell.isEmpty()) {
			return;
		}

		if (cell.getFood() > 0) {
			graphics.setCellObjectImage(row, col, "food");
			graphics.setCellText(row, col, String.valueOf(cell.getFood()));
			return;
		}

		Bug bug = cell.getBug();

		if (bug == null) {
			return;
		}

		if (bug.isDead()) {
			graphics.setCellObjectImage(row, col, "bugs_dead_bug");
		} else if (bug.getHunger() > 10) {
			graphics.setCellObjectImage(row, col, "bugs_hungry_bug");
		} else if (bug.getFun() < 10) {
			graphics.setCellObjectImage(row, col, "bugs_sad_bug");
		} else if (bug.getAge() > 15) {
			graphics.setCellObjectImage(row, col, "bugs_old_bug");
		} else {
			graphics.setCellObjectImage(row, col, "bugs_happy_bug");
		}
	}

	private void updateActionButtons() {
		graphics.removeButton(BTN_SELL_BUG);
		graphics.removeButton(BTN_CLEAN_CELL);

		if (!hasSelectedCell()) {
			return;
		}

		Cell cell = world.getCell(selectedRow, selectedCol);

		if (cell.isBugAlive()) {
			graphics.addButton(BTN_SELL_BUG);
		} else if (!cell.isEmpty()) {
			graphics.addButton(BTN_CLEAN_CELL);
		}
	}
}