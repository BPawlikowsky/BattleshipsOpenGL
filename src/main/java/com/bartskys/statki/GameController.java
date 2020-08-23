package com.bartskys.statki;

import com.bartskys.statki.input.Input;
import com.bartskys.statki.input.MouseInputClick;
import com.bartskys.statki.input.MouseInputPos;
import com.bartskys.statki.math.Vector3f;
import com.bartskys.statki.model.*;

import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

class GameController {

    private static final String PLAYER1 = "Player01";
    private static final String PLAYER2 = "Player02";
    private static RenderBox PLAYER1SETUP;
    private static RenderBox PLAYER2SETUP;
    private static RenderBox BACKGROUND;
    int p1Ships = 1;
    int p2Ships = 1;
    float scale = 0.489f;
    private static boolean running;
    private final Player player1, player2;
    private boolean p1setup, p2setup;
    float boardPosX = -1.1f;
    float boardPosY = 2.52f;
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
    private double mouseX, mouseY;
    int mClick; int mAction;
    boolean clickWait = false;
    boolean buttonD = false;
    boolean turn = true;
    boolean winner = false;

    GameController() {
        ViewRenderer.init();
        running = true;
        player1 = new Player(generateTiles(boardPosX, boardPosY - 10.0f, PLAYER1), PLAYER1);
        player2 = new Player(generateTiles(boardPosX, boardPosY +0.265f, PLAYER2), PLAYER2);
        p1setup = p2setup = true;
        PLAYER1SETUP = new RenderBox(
                "PlayerSetup",
                "res/playersetup.png",
                new Vector3f(-5.8f, 4.0f, 0.0f),
                1.28f,
                0.24f
        );
        PLAYER2SETUP = new RenderBox(
                "PlayerSetup",
                "res/playersetup.png",
                new Vector3f(5.5f, 4.0f, 0.0f),
                1.28f,
                0.24f
        );
        BACKGROUND = new RenderBox(
                "background",
                "res/statkitlo.png",
                new Vector3f(0.0f, 0.0f, -0.1f),
                12.80f * 0.8f, 7.20f * 0.8f
        );
    }

    void mainLoop() {

        //Main Loop
        while (running) {
            if(p1setup) {
                randClick(player1);
            }
            if(!p1setup && p2setup) {
                randClick(player2);
            }

            // Player Setup
            if((p1setup || p2setup) && Input.isKeyDown(GLFW_KEY_D) && !buttonD) {
                direction = !direction;
                buttonD = true;
            }
            if(mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait) {
                Tile tempT;
                if(isMouseOnTile(player1.getBoard(), mouseX, mouseY))
                    tempT = tileFromMouse(player1.getBoard(), mouseX, mouseY);
                else if (isMouseOnTile(player2.getBoard(), mouseX,mouseY))
                    tempT = tileFromMouse(player2.getBoard(), mouseX, mouseY);
                else tempT = new Tile();
                if(p1setup)
                    playerSetup(tempT, player1);
                if(p2setup && !p1setup)
                    playerSetup(tempT, player2);
            }

            // Main Game
            if(!p1setup && !p2setup && !winner) {
                if(turn) {
                    randClick(player2);
                    if(mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait)
                        if(isMouseOnTile(player2.getBoard(), mouseX, mouseY)) {
                            clickWait = true;
                            Tile t = tileFromMouse(player2.getBoard(), mouseX, mouseY);
                            if(!t.isShotAt())
                                if(shoot(t, player2)) turn = !turn;
                    }
                } else {
                    randClick(player1);
                    if(mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait)
                        if(isMouseOnTile(player1.getBoard(), mouseX, mouseY)) {
                            clickWait = true;
                            Tile t = tileFromMouse(player1.getBoard(), mouseX, mouseY);
                            if(!t.isShotAt())
                                if(shoot(t, player1)) turn = !turn;
                    }
                }
                if(winner(player1) || winner(player2)) winner = true;
            }
            // Update game events and Counts frames and updates
            frameUpdate();

            // Render after update
            render();

            if (glfwWindowShouldClose(ViewRenderer.getWindow()))
                running = false;
        }
        ViewRenderer.terminate();
    }

    private void randClick(Player player) {
        Vector3f randCoords = randTileCoords(player.getBoard());
        mouseX = randCoords.x;
        mouseY = randCoords.y;
        mClick = GLFW_MOUSE_BUTTON_1;
        mAction = GLFW_PRESS;
    }

    Vector3f randTileCoords(ArrayList<Tile> board) {
        Random r = new Random();
        int x = r.nextInt(10);
        int y = r.nextInt(10);
        if(x % 2 == 0) direction = !direction;
        String s = Integer.toString(x) + Integer.toString(y);
        for(Tile t : board) {
            if(s.equals(t.getName())) return t.getCoords();
        }
        return new Vector3f();
    }

    private boolean shoot(Tile shot, Player player) {
        for (Tile t : player.getBoard()) {
            if(t.equals(shot)) {
                t.setShotAt(true);
                return true;
            }
        }
        return false;
    }

    private void render() {
        ViewRenderer.renderStart();
        ViewRenderer.renderBox(BACKGROUND);
        if (p1setup) {
            ViewRenderer.renderBox(PLAYER1SETUP);
            renderSetup(player1);
        }
        else if (p2setup) {
            ViewRenderer.renderBox(PLAYER2SETUP);
            renderSetup(player2);
        }
        if(!p1setup && !p2setup && turn && !winner)
            renderShotBoard(player2);
        else if(!p1setup && !p2setup && !turn)
            renderShotBoard(player1);

        if(winner(player1)) ViewRenderer.renderBox(PLAYER1SETUP);
        if(winner(player2)) ViewRenderer.renderBox(PLAYER2SETUP);
        if(winner) {
            renderWinnerBoard(player1);
            renderWinnerBoard(player2);
        }

        ViewRenderer.renderFinish();
    }

    private void update() {
        // Aligning mouse position to the board so that the mouse coords correspond to the board coords
        mouseX = ((float) MouseInputPos.xPos / 1280f - 1.0f) * 10.0f;
        mouseY = ((float) MouseInputPos.yPos / 720f - 1.0f) * (-10.0f * 9.0f / 16.0f);
        mClick = MouseInputClick.mbutton;
        mAction = MouseInputClick.maction;
        // Click reset
        if (frames >= 30 && mAction != GLFW_PRESS && clickWait)
            clickWait = false;
        if (frames >= 30 && Input.isKeyDown(GLFW_KEY_D) && buttonD)
            buttonD = false;

        glfwPollEvents();
    }

    private void renderSetup(Player player) {
        int shipNum = 0;
        if(player.getName() == PLAYER1)
            shipNum = p1Ships;
        else shipNum = p2Ships;


        if (isMouseOnTile(player.getBoard(), mouseX, mouseY)) {
            ArrayList<Tile> tiles;
            tiles = tilesFromTile(player.getBoard(), tileFromMouse(player.getBoard(), mouseX, mouseY), 1);

            if(shipNum > 4 && shipNum <= 7)
                tiles = tilesFromTile(player.getBoard(), tileFromMouse(player.getBoard(), mouseX, mouseY), 2);
            if(shipNum > 7 && shipNum < 10)
                tiles = tilesFromTile(player.getBoard(), tileFromMouse(player.getBoard(), mouseX, mouseY), 3);
            if(shipNum == 10)
                tiles = tilesFromTile(player.getBoard(), tileFromMouse(player.getBoard(), mouseX, mouseY), 4);
            for (Tile t: tiles) {
                ViewRenderer.renderShip(t);
            }
        }
        renderBoard(player);
    }

    private void renderBoard(Player player) {
        for (Tile t : player.getBoard()) {
            if (t.isOwned())
                ViewRenderer.renderShip(t);
            else
                ViewRenderer.renderEmptyTile(t);
        }
    }

    private void renderShotBoard(Player player) {
        for (Tile t : player.getBoard()) {
            if (t.isShotAt()) {
                if(t.isOwned())
                    ViewRenderer.renderHit(t);
                else ViewRenderer.renderShot(t);
            }
            else
                ViewRenderer.renderEmptyTile(t);
        }
    }

    private void renderWinnerBoard(Player player) {
        for (Tile t : player.getBoard()) {
            if (t.isShotAt() && t.isOwned()) {
                ViewRenderer.renderHit(t);
            }
            if(t.isShotAt() && !t.isOwned()) ViewRenderer.renderShot(t);
            else ViewRenderer.renderEmptyTile(t);
        }
    }

    private boolean winner(Player player) {
        int owned, shot;
        owned = shot = 0;
        for (Tile t : player.getBoard()) {
            if(t.isShotAt() && t.isOwned()) shot++;
            if(t.isOwned()) owned++;
        }
        if(shot == owned) return true;
        else return false;
    }

    void playerSetup(Tile t, Player player) {
        if (p1setup || p2setup) {
            int pShips = -1;
            if (p1setup)
                pShips = p1Ships;
            if(p2setup && !p1setup)
                pShips = p2Ships;
            switch (pShips) {
                case 0: case 1: case 2: case 3: case 4: {
                    shipType = ShipEnum.SINGLE;
                    if (mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait) {
                        clickWait = true;
                        if (!t.isOwned())
                            if (checkAdjacent(t, player.getBoard()))
                                if (assembleShip(t, player, 1, pShips, direction))
                                    if(player.getName().equals(PLAYER1))
                                        p1Ships++;
                                    else if(player.getName().equals(PLAYER2)) p2Ships++;
                    }
                }
                break;
                case 5: case 6: case 7: {
                    shipType = ShipEnum.DOUBLE;
                    if (mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait) {
                        clickWait = true;
                        if (!t.isOwned())
                            if (checkAdjacent(t, player.getBoard()))
                                if (assembleShip(t, player, 2, pShips, direction))
                                    if(player.getName().equals(PLAYER1))
                                        p1Ships++;
                                    else p2Ships++;
                    }
                }
                break;
                case 8: case 9: {
                    shipType = ShipEnum.TRIPLE;
                    if (mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait) {
                        clickWait = true;
                        if (!t.isOwned())
                            if (checkAdjacent(t, player.getBoard()))
                                if (assembleShip(t, player, 3, pShips, direction))
                                    if(player.getName().equals(PLAYER1))
                                        p1Ships++;
                                    else p2Ships++;
                    }
                } break;
                case 10: {
                    shipType = ShipEnum.QUAD;
                    if (mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait) {
                        clickWait = true;
                        if (!t.isOwned())
                            if (checkAdjacent(t, player.getBoard()))
                                if (assembleShip(t, player, 4, pShips, direction))
                                    if(player.getName().equals(PLAYER1)) {
                                        //showBoard(player1);
                                        p1setup = false;
                                    }
                                    else {
                                        //showBoard(player2);
                                        p2setup = false;
                                    }
                    }
                } break;
            }
        }
    }

    boolean checkAdjacent(Tile tile, ArrayList<Tile> board) {

<<<<<<< Updated upstream
        float xLo = board.get(0).getCoords().x;
        float yLo = board.get(0).getCoords().y;
        float xHi = board.get(9).getCoords().x;
        float yHi = board.get(99).getCoords().y;
        //System.out.printf("Check Adj | xLo: %.2f | xHi: %.2f | yLo: %.2f | yHi: %.2f\n", xLo, xHi, yLo, yHi);
        for (int j = 0; j < board.size(); j++) {
            if (tile.equals(board.get(j))) {
                //System.out.println(board.get(j).getCoords().toString());
                if (board.get(j).getCoords().x > xLo &&
                        board.get(j).getCoords().x < xHi &&
                        board.get(j).getCoords().y < yLo &&
                        board.get(j).getCoords().y > yHi
=======
        float xLo = board.get(0).getPosition().x;
        float yLo = board.get(0).getPosition().y;
        float xHi = board.get(9).getPosition().x;
        float yHi = board.get(99).getPosition().y;

        for (int j = 0; j < board.size(); j++) {
            if (tile.equals(board.get(j))) {

                if (board.get(j).getPosition().x > xLo &&
                        board.get(j).getPosition().x < xHi &&
                        board.get(j).getPosition().y < yLo &&
                        board.get(j).getPosition().y > yHi
>>>>>>> Stashed changes
                ) {
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

    boolean assembleShip(Tile t, Player player, int number, int shipnum, boolean dir) {
        ArrayList<Tile> tiles = tilesFromTile(player.getBoard(), t, number);
        if(tiles.size() == 0) return false;
        addShip(player, shipType, shipnum, tiles, dir);

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
        System.out.println(
                player.getName() + " added ship " +
                        shipType + " of number " + shipNumber +
                " in dir: " + ((dir) ? "horizontal":"vertical"));
    }

    private boolean isMouseOnTile(ArrayList<Tile> tiles, double mouseX, double mouseY) {
        for (Tile t : tiles) {
            float diameter = 0.25f; //Board tile diameter
            if (
                    mouseX > t.getCoords().x - diameter &&
                            mouseX < (t.getCoords().x + diameter) &&
                            mouseY > (t.getCoords().y - diameter) &&
                            mouseY < (t.getCoords().y + diameter)
            ) {
                return true;
            }
        }
        return false;
    }

    private Tile tileFromMouse(ArrayList<Tile> tiles, double mouseX, double mouseY) {
        for (Tile t : tiles) {
            float diameter = 0.25f; //Board tile diameter
            if (
                    mouseX > (t.getCoords().x - diameter) &&
                            mouseX < (t.getCoords().x + diameter) &&
                            mouseY > (t.getCoords().y - diameter) &&
                            mouseY < (t.getCoords().y + diameter)
            ) {
                return t;
            }
        }
        return new Tile();
    }

    private ArrayList<Tile> tilesFromTile(ArrayList<Tile> tiles, Tile tile, int number) {
        ArrayList<Tile> tileList = new ArrayList<>();
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).equals(tile))
                for (int j = 0; j < number; j++) {
                    if(direction && i + (j * 10) >= tiles.size()) return tileList;
                    if(!direction && i + j >= tiles.size()) return tileList;
                    if(direction && !checkAdjacent(tiles.get(i + (j * 10)), tiles)) return tileList;
                    if(!direction && !checkAdjacent(tiles.get(i + j), tiles)) return tileList;
                    if(direction) {
                            tileList.add(tiles.get(i + (j * 10)));
                    } else if(tiles.get(i + j).getName().charAt(1) == tiles.get(i).getName().charAt(1))
                                tileList.add(tiles.get(i + j));
                    else return new ArrayList<>();
                }
        }
        return tileList;
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

    private ArrayList<Tile> generateTiles(float posX, float posY, String player) {
        ArrayList<Tile> tiles = new ArrayList<>();

        for (int y = 9; y >= 0; y--) {
            for (int x = 0; x <= 9; x++) {
                tiles.add(new Tile(
                        String.valueOf(x) + (9 - y),
                        new Vector3f(
                                (x * scale) + posY,
                                (y * scale) + posX,
                                0.0f), player)
                );
            }
        }
        return tiles;
    }
<<<<<<< Updated upstream

    void showBoard(Player player) {
        for (Tile t :
                player.getBoard()) {
            if(t.isOwned()) System.out.println(t.getName() + " Owned: " +
                    t.isOwned() + " Owned by ship: " +
                    t.getOwnedByShip() + " Coords: " +
                    t.getCoords().toString());
        }
    }
=======
>>>>>>> Stashed changes
}