package edu.upb.lp.game.bugworld;

public class Cell {

    private BugWorld world;

    private Bug bug;
    private int food;

    private int row;
    private int col;

    public Cell(int row, int col, BugWorld world) {
        this.row = row;
        this.col = col;
        this.world = world;
    }

    public boolean isEmpty() {
        return bug == null && food == 0;
    }

    public boolean hasBug() {
        return bug != null;
    }

    public boolean isBugAlive() {
        return bug != null && !bug.isDead();
    }

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;

        if (bug != null) {
            bug.setCell(this);
        }
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public boolean eat() {
        if (food > 0) {
            food--;
            return true;
        }
        return false;
    }

    public void createBug() {
        this.bug = new Bug(this);
    }

    public void day() {
        if (bug != null) {
            bug.day();
        }
    }

    public boolean tryToEat() {
        return world.tryToEat(row, col);
    }

    public boolean hasCloseFriend() {
        return world.hasCloseFriend(row, col);
    }

    public void tryToHaveBaby() {
        world.tryToHaveBaby(row, col);
    }

    public void bugDied(String reason) {
        world.bugDied(row, col, reason);
    }
}