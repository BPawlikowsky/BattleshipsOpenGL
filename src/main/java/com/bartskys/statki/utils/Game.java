package com.bartskys.statki.utils;

import com.bartskys.statki.math.Vector3f;
import com.bartskys.statki.model.Player;
import com.bartskys.statki.model.Ship;
import com.bartskys.statki.model.ShipEnum;
import com.bartskys.statki.model.Tile;

import java.util.ArrayList;
public class Game {
    public static void playerSetup(
            boolean clicked, int frames, float mouseX, float mouseY,
            Player player1, Player player2,
            boolean p1setup, boolean p2setup,
            int p1Ships, int p2Ships,
            ShipEnum shipType, boolean direction) {
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
                            Tile t = tileFromMouse(player1.getBoard(), mouseX, mouseY);
                            if (!t.isOwned())
                                if (checkAdjacent(t, player1.getBoard()))
                                    if(assembleShip(t, player1, 1, p1Ships, direction, shipType))
                                        p1Ships++;
                        }
                    }
                    break;
                    case 5:
                    case 6:
                    case 7: {
                        shipType = ShipEnum.DOUBLE;
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player1.getBoard(), mouseX, mouseY);
                            if (!t.isOwned())
                                if (checkAdjacent(t, player1.getBoard()))
                                    if(assembleShip(t, player1, 2, p1Ships, direction, shipType))
                                        p1Ships++;
                        }
                    }
                    break;
                    case 8:
                    case 9: {
                        shipType = ShipEnum.TRIPLE;
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player1.getBoard(), mouseX, mouseY);
                            if (!t.isOwned())
                                if (checkAdjacent(t, player1.getBoard()))
                                    if(assembleShip(t, player1, 3, p1Ships, direction, shipType))
                                        p1Ships++;
                        }
                    }
                    break;
                    case 10: {
                        shipType = ShipEnum.QUAD;
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player1.getBoard(), mouseX, mouseY);
                            if (!t.isOwned())
                                if (checkAdjacent(t, player1.getBoard()))
                                    if(assembleShip(t, player1, 4, p1Ships, direction, shipType)){
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
                            Tile t = tileFromMouse(player2.getBoard(), mouseX, mouseY);
                            if (!t.isOwned())
                                if (checkAdjacent(t, player2.getBoard()))
                                    assembleShip(t, player2, 1, p2Ships, direction, shipType);
                            p2Ships++;
                        }
                    }
                    break;
                    case 5:
                    case 6:
                    case 7: {
                        shipType = ShipEnum.DOUBLE;
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player2.getBoard(), mouseX, mouseY);
                            if (!t.isOwned())
                                if (checkAdjacent(t, player2.getBoard()))
                                    if(assembleShip(t, player2, 2, p2Ships, direction, shipType))
                                        p2Ships++;
                        }
                    }
                    break;
                    case 8:
                    case 9: {
                        shipType = ShipEnum.TRIPLE;
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player2.getBoard(), mouseX, mouseY);
                            if (!t.isOwned())
                                if (checkAdjacent(t, player2.getBoard()))
                                    if(assembleShip(t, player2, 3, p2Ships, direction, shipType))
                                        p2Ships++;
                        }
                    }
                    break;
                    case 10: {
                        shipType = ShipEnum.QUAD;
                        if (clicked && frames % 60 == 0) {
                            Tile t = tileFromMouse(player2.getBoard(), mouseX, mouseY);
                            if (!t.isOwned())
                                if (checkAdjacent(t, player2.getBoard()))
                                    if(assembleShip(t, player2, 4, p2Ships, direction, shipType))
                                        p2setup = false;
                        }
                    }
                    break;
                }
            }
        }
    }

    public static boolean checkAdjacent(Tile tile, ArrayList<Tile> board) {

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

    public static boolean assembleShip(Tile t, Player player, int number, int shipnum, boolean dir, ShipEnum shipType) {
        //if(!checkAdjacent(t, player.getBoard())) return false;
        ArrayList<Tile> tiles = tilesFromTile(player.getBoard(), t, number, dir);
        if(tiles.size() == 0) return false;
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

    public static void addShip(Player player, ShipEnum shipType, int shipNumber, ArrayList<Tile> t, boolean dir) {
        player.getShips().add(new Ship(t, dir, shipType + String.valueOf(shipNumber)));
    }

    public static boolean isMouseOnTile(ArrayList<Tile> tiles, float mouseX, float mouseY) {
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

    public static Tile tileFromMouse(ArrayList<Tile> tiles, float mouseX, float mouseY) {
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

    public static ArrayList<Tile> tilesFromTile(ArrayList<Tile> tiles, Tile tile, int number, boolean dir) {
        ArrayList<Tile> ts = new ArrayList<>();
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).equals(tile))
                for (int j = 0; j < number; j++)
                    if(i + (j * 10) < tiles.size())
                        if(dir)
                            ts.add(tiles.get(i + (j * 10)));
                        else
                            ts.add(tiles.get(i + j));
                    else return new ArrayList<>();
        }
        return ts;
    }

    public static ArrayList<Tile> generateTiles(float posX, float posY, String player) {
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
}
