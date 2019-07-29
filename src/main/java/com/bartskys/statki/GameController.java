package com.bartskys.statki;

import com.bartskys.statki.math.Vector3f;
import com.bartskys.statki.model.Player;
import com.bartskys.statki.model.Tile;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class GameController {


      private static boolean running;
      private Player player1, player2;

      public GameController() {

            ViewRenderer.init();
            running = true;
            player1 = new Player(generateTiles(), "player01");
      }

      public void mainLoop() {

            long lastTime = System.currentTimeMillis();
            long timer = System.currentTimeMillis();
            double delta = 0.0;
            double ns = 1000000000.0 / 60.0;
            int updates = 0;
            int frames = 0;

            while (running) {

                  long now = System.nanoTime();
                  delta += (now - lastTime) / ns;
                  lastTime = now;
                  if ( delta >= 1.0 ) {
                        update();
                        updates++;
                        delta--;
                  }

                  ViewRenderer.renderStart();

                  for( Tile t: player1.getBoard() ) {
                        if ( t != null )
                              ViewRenderer.renderTile(t);
                  }


                  ViewRenderer.renderFinish();

                  frames++;
                  if ( System.currentTimeMillis() - timer > 1000 ) {
                        timer += 1000;
                        System.out.println(updates + " ups, " + frames + " fps" );
                        updates = 0;
                        frames = 0;

                  }

                  if(glfwWindowShouldClose(ViewRenderer.getWindow()))

                        running = false;
            }

            ViewRenderer.terminate();
      }

      private ArrayList<Tile> generateTiles() {

            ArrayList<Tile> tiles = new ArrayList<>();
            float position = - 2.0f;
            float scale = 0.5f;

            for (int y = 0; y < 10; y++) {
                  for (int x = 0; x < 10; x++) {
                              tiles.add( new Tile(
                                      String.valueOf(x) + String.valueOf(y),
                                      new Vector3f(
                                      (x * scale) + position,
                                      (y * scale) + position,
                                      0.0f))
                              );
                  }
            }
            return tiles;
      }

      static void update() {

            glfwPollEvents();
      }
}
