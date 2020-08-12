package com.bartskys.statki;

import com.bartskys.statki.input.MouseInputClick;
import com.bartskys.statki.input.MouseInputPos;
import com.bartskys.statki.math.Vector3f;
import com.bartskys.statki.model.*;


import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

class GameController {
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
    boolean direction = true;
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
            if (!clicked) clicked = (MouseInputClick.mbutton == GLFW_MOUSE_BUTTON_1
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
        if (p1setup || p2setup) {
            if (p1setup) {
                switch (p1Ships) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4: {
                        shipType = ShipEnum.SINGLE;
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player1.getBoard());
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
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player1.getBoard());
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
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player1.getBoard());
                            if (!t.isOwned())
                                if (checkAdjacent(t, player1.getBoard()))
                                    if(assembleShip(t, player1, 3, p1Ships, direction))
                                        p1Ships++;
                        }
                    }
                    break;
                    case 10: {
                        shipType = ShipEnum.QUAD;
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player1.getBoard());
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
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player2.getBoard());
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
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player2.getBoard());
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
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player2.getBoard());
                            if (!t.isOwned())
                                if (checkAdjacent(t, player2.getBoard()))
                                    if(assembleShip(t, player2, 3, p2Ships, direction))
                                        p2Ships++;
                        }
                    }
                    break;
                    case 10: {
                        shipType = ShipEnum.QUAD;
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player2.getBoard());
                            if (!t.isOwned())
                                if (checkAdjacent(t, player2.getBoard()))
                                    if(assembleShip(t, player2, 4, p2Ships, direction))
                                        p2setup = false;
                        }
                    }
                    break;
                }
            }
        }
    }

    boolean checkAdjacent(Tile tile, ArrayList<Tile> board) {

        float xLo = board.get(0).getCoords().x;
        float yLo = board.get(0).getCoords().y;
        float xHi = board.get(9).getCoords().x;
        float yHi = board.get(99).getCoords().y;
        System.out.printf("Check Adj | xLo: %.2f | xHi: %.2f | yLo: %.2f | yHi: %.2f\n", xLo, xHi, yLo, yHi);
        for (int j = 0; j < board.size(); j++) {
            if (tile.equals(board.get(j))) {
                System.out.println(board.get(j).getCoords().toString());
                if (board.get(j).getCoords().x > xLo &&
                        board.get(j).getCoords().x < xHi &&
                        board.get(j).getCoords().y < yLo &&
                        board.get(j).getCoords().y > yHi
                ) {
                    System.out.println("Equals");
                    if (board.get(j + 1).isOwned() ||
                            board.get(j - 1).isOwned() ||
                            board.get(j + 9).isOwned() ||
                            board.get(j - 9).isOwned() ||
                            board.get(j + 10).isOwned() ||
                            board.get(j - 10).isOwned() ||
                            board.get(j + 11).isOwned() ||
                            board.get(j - 11).isOwned()
                    ) return false;
                } else if (board.get(j).getCoords().x == xLo &&
                            board.get(j).getCoords().y < yLo &&
                        board.get(j).getCoords().y > yHi) {
                    if (board.get(j + 1).isOwned() ||
                            board.get(j - 9).isOwned() ||
                            board.get(j + 10).isOwned() ||
                            board.get(j - 10).isOwned() ||
                            board.get(j + 11).isOwned()
                    ) return false;
                } else if (board.get(j).getCoords().x == xHi &&
                        board.get(j).getCoords().y < yLo &&
                        board.get(j).getCoords().y > yHi) {
                    if (board.get(j - 1).isOwned() ||
                            board.get(j + 9).isOwned() ||
                            board.get(j + 10).isOwned() ||
                            board.get(j - 10).isOwned() ||
                            board.get(j - 11).isOwned()
                    ) return false;
                } else if (board.get(j).getCoords().y == yLo &&
                        board.get(j).getCoords().x > xLo &&
                        board.get(j).getCoords().x < xHi) {
                    if (board.get(j + 1).isOwned() ||
                            board.get(j - 1).isOwned() ||
                            board.get(j + 9).isOwned() ||
                            board.get(j + 10).isOwned() ||
                            board.get(j + 11).isOwned()
                    ) return false;
                } else if (board.get(j).getCoords().y == yHi &&
                        board.get(j).getCoords().x > xLo &&
                        board.get(j).getCoords().x < xHi) {
                    if (board.get(j + 1).isOwned() ||
                            board.get(j - 1).isOwned() ||
                            board.get(j - 9).isOwned() ||
                            board.get(j - 10).isOwned() ||
                            board.get(j - 11).isOwned()
                    ) return false;
                }
            }
        }
        return true;
    }

    int checkCorner(Tile tile, ArrayList<Tile> board) {

        float xLo = board.get(0).getCoords().x;
        float yLo = board.get(0).getCoords().y;
        float xHi = board.get(9).getCoords().x;
        float yHi = board.get(99).getCoords().y;
        System.out.printf("Check Corner | xLo: %.2f | xHi: %.2f | yLo: %.2f | yHi: %.2f\n", xLo, xHi, yLo, yHi);
        for (int j = 0; j < board.size(); j++) {
            if (tile.equals(board.get(j))) {
                if (board.get(j).getCoords().x == xHi) {
                    return 0;
                } else if (board.get(j).getCoords().y == yHi) {
                     return 1;
                }
            }
        }
        return -1;
    }

    boolean assembleShip(Tile t, Player player, int number, int shipnum, boolean dir) {
        if(!checkAdjacent(t, player.getBoard())) return false;
        if(number > 1 && !t.getName().equals("Null") && Integer.parseInt(t.getName()) >= 90 &&  !dir) return false;
        if(number > 1 && t.getName().charAt(1) == '9'  &&  dir) return false;
        ArrayList<Tile> tiles = tilesFromTile(player.getBoard(), t, number);
        if(tiles.size() == 0) return false;
        for (int i = 0; i < tiles.size(); i++) {
            if(i+1 < tiles.size())
                if(number > 1 && checkCorner(tiles.get(i+1), player.getBoard()) >= 0) return false;
            else if(i+1 > tiles.size())return false;
            if(!checkAdjacent(tiles.get(i), player.getBoard())) return false;
        }
        addShip(player, shipType, shipnum, tiles, true);

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

    private void render() {
        ViewRenderer.renderStart();
        if (p1setup)
            ViewRenderer.renderBox(PLAYER1SETUP);
        if (p2setup)
            ViewRenderer.renderBox(PLAYER2SETUP);
        renderBoard(player1);
        renderBoard(player2);
        if (isMouseOnTile(player1.getBoard()))
            ViewRenderer.renderShip(tileFromMouse(player1.getBoard()));
        if (isMouseOnTile(player2.getBoard()))
            ViewRenderer.renderShip(tileFromMouse(player2.getBoard()));
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
        glfwPollEvents();
    }

    private boolean isMouseOnTile(ArrayList<Tile> tiles) {
        for (Tile t : tiles) {
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
        for (Tile t : tiles) {
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
        return new Tile("Null", new Vector3f(0.0f, 0.0f, 0.0f), "null");
    }

    private ArrayList<Tile> tilesFromTile(ArrayList<Tile> tiles, Tile tile, int number) {
        ArrayList<Tile> ts = new ArrayList<>();
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).equals(tile))
                for (int j = 0; j < number; j++)
                    if(i + (j * 10) < tiles.size())
                        ts.add(tiles.get(i + (j * 10)));
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

//    void showAdj(ArrayList<Tile> board, int pos) {
//        Tile el = board.get(pos-11);
//        System.out.printf("Element 50-11: \n\tName: %s\n\tCoords: %s\n",pos, el.getName(), el.getCoords().toString());
//        el = board.get(pos-10);
//        System.out.printf("Element 50-10: \n\tName: %s\n\tCoords: %s\n",pos, el.getName(), el.getCoords().toString());
//        el = board.get(pos-9);
//        System.out.printf("Element 50-9: \n\tName: %s\n\tCoords: %s\n",pos, el.getName(), el.getCoords().toString());
//        el = board.get(pos-1);
//        System.out.printf("Element 50-1: \n\tName: %s\n\tCoords: %s\n",pos, el.getName(), el.getCoords().toString());
//        el = board.get(pos);
//        System.out.printf("Element 50: \n\tName: %s\n\tCoords: %s\n",pos, el.getName(), el.getCoords().toString());
//        el = board.get(pos+1);
//        System.out.printf("Element 50+1: \n\tName: %s\n\tCoords: %s\n",pos, el.getName(), el.getCoords().toString());
//        el = board.get(pos+9);
//        System.out.printf("Element 50+9: \n\tName: %s\n\tCoords: %s\n",pos, el.getName(), el.getCoords().toString());
//        el = board.get(pos+10);
//        System.out.printf("Element 50+10: \n\tName: %s\n\tCoords: %s\n",pos, el.getName(), el.getCoords().toString());
//        el = board.get(pos+11);
//        System.out.printf("Element %d+11: \n\tName: %s\n\tCoords: %s\n", pos, el.getName(), el.getCoords().toString());
//    }
}