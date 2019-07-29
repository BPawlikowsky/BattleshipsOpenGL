package com.bartskys.statki;

import com.bartskys.statki.input.MouseInput;
import com.bartskys.statki.math.Vector3f;
import com.bartskys.statki.model.Player;
import com.bartskys.statki.model.Tile;

import java.util.ArrayList;
import java.util.Comparator;

import static org.lwjgl.glfw.GLFW.*;

class GameController {


      private static boolean running;
      private Player player1, player2;
      float position = 2.0f;

      final ArrayList<Tile> tiles;

      GameController() {

            ViewRenderer.init();
            running = true;
            player1 = new Player(generateTiles(), "player01");
            player2 = new Player(generateTiles(), "player02");
            tiles = new ArrayList<>(player1.getBoard());
      }

      void mainLoop() {

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
                  if (delta >= 1.0) {
                        update();
                        updates++;
                        delta--;
                  }

                  render();

                  frames++;
                  if (System.currentTimeMillis() - timer > 1000) {
                        player1.setBoard(tiles);
                        timer += 1000;
                        System.out.println(updates + " ups, " + frames + " fps");
                        updates = 0;
                        frames = 0;

                  }

                  if (glfwWindowShouldClose(ViewRenderer.getWindow()))

                        running = false;
            }

            ViewRenderer.terminate();
      }

      private void render() {

            ViewRenderer.renderStart();


            for (Tile t : player1.getBoard()) {

                  if (
                          MouseInput.xPos + 10f > Math.abs((t.getCoords().x) * 128f * 0.5f) &&
                                  720f - MouseInput.yPos + 10f > Math.abs((t.getCoords().y) * 72f * 0.95f) &&
                                  MouseInput.xPos - 10f < Math.abs((t.getCoords().x) * 128f * 0.5f) + 32f &&
                                  720f - MouseInput.yPos - 10f < Math.abs((t.getCoords().y) * 72f * 0.95f) + 18f
                  ) {
                        ViewRenderer.renderShot(t);
                        System.out.println(
                                "Tile name: " + t.getName() +
                                        " x: " + t.getCoords().x + " y: " + t.getCoords().y +
                                        "\nMouse x: " + MouseInput.xPos + " y: " + MouseInput.yPos);
                  } else
                        ViewRenderer.renderEmptyTile(t);
            }

            ViewRenderer.renderFinish();
      }

      private ArrayList<Tile> generateTiles() {

            ArrayList<Tile> tiles = new ArrayList<>();

            float scale = 0.5f;
            position = 0.0f;

            for (int y = 9; y >= 0; y--) {
                  for (int x = 0; x < 10; x++) {
                        tiles.add(new Tile(
                                String.valueOf(x) + (9 - y),
                                new Vector3f(
                                        (x * scale) + position,
                                        (y * scale) + position,
                                        0.0f))
                        );
                  }
            }
            tiles = sortTilesByY(tiles);

            for (Tile t : tiles) {
                  System.out.printf(
                          "Tile name: %s | x: %f, y: %f\n", t.getName(),
                          Math.abs(t.getCoords().x * 128.0f / 2.0f),
                          Math.abs(t.getCoords().y * 72.0f / 2f));
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

      private void update() {


            glfwPollEvents();
      }
}
