package edu.upb.lp.game.bugworld;

public class AutomaticDayPasser implements Runnable {
    private BugWorldGame game;

    
    public AutomaticDayPasser(BugWorldGame game) {
        this.game = game;
    }


    @Override
    public void run() {
       game.automaticDay();
    }

}
