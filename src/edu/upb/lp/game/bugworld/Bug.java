package edu.upb.lp.game.bugworld;

public class Bug {

	private int hunger;
	private int fun;
	private int age;
	private boolean dead;

	private Cell cell;

	public Bug(Cell cell) {
		this.cell = cell;
		this.hunger = 0;
		this.fun = 15;
		this.age = 0;
		this.dead = false;
	}

	public int getHunger() {
		return hunger;
	}

	public int getFun() {
		return fun;
	}

	public int getAge() {
		return age;
	}

	public boolean isDead() {
		return dead;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public void day() {
		if (!dead) {
			hunger++;
			age++;
			fun--;

			if (hunger > 10) {
				tryToEat();
			} else {
				tryToPlay();
			}

			if (age >= 30) {
				die("Old age");
			} else if (hunger >= 20) {
				die("Hunger");
			} else if (fun <= 0) {
				die("Boredom");
			}
		}

	}

	public void tryToEat() {
		if (cell.tryToEat()) {
			hunger = 0;
		}
	}

	public void tryToPlay() {
		if (cell.hasCloseFriend()) {

			if (fun < 19) {
				fun += 2;
			} else if (fun == 19) {
				fun = 20;
			}

			if (fun == 20 && Math.random() > 0.90) {
				cell.tryToHaveBaby();
			}
		}
	}

	public void die(String reason) {
		dead = true;
		cell.bugDied(reason);
	}

	public int price() {
		if (dead)
			return 0;

		if (age < 15)
			return age;

		return 30 - age;
	}
}