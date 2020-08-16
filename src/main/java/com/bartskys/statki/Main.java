package com.bartskys.statki;


public class Main  {
//Initializing the GameController(TODO: should be a Singleton!!)
    private static final GameController gameController = new GameController();

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
//Running the main game loop
        gameController.mainLoop();
    }
}
