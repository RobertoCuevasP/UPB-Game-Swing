package edu.upb.lp.game.bugworld;

import edu.upb.lp.game.core.GameUI;

public class BugWorldGame {

	private final int rows = 8;
	private final int cols = 8;

	private Cell[][] cells = new Cell[rows][cols];

	private GameUI ui;
	private BugWorldController controller;

	private int money = 100;
	private int score = 0;
	private int foodPrice = 10;

	private boolean manualDayPassed = false;
	private String dayLoopId;

	public BugWorldGame(GameUI ui, BugWorldController controller) {
		this.ui = ui;
		this.controller = controller;
		initialiseWorld();
	}

	private void initialiseWorld() {

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				cells[r][c] = new Cell(r, c, this);
			}
		}

		cells[3][3].createBug();
		cells[3][4].createBug();
	}

	public void startLoop() {
		dayLoopId = ui.executeRepeatedly(new AutomaticDayPasser(this), 1000);
		/*
		 * dayLoopId = ui.executeRepeatedly(() -> { world.day(); updateInterface(); },
		 * 10000);
		 */
	}

	public void stopLoop() {
		ui.stopLoop(dayLoopId);
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public Cell getCell(int row, int col) {
		return cells[row][col];
	}

	public int getMoney() {
		return money;
	}

	public int getScore() {
		return score;
	}

	public int getFoodPrice() {
		return foodPrice;
	}

	public void day() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				cells[r][c].day();
			}
		}
		ui.showTemporaryMessage("A day has passed.");
		manualDayPassed = true;
	}

	public void automaticDay() {
		if (!manualDayPassed) {
			day();
			controller.updateInterface();
		}
		manualDayPassed = false;
	}

	public void buyFood() {

		if (money < foodPrice) {
			ui.showTemporaryMessage("Not enough money.");
		} else {
			int r = (int) (Math.random() * rows);
			int c = (int) (Math.random() * cols);
			int currentr = r;
			int currentc = c;
			boolean found = false;
			do {
				if (cells[currentr][currentc].isEmpty()) {
					cells[currentr][currentc].setFood(20);
					money -= foodPrice;
					foodPrice++;
					day();
					found = true;
				} else {
					if (currentc == c - 1) {
						currentc = 0;
						if (currentr == r -1) {
							currentr = 0;
						} else {
							currentr++;
						}
					} else {
						currentc++;
					}
				}
			} while ((currentr != r || currentc != c) && !found);
			if (!found) {
				ui.showTemporaryMessage("No more room for food!");
			}
		}
	}

	public void moveBug(int fromRow, int fromCol, int toRow, int toCol) {

		Cell origin = cells[fromRow][fromCol];
		Cell target = cells[toRow][toCol];

		if (origin.isBugAlive() && target.isEmpty()) {

			target.setBug(origin.getBug());
			origin.setBug(null);
			origin.setFood(0);
			day();
		}
	}

	public void sellBug(int row, int col) {

		Cell cell = cells[row][col];

		if (cell.isBugAlive()) {
			money += cell.getBug().price();

			cell.setBug(null);

			day();
		}
	}

	public void cleanCell(int row, int col) {
		cells[row][col].setBug(null);
		cells[row][col].setFood(0);

		day();
	}

	public boolean tryToEat(int row, int col) {

		return eat(row - 1, col) || eat(row + 1, col) || eat(row, col - 1) || eat(row, col + 1);
	}

	private boolean eat(int row, int col) {

		if (inside(row, col)) {
			return cells[row][col].eat();
		}

		return false;
	}

	public boolean hasCloseFriend(int row, int col) {

		return alive(row - 1, col) || alive(row + 1, col) || alive(row, col - 1) || alive(row, col + 1);
	}

	private boolean alive(int row, int col) {

		return inside(row, col) && cells[row][col].isBugAlive();

	}

	public void tryToHaveBaby(int row, int col) {
		int nr = -1;
		int nc = -1;
		if (col > 0 && cells[row][col - 1].isEmpty()) {
			nr = row;
			nc = col - 1;
		} else if (row > 0 && cells[row - 1][col].isEmpty()) {
			nr = row -1;
			nc = col;
		} else if (col < this.cols - 1 && cells[row][col + 1].isEmpty()) {
			nr = row;
			nc = col - 1;
		} else if (row < this.rows - 1 && cells[row + 1][col].isEmpty()) {
			nr = row + 1;
			nc = col;
		}
		
		if (nr != -1 && nc != -1) {
			ui.showTemporaryMessage("A bug was born in the position (" + nr + "," + nc + ")");
			cells[nr][nc].createBug();
		} else {
			ui.showTemporaryMessage("The bug in position (" + row + "," + col + ") is trying to have a baby, but thre is no room!");
		}		
	}

	public void bugDied(int row, int col, String reason) {
		ui.showTemporaryMessage("Bug at (" + row + "," + col + ") died: " + reason);
	}

	private boolean inside(int row, int col) {
		return row >= 0 && row < rows && col >= 0 && col < cols;
	}
}