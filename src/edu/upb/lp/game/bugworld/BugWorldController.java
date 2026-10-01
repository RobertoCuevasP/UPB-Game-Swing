package edu.upb.lp.game.bugworld;

import edu.upb.lp.game.core.GameController;
import edu.upb.lp.game.core.GameUI;
import edu.upb.lp.game.core.StorageManager;

public class BugWorldController implements GameController {

	private static final String BTN_RESTART = "Restart";
	private static final String BTN_PASS_DAY = "Pass Day";
	private static final String BTN_BUY_FOOD = "Buy Food";
	private static final String BTN_SELL_BUG = "Sell Bug";
	private static final String BTN_CLEAN_CELL = "Clean Cell";

	private final GameUI ui;
	private BugWorldGame world;
	private StorageManager storageManager;
	private ScoreManager scoreManager;

	private int selectedRow = -1;
	private int selectedCol = -1;

	private String dayLoopId;

	public BugWorldController(GameUI ui) {
		this.ui = ui;
		this.world = new BugWorldGame(this);
		this.storageManager = new StorageManager();
		this.scoreManager = new ScoreManager(ui, storageManager);
	}

	@Override
	public void initialiseInterface() {
		ui.configureGrid(world.getRows(), world.getCols());

		ui.addButton(BTN_RESTART);
		ui.addButton(BTN_PASS_DAY);
		ui.addButton(BTN_BUY_FOOD);
		startLoop();
		updateInterface();
	}

	private void startLoop() {
		dayLoopId = ui.executeRepeatedly(new AutomaticDayPasser(this), 10000);
		/*
		 * dayLoopId = ui.executeRepeatedly(() -> { automaticDay(); }, 1000);
		 */
	}

	private void stopLoop() {
		ui.stopLoop(dayLoopId);
	}

	public void automaticDay() {
		if (world.automaticDay()) {
			showDayPassed();
			updateInterface();
		}
	}

	@Override
	public void onButtonPressed(String name) {
		ui.playSound("click");
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
				ui.showTemporaryMessage("Not enough money.");
			} else if (world.buyFood()) {
				showDayPassed();
			} else {
				ui.showTemporaryMessage("No more room for food!");
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
		ui.showTemporaryMessage("Bug at (" + row + "," + col + ") died: " + reason);
	}

	public void bugBorn(int row, int col) {
		ui.showTemporaryMessage("A bug was born in the position (" + row + "," + col + ")");
	}

	public void noRoomForBaby(int row, int col) {
		ui.showTemporaryMessage("The bug in position (" + row + "," + col + ") is trying to have a baby, but there is no room!");
	}

	private void showDayPassed() {
		ui.showTemporaryMessage("A day has passed.");
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
		ui.setLabel("score", "Score: " + world.getScore());
		ui.setLabel("money", "Money: " + world.getMoney());
		ui.setLabel("foodPrice", "Food price: " + world.getFoodPrice());
		ui.setLabel("highScore", "High score: " + scoreManager.getHighScore() + " (" + scoreManager.getHighScoreName() + ")");
		if (hasSelectedCell()) {
			Cell cell = world.getCell(selectedRow, selectedCol);

			if (cell.isBugAlive()) {
				Bug bug = cell.getBug();
				ui.setLabel("selected", "Selected: Bug");
				ui.setLabel("age", "Age: " + bug.getAge());
				ui.setLabel("hunger", "Hunger: " + bug.getHunger());
				ui.setLabel("fun", "Fun: " + bug.getFun());
			} else if (cell.hasBug()) {
				ui.setLabel("selected", "Selected: Dead bug");
				ui.setLabel("age", "Age: -");
				ui.setLabel("hunger", "Hunger: -");
				ui.setLabel("fun", "Fun: -");
			} else if (cell.getFood() > 0) {
				ui.setLabel("selected", "Selected: Food");
				ui.setLabel("age", "Food: " + cell.getFood());
				ui.setLabel("hunger", "Hunger: -");
				ui.setLabel("fun", "Fun: -");
			} else {
				ui.setLabel("selected", "Selected: Empty cell");
				ui.setLabel("age", "Age: -");
				ui.setLabel("hunger", "Hunger: -");
				ui.setLabel("fun", "Fun: -");
			}
		} else {
			ui.setLabel("selected", "Selected: none");
			ui.setLabel("age", "Age: -");
			ui.setLabel("hunger", "Hunger: -");
			ui.setLabel("fun", "Fun: -");
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
			ui.setCellBackgroundImage(row, col, "colors_blue");
		} else {
			ui.setCellBackgroundImage(row, col, "colors_grey");
		}

		ui.clearCellObjectImage(row, col);
		ui.setCellText(row, col, "");

		if (cell.isEmpty()) {
			return;
		}

		if (cell.getFood() > 0) {
			ui.setCellObjectImage(row, col, "food");
			ui.setCellText(row, col, String.valueOf(cell.getFood()));
			return;
		}

		Bug bug = cell.getBug();

		if (bug == null) {
			return;
		}

		if (bug.isDead()) {
			ui.setCellObjectImage(row, col, "bugs_dead_bug");
		} else if (bug.getHunger() > 10) {
			ui.setCellObjectImage(row, col, "bugs_hungry_bug");
		} else if (bug.getFun() < 10) {
			ui.setCellObjectImage(row, col, "bugs_sad_bug");
		} else if (bug.getAge() > 15) {
			ui.setCellObjectImage(row, col, "bugs_old_bug");
		} else {
			ui.setCellObjectImage(row, col, "bugs_happy_bug");
		}
	}

	private void updateActionButtons() {
		ui.removeButton(BTN_SELL_BUG);
		ui.removeButton(BTN_CLEAN_CELL);

		if (!hasSelectedCell()) {
			return;
		}

		Cell cell = world.getCell(selectedRow, selectedCol);

		if (cell.isBugAlive()) {
			ui.addButton(BTN_SELL_BUG);
		} else if (!cell.isEmpty()) {
			ui.addButton(BTN_CLEAN_CELL);
		}
	}
}