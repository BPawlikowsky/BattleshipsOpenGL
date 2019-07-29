package com.bartskys.statki;


public class Main implements Runnable {

    private boolean running = false;

    private GameController gameController;

    private void start() {

        running = true;
        Thread thread = new Thread(this, "Game");
        thread.start();
    }

    @Override
    public void run() {
        gameController = new GameController();
        gameController.mainLoop();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
