package com.bartskys.statki;


public class Main  {

    private boolean running = false;

    private static final GameController gameController = new GameController();



    public static void main(String[] args) {

        gameController.mainLoop();
    }
}
