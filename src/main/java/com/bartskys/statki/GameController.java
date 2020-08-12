package com.bartskys.statki;

import com.bartskys.statki.input.MouseInput;
import com.bartskys.statki.math.Vector3f;
import com.bartskys.statki.model.Player;
import com.bartskys.statki.model.ShipEnum;
import com.bartskys.statki.model.Tile;


import java.util.ArrayList;
import java.util.Comparator;

import static org.lwjgl.glfw.GLFW.*;

class GameController {
      private static final String PLAYER1 = "Player01";
      private static final String PLAYER2 = "Player02";
      private static boolean running;
      private final Player player1, player2;
      private float mouseX, mouseY;
      float boardPosX = -2.2f;
      float boardPosY = 2.0f;

      //Frame counting
      long lastTime = System.currentTimeMillis();
      long timer = System.currentTimeMillis();
      double delta = 0.0;
      double ns = 1000000000.0 / 60.0;
      int updates = 0;
      int frames = 0;
      //-------------

      GameController() {
            ViewRenderer.init();
            running = true;
            player1 = new Player(generateTiles(boardPosX, boardPosY - 10.0f, PLAYER1), PLAYER1);
            player2 = new Player(generateTiles(boardPosX, boardPosY, PLAYER2), PLAYER2);

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

      void addShip(Player player, ShipEnum shipType, int shipNumber, Tile t) {

      }

      private void render() {
            ViewRenderer.renderStart();

            renderBoard(player1);
            renderBoard(player2);
            if(isMouseOnTile(player1.getBoard()))
                  ViewRenderer.renderShip(tileFromMouse(player1.getBoard()));
            if(isMouseOnTile(player2.getBoard()))
                  ViewRenderer.renderShip(tileFromMouse(player2.getBoard()));
            ViewRenderer.renderFinish();
      }

      private void renderBoard(Player player) {
            for (Tile t : player.getBoard()) {
                  ViewRenderer.renderEmptyTile(t);
            }
      }

      private void update() {
            // Aligning mouse position to the board so that the mouse coords corespond to the board coords
            mouseX = ((float) MouseInput.xPos / 1280f - 1.0f) * 10.0f;
            mouseY = ((float) MouseInput.yPos / 720f - 1.0f) * (-10.0f * 9.0f / 16.0f);
            glfwPollEvents();
      }

      private boolean isMouseOnTile(ArrayList<Tile> tiles) {
            for(Tile t : tiles) {
                  float diameter = 0.25f; //Board tile dimeter
                  if (
                          mouseX > t.getCoords().x - diameter &&
                                  mouseX < (t.getCoords().x + diameter) &&
                                  mouseY > (t.getCoords().y - diameter) &&
                                  mouseY < (t.getCoords().y + diameter)
                  ) {
                        System.out.printf(
                                "Tile name: %s x: %.1f y: %.1f\nMouse x: %.2f y: %.2f | %s\n",
                                t.getName(),
                                t.getCoords().x,
                                t.getCoords().y,
                                mouseX, mouseY, t.getPlayer()
                        );
                        return true;
                  }
            }
            return false;
      }

      private Tile tileFromMouse(ArrayList<Tile> tiles) {
            for(Tile t : tiles) {
                  float diameter = 0.25f; //Board tile dimeter
                  if (
                          mouseX > t.getCoords().x - diameter &&
                                  mouseX < (t.getCoords().x + diameter) &&
                                  mouseY > (t.getCoords().y - diameter) &&
                                  mouseY < (t.getCoords().y + diameter)
                  ) {
                        System.out.printf(
                                "Tile name: %s x: %.1f y: %.1f\nMouse x: %.2f y: %.2f | %s\n",
                                t.getName(),
                                t.getCoords().x,
                                t.getCoords().y,
                                mouseX, mouseY, t.getPlayer()
                        );
                        return t;
                  }
            }
            return new Tile("Null", new Vector3f(0.0f, 0.0f, 0.0f),"null");
      }

      private ArrayList<Tile> generateTiles(float posX, float posY, String player) {
            ArrayList<Tile> tiles = new ArrayList<>();

            float scale = 0.5f;

            for (int y = 9; y >= 0; y--) {
                  for (int x = 0; x < 10; x++) {
                        tiles.add(new Tile(
                                String.valueOf(x) + (9 - y),
                                new Vector3f(
                                        (x * scale) + posY,
                                        (y * scale) + posX,
                                        0.1f), player)
                        );
                  }
            }

//            for (Tile t : tiles) {
//                  System.out.printf(
//                          "Tile name: %s | x: %f, y: %f\n", t.getName(),
//                          t.getCoords().x,
//                          t.getCoords().y);
//            }

            return tiles;
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
}
