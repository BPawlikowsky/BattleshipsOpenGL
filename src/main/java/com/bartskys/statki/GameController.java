package com.bartskys.statki;

import com.bartskys.statki.input.MouseInputClick;
import com.bartskys.statki.input.MouseInputPos;
import com.bartskys.statki.math.Vector3f;
import com.bartskys.statki.model.*;


import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

class GameController {
      private ArrayList<Tile> temp = new ArrayList<>();
      private boolean building = false; int c = 0;
      private static final String PLAYER1 = "Player01";
      private static final String PLAYER2 = "Player02";
      private static RenderBox PLAYER1SETUP;
      private static RenderBox PLAYER2SETUP;
      int p1Ships = 1;
      int p2Ships = 1;
      private static boolean running;
      private final Player player1, player2;
      private boolean p1setup, p2setup;
      private float mouseX, mouseY;
      float boardPosX = -2.2f;
      float boardPosY = 2.0f;
      ShipEnum shipType = ShipEnum.SINGLE;
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
            player2 = new Player(generateTiles(boardPosX, boardPosY + 1.3f, PLAYER2), PLAYER2);
            p1setup = p2setup = true;
            PLAYER1SETUP = new RenderBox(
                    "PlayerSetup",
                    new Vector3f(-5.8f, 4.0f, 0.0f)
            );
            PLAYER2SETUP = new RenderBox(
                    "PlayerSetup",
                    new Vector3f(5.5f, 4.0f, 0.0f)
            );
      }

      void mainLoop() {
            boolean clicked = false;
            //Main Loop
            while (running) {
                  // Click register
                  if(!clicked) clicked = (MouseInputClick.mbutton == GLFW_MOUSE_BUTTON_1
                                                && MouseInputClick.maction == GLFW_PRESS);
                  // Click reset
                  if (frames % 60 == 0 && clicked)
                        clicked = false;
                  // Update game events and Counts frames and updates
                  frameUpdate();

                  playerSetup(clicked);

                  // Render after update
                  render();

                  if (glfwWindowShouldClose(ViewRenderer.getWindow()))
                        running = false;
            }
            ViewRenderer.terminate();
      }

      void playerSetup(boolean clicked) {
            if(p1setup || p2setup) {
                  if(p1setup) {
                        switch (p1Ships) {
                              case 0:
                              case 1:
                              case 2:
                              case 3:
                              case 4: {
                                    shipType = ShipEnum.SINGLE;
                                    if(clicked && frames%60 == 0) {
                                          assembleShip(player1, 1, p1Ships);
                                          p1Ships++;
                                    }
                              } break;
                              case 5:
                              case 6:
                              case 7: {
                                    shipType = ShipEnum.DOUBLE;
                                    if(clicked && frames%60 == 0) {
                                          assembleShip(player1, 2, p1Ships);
                                          p1Ships++;
                                    }
                              } break;
                              case 8:
                              case 9: {
                                    shipType = ShipEnum.TRIPLE;
                                    if(clicked && frames%60 == 0) {
                                          assembleShip(player1, 3, p1Ships);
                                          p1Ships++;
                                    }
                              } break;
                              case 10: {
                                    shipType = ShipEnum.QUAD;
                                    if(clicked && frames%60 == 0) {
                                          assembleShip(player1, 4, p1Ships);
                                          p1setup = false;
                                    }

                              } break;
                        }
                  }
                  else if(p2setup) {
                        switch (p2Ships) {
                              case 0:
                              case 1:
                              case 2:
                              case 3:
                              case 4: {
                                    shipType = ShipEnum.SINGLE;
                                    if(clicked && frames%60 == 0) {
                                          assembleShip(player2, 1, p2Ships);
                                          p2Ships++;
                                    }
                              } break;
                              case 5:
                              case 6:
                              case 7: {
                                    shipType = ShipEnum.DOUBLE;
                                    if(clicked && frames%60 == 0) {
                                          assembleShip(player2, 2, p2Ships);
                                          p2Ships++;
                                    }
                              } break;
                              case 8:
                              case 9: {
                                    shipType = ShipEnum.TRIPLE;
                                    if(clicked && frames%60 == 0) {
                                          assembleShip(player2, 3, p2Ships);
                                          p2Ships++;
                                    }
                              } break;
                              case 10: {
                                    shipType = ShipEnum.QUAD;
                                    if(clicked && frames%60 == 0) {
                                          assembleShip(player2, 4, p2Ships);
                                          p2setup = false;
                                    }

                              } break;
                        }
                  }
            }
      }

      void assembleShip(Player player, int number, int shipnum) {
                  Tile t = tileFromMouse(player.getBoard());
                  ArrayList<Tile> tiles = tilesFromTile(player.getBoard(), t, number);
                  addShip(player, shipType, shipnum, tiles, true);

                  System.out.println("Ship " + shipType + " added to " + player.getName() + " | " + p1Ships);

                  for (Tile ptiles : player.getBoard()) {
                        for (Tile tile: tiles) {
                              if(tile.equals(ptiles)) {
                                    ptiles.setOwned(true);
                                    ptiles.setOwnedByShip(shipType+String.valueOf(shipnum));
                              }
                        }
                  }
      }

      void addShip(Player player, ShipEnum shipType, int shipNumber, ArrayList<Tile> t, boolean dir) {
            player.getShips().add(new Ship(t, dir, shipType+String.valueOf(shipNumber)));
      }

      private void render() {
            ViewRenderer.renderStart();
            if(p1setup)
                  ViewRenderer.renderBox(PLAYER1SETUP);
            if(p2setup)
                  ViewRenderer.renderBox(PLAYER2SETUP);
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
                  if(t.isOwned())
                        ViewRenderer.renderShip(t);
                  else
                        ViewRenderer.renderEmptyTile(t);
            }
      }

      private void update() {


            // Aligning mouse position to the board so that the mouse coords correspond to the board coords
            mouseX = ((float) MouseInputPos.xPos / 1280f - 1.0f) * 10.0f;
            mouseY = ((float) MouseInputPos.yPos / 720f - 1.0f) * (-10.0f * 9.0f / 16.0f);
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
//                        System.out.printf(
//                                "Tile name: %s x: %.1f y: %.1f\nMouse x: %.2f y: %.2f | %s\n",
//                                t.getName(),
//                                t.getCoords().x,
//                                t.getCoords().y,
//                                mouseX, mouseY, t.getPlayer()
//                        );
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
                        return t;
                  }
            }
            return new Tile("Null", new Vector3f(0.0f, 0.0f, 0.0f),"null");
      }

      private ArrayList<Tile> tilesFromTile(ArrayList<Tile> tiles, Tile tile, int number) {
            ArrayList<Tile> ts = new ArrayList<>();
            for(int i = 0; i < tiles.size(); i++) {
                  if(tiles.get(i).equals(tile))
                        for (int j = 0; j < number; j++)
                              ts.add(tiles.get(i+(j*10)));
            }
            return ts;
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

      private void frameUpdate() {
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
