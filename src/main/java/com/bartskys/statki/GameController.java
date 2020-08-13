package com.bartskys.statki;

import com.bartskys.statki.input.Input;
import com.bartskys.statki.input.MouseInput;
import com.bartskys.statki.math.Vector3f;
import com.bartskys.statki.model.Player;
import com.bartskys.statki.model.Tile;
import org.lwjgl.nuklear.*;
import static org.lwjgl.nuklear.Nuklear.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;

import static org.lwjgl.glfw.GLFW.*;

class GameController {
<<<<<<< Updated upstream


      private static boolean running;
      private Player player1, player2;
      float position = 2.0f;
      boolean init = true;

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

                  update();
                  //Render
                  render();

                  if (glfwWindowShouldClose(ViewRenderer.getWindow()))

                        running = false;
            }

            ViewRenderer.terminate();
      }

      private void render() {

            if(!onTiles)ViewRenderer.setCallbacks();
            else ViewRenderer.getNuklear().setupCallbacks(ViewRenderer.getWindow());

            ViewRenderer.renderStart();

            ViewRenderer.getNuklear().update(ViewRenderer.getNuklear().getCtx(), "Dupka");

            ViewRenderer.flagSetup();

            for (Tile t : player1.getBoard()) {

                  if(isMouseOnTile(t)){
                        ViewRenderer.renderShot(t);
                  }
                  else ViewRenderer.renderEmptyTile(t);

=======
    private static final String PLAYER1 = "Player01";
    private static final String PLAYER2 = "Player02";
    private static RenderBox PLAYER1SETUP;
    private static RenderBox PLAYER2SETUP;
    int p1Ships = 1;
    int p2Ships = 1;
    private static boolean running;
    private final Player player1, player2;
    private boolean p1setup, p2setup;
    private double mouseX, mouseY;
    int mClick;
    int mAction;
    float boardPosX = -2.2f;
    float boardPosY = 2.0f;
    ShipEnum shipType = ShipEnum.SINGLE;
    boolean direction = true;
    //Frame counting
    long lastTime = System.currentTimeMillis();
    long timer = System.currentTimeMillis();
    double delta = 0.0;
    double ns = 1000000000.0 / 60.0;
    int updates = 0;
    int frames = 0;
    boolean clicked = false;
    boolean buttonD = false;
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

        //Main Loop
        while (running) {


            if(Input.isKeyDown(GLFW_KEY_D) && !buttonD) {
                direction = !direction;
                buttonD = true;

            }
            // Click register
            if (!clicked) {

                System.out.println("mClick: " + mClick + " | mAction: " + mAction);
                System.out.println("MOT: " + isMouseOnTile(player1.getBoard(), mouseX, mouseY));

                if((mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS)) {
                    System.out.println("Clicked");
                    if((isMouseOnTile(player1.getBoard(), mouseX, mouseY) || isMouseOnTile(player2.getBoard(), mouseX, mouseY)))
                        clicked = true;
                }
            }
            Tile tempT;
            if(isMouseOnTile(player1.getBoard(), mouseX, mouseY))
                tempT = tileFromMouse(player1.getBoard(), mouseX, mouseY);
            else if (isMouseOnTile(player2.getBoard(), mouseX,mouseY))
                tempT = tileFromMouse(player2.getBoard(), mouseX, mouseY);
            else tempT = new Tile();
            playerSetup(clicked, tempT);

            // Update game events and Counts frames and updates
            frameUpdate();

            // Render after update
            render();

            if (glfwWindowShouldClose(ViewRenderer.getWindow()))
                running = false;
        }
        ViewRenderer.terminate();
    }

    private void render() {
        ViewRenderer.renderStart();
        if (p1setup)
            ViewRenderer.renderBox(PLAYER1SETUP);
        if (p2setup)
            ViewRenderer.renderBox(PLAYER2SETUP);
        renderBoard(player1);
        renderBoard(player2);
        if (isMouseOnTile(player1.getBoard(), mouseX, mouseY)) {
            ArrayList<Tile> tiles;
            tiles = tilesFromTile(player1.getBoard(), tileFromMouse(player1.getBoard(), mouseX, mouseY), 1, direction);
            if(shipType == ShipEnum.DOUBLE)
                tiles = tilesFromTile(player1.getBoard(), tileFromMouse(player1.getBoard(), mouseX, mouseY), 2, direction);
            if(shipType == ShipEnum.TRIPLE)
                tiles = tilesFromTile(player1.getBoard(), tileFromMouse(player1.getBoard(), mouseX, mouseY), 3, direction);
            if(shipType == ShipEnum.QUAD)
                tiles = tilesFromTile(player1.getBoard(), tileFromMouse(player1.getBoard(), mouseX, mouseY), 4, direction);
            for (Tile t: tiles
                 ) {
                ViewRenderer.renderShip(t);
            }

        }
        if (isMouseOnTile(player2.getBoard(), mouseX, mouseY))
            ViewRenderer.renderShip(tileFromMouse(player2.getBoard(), mouseX, mouseY));
        ViewRenderer.renderFinish();
    }

    private void renderBoard(Player player) {
        for (Tile t : player.getBoard()) {
            if (t.isOwned())
                ViewRenderer.renderShip(t);
            else
                ViewRenderer.renderEmptyTile(t);
        }
    }

    private void update() {


        // Aligning mouse position to the board so that the mouse coords correspond to the board coords
        mouseX = ((float) MouseInputPos.xPos / 1280f - 1.0f) * 10.0f;
        mouseY = ((float) MouseInputPos.yPos / 720f - 1.0f) * (-10.0f * 9.0f / 16.0f);
        mClick = MouseInputClick.mbutton;
        mAction = MouseInputClick.maction;
        // Click reset
        if (frames >= 30 && clicked)
            clicked = false;
        if (frames >= 30 && buttonD)
            buttonD = false;
        glfwPollEvents();
    }

    void playerSetup(boolean clicked, Tile t) {
        if (p1setup || p2setup) {
            if (p1setup) {
                switch (p1Ships) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4: {
                        shipType = ShipEnum.SINGLE;
                        if (clicked) {
                            if (!t.isOwned())
                                if (checkAdjacent(t, player1.getBoard()))
                                    if(assembleShip(t, player1, 1, p1Ships, direction))
                                        p1Ships++;
                        }
                    }
                    break;
                    case 5:
                    case 6:
                    case 7: {
                        shipType = ShipEnum.DOUBLE;
                        if (clicked) {
                            if (!t.isOwned())
                                if (checkAdjacent(t, player1.getBoard()))
                                    if(assembleShip(t, player1, 2, p1Ships, direction))
                                        p1Ships++;
                        }
                    }
                    break;
                    case 8:
                    case 9: {
                        shipType = ShipEnum.TRIPLE;
                        if (clicked) {
                            if (!t.isOwned())
                                if (checkAdjacent(t, player1.getBoard()))
                                    if(assembleShip(t, player1, 3, p1Ships, direction))
                                        p1Ships++;
                        }
                    }
                    break;
                    case 10: {
                        shipType = ShipEnum.QUAD;
                        if (clicked) {
                            if (!t.isOwned())
                                if (checkAdjacent(t, player1.getBoard()))
                                    if(assembleShip(t, player1, 4, p1Ships, direction)){
                                        p1setup = false;
                                    }
                        }

                    }
                    break;
                }
            } else if (p2setup) {
                switch (p2Ships) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4: {
                        shipType = ShipEnum.SINGLE;
                        if (clicked) {
                            if (!t.isOwned())
                                if (checkAdjacent(t, player2.getBoard()))
                                    assembleShip(t, player2, 1, p2Ships, direction);
                            p2Ships++;
                        }
                    }
                    break;
                    case 5:
                    case 6:
                    case 7: {
                        shipType = ShipEnum.DOUBLE;
                        if (clicked) {
                            if (!t.isOwned())
                                if (checkAdjacent(t, player2.getBoard()))
                                    if(assembleShip(t, player2, 2, p2Ships, direction))
                                        p2Ships++;
                        }
                    }
                    break;
                    case 8:
                    case 9: {
                        shipType = ShipEnum.TRIPLE;
                        if (clicked) {
                            if (!t.isOwned())
                                if (checkAdjacent(t, player2.getBoard()))
                                    if(assembleShip(t, player2, 3, p2Ships, direction))
                                        p2Ships++;
                        }
                    }
                    break;
                    case 10: {
                        shipType = ShipEnum.QUAD;
                        if (clicked) {
                            if (!t.isOwned())
                                if (checkAdjacent(t, player2.getBoard()))
                                    if(assembleShip(t, player2, 4, p2Ships, direction))
                                        p2setup = false;
                        }
                    }
                    break;
                }
>>>>>>> Stashed changes
            }

            ViewRenderer.renderFinish();
      }

      private void update() {
            if(Input.isKeyDown(GLFW_KEY_E))
                  onTiles = !onTiles;
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
<<<<<<< Updated upstream

            frames++;

            if (System.currentTimeMillis() - timer > 1000) {

                  timer += 1000;
                  System.out.println(updates + " ups, " + frames + " fps");
                  updates = 0;
                  frames = 0;
            }
      }

      private boolean isMouseOnTile(Tile t) {
            float mouseX = ((float) MouseInput.xPos / 1280f - 1.0f) * 10.0f;
            float mouseY = ((float) MouseInput.yPos / 720f - 1.0f) * (-10.0f * 9.0f / 16.0f);
            float diameter = 0.5f;
            if(
=======
        }
        return true;
    }

    boolean assembleShip(Tile t, Player player, int number, int shipnum, boolean dir) {
        //if(!checkAdjacent(t, player.getBoard())) return false;
        ArrayList<Tile> tiles = tilesFromTile(player.getBoard(), t, number, dir);
        if(tiles.size() == 0) return false;
        addShip(player, shipType, shipnum, tiles, dir);

        System.out.println("Ship " + shipType + " added to " + player.getName() + " | " + shipnum);

        for (Tile ptiles : player.getBoard()) {
            for (Tile tile : tiles) {
                if (tile.equals(ptiles)) {
                    ptiles.setOwned(true);
                    ptiles.setOwnedByShip(shipType + String.valueOf(shipnum));
                }
            }
        }
        return true;
    }

    void addShip(Player player, ShipEnum shipType, int shipNumber, ArrayList<Tile> t, boolean dir) {
        player.getShips().add(new Ship(t, dir, shipType + String.valueOf(shipNumber)));
    }

    private boolean isMouseOnTile(ArrayList<Tile> tiles, double mouseX, double mouseY) {
        for (Tile t : tiles) {
            float diameter = 0.25f; //Board tile dimeter
            if (
>>>>>>> Stashed changes
                    mouseX > t.getCoords().x - diameter &&
                            mouseX < (t.getCoords().x + diameter) &&
                            mouseY > (t.getCoords().y - diameter) &&
                            mouseY < (t.getCoords().y + diameter)
<<<<<<< Updated upstream
            ){
                  System.out.println("Tile name: " + t.getName() + " x: " + t.getCoords().x + " y: " + t.getCoords().y + "\nMouse x: " + mouseX + " y: " + mouseY);
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
=======
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

    private Tile tileFromMouse(ArrayList<Tile> tiles, double mouseX, double mouseY) {
        for (Tile t : tiles) {
            float diameter = 0.25f; //Board tile dimeter
            if (
                    mouseX > t.getCoords().x - diameter &&
                            mouseX < (t.getCoords().x + diameter) &&
                            mouseY > (t.getCoords().y - diameter) &&
                            mouseY < (t.getCoords().y + diameter)
            ) {
                System.out.println("DD");
                return t;
            }
        }
        return new Tile("Null", new Vector3f(0.0f, 0.0f, 0.0f), "null");
    }

    private ArrayList<Tile> tilesFromTile(ArrayList<Tile> tiles, Tile tile, int number, boolean dir) {
        ArrayList<Tile> ts = new ArrayList<>();
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).equals(tile))
                for (int j = 0; j < number; j++)
                    if(i + (j * 10) < tiles.size() && i + j < tiles.size())
                        if(dir)
                            ts.add(tiles.get(i + (j * 10)));
                        else
                            ts.add(tiles.get(i + j));
                    else return new ArrayList<>();
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
>>>>>>> Stashed changes
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

      private void playerSetup(Player player) {

      }
}
