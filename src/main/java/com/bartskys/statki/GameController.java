package com.bartskys.statki;

import com.bartskys.statki.input.Input;
import com.bartskys.statki.input.MouseInput;
import com.bartskys.statki.math.Vector3f;
import com.bartskys.statki.model.Player;
import com.bartskys.statki.model.Ship;
import com.bartskys.statki.model.ShipEnum;
import com.bartskys.statki.model.Tile;
import org.lwjgl.nuklear.*;
import static org.lwjgl.nuklear.Nuklear.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

class GameController {
      private static boolean running;
      private Player player1, player2;
      private float mouseX, mouseY;
      float position = 2.0f;

      long lastTime = System.currentTimeMillis();
      long timer = System.currentTimeMillis();
      double delta = 0.0;
      double ns = 1000000000.0 / 60.0;
      int updates = 0;
      int frames = 0;
      private boolean onTiles = false;


      GameController() {
            ViewRenderer.init();
            running = true;
            player1 = new Player(generateTiles(), "player01");
            player2 = new Player(generateTiles(), "player02");

      }

      void mainLoop() {
            //Main Loop
            while (running) {
                  // Counts frames and updates
                  frameCounter();
                  // Update game events
                  update();
                  // Render after update
                  render();

                  if (glfwWindowShouldClose(ViewRenderer.getWindow()))

                        running = false;
            }

            ViewRenderer.terminate();
      }

      private void render() {
            ViewRenderer.setCallbacks();

            ViewRenderer.renderStart();

            ViewRenderer.flagSetup();

            for (Tile t : player1.getBoard()) {

                  if(isMouseOnTile(t)){
                        ViewRenderer.renderShip(t);
                  }
                  else ViewRenderer.renderEmptyTile(t);
            }

            ViewRenderer.renderFinish();
      }

      private void update() {
            // Aligning mouse position to the board so that the mouse coords corespond to the board coords
            mouseX = ((float) MouseInput.xPos / 1280f - 1.0f) * 10.0f;
            mouseY = ((float) MouseInput.yPos / 720f - 1.0f) * (-10.0f * 9.0f / 16.0f);
            glfwPollEvents();
      }

      private void frameCounter() {
            //Timed updates
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0) {
                  update();
                  updates++;
                  delta--;
            }

            frames++;

            if (System.currentTimeMillis() - timer > 1000) {

                  timer += 1000;
                  System.out.println(updates + " ups, " + frames + " fps");
                  updates = 0;
                  frames = 0;
            }
      }

      private boolean isMouseOnTile(Tile t) {

            float diameter = 0.5f; //Board tile dimeter
            if(
                    mouseX > t.getCoords().x - diameter &&
                            mouseX < (t.getCoords().x + diameter) &&
                            mouseY > (t.getCoords().y - diameter) &&
                            mouseY < (t.getCoords().y + diameter)
            ){
                  System.out.printf(
                          "Tile name: %s x: %.1f y: %.1f\nMouse x: %.2f y: %.2f\n",
                          t.getName(),
                          t.getCoords().x,
                          t.getCoords().y,
                          mouseX, mouseY
                  );
                  return true;
            }
            else return false;
      }

      private ArrayList<Tile> generateTiles() {
            ArrayList<Tile> tiles = new ArrayList<>();

            float scale = 1f;
            position = -5.0f;

            for (int y = 9; y >= 0; y--) {
                  for (int x = 0; x < 10; x++) {
                        tiles.add(new Tile(
                                String.valueOf(x) + (9 - y),
                                new Vector3f(
                                        (x * scale) + position,
                                        (y * scale) + position,
                                        0.1f))
                        );
                  }
            }
            tiles = sortTilesByY(tiles);

            for (Tile t : tiles) {
                  System.out.printf(
                          "Tile name: %s | x: %f, y: %f\n", t.getName(),
                          t.getCoords().x,
                          t.getCoords().y);
            }

            return tiles;
      }

      private ArrayList<Tile> sortTilesByY(ArrayList<Tile> tiles) {
            Comparator<? super Tile> comparator = new Comparator<Tile>() {
                  @Override
                  public int compare(Tile o1, Tile o2) {
                        if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                              return 1;
                        } else if (Integer.parseInt(o1.getName()) == Integer.parseInt(o2.getName()))
                              return 0;
                        else return -1;
                  }
            };
            tiles.sort(comparator);
            return tiles;
      }
}
