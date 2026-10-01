package edu.upb.lp.game.bugworld;

public class AutomaticDayPasser implements Runnable {
    private BugWorldController controller;


    public AutomaticDayPasser(BugWorldController controller) {
        this.controller = controller;
    }


    @Override
    public void run() {
       controller.automaticDay();
    }

}
