package com.bartskys.statki.utils;

import com.bartskys.statki.math.Vector3f;
import com.bartskys.statki.model.Player;
import com.bartskys.statki.model.Tile;

import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class GameUtils {
    public static boolean checkAdjacent(Tile tile, ArrayList<Tile> board) {

        float xLo = board.get(0).getPosition().x;
        float yLo = board.get(0).getPosition().y;
        float xHi = board.get(9).getPosition().x;
        float yHi = board.get(99).getPosition().y;
        //System.out.printf("Check Adj | xLo: %.2f | xHi: %.2f | yLo: %.2f | yHi: %.2f\n", xLo, xHi, yLo, yHi);
        for (int j = 0; j < board.size(); j++) {
            if (tile.equals(board.get(j))) {
                //System.out.println(board.get(j).getCoords().toString());
                if (board.get(j).getPosition().x > xLo &&
                        board.get(j).getPosition().x < xHi &&
                        board.get(j).getPosition().y < yLo &&
                        board.get(j).getPosition().y > yHi
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
                } else if (board.get(j).getPosition().x == xLo &&
                        board.get(j).getPosition().y < yLo &&
                        board.get(j).getPosition().y > yHi) {
                    if (board.get(j + 1).isOwned() ||
                            board.get(j - 9).isOwned() ||
                            board.get(j + 10).isOwned() ||
                            board.get(j - 10).isOwned() ||
                            board.get(j + 11).isOwned()
                    ) return false;
                } else if (board.get(j).getPosition().x == xHi &&
                        board.get(j).getPosition().y < yLo &&
                        board.get(j).getPosition().y > yHi) {
                    if (board.get(j - 1).isOwned() ||
                            board.get(j + 9).isOwned() ||
                            board.get(j + 10).isOwned() ||
                            board.get(j - 10).isOwned() ||
                            board.get(j - 11).isOwned()
                    ) return false;
                } else if (board.get(j).getPosition().y == yLo &&
                        board.get(j).getPosition().x > xLo &&
                        board.get(j).getPosition().x < xHi) {
                    if (board.get(j + 1).isOwned() ||
                            board.get(j - 1).isOwned() ||
                            board.get(j + 9).isOwned() ||
                            board.get(j + 10).isOwned() ||
                            board.get(j + 11).isOwned()
                    ) return false;
                } else if (board.get(j).getPosition().y == yHi &&
                        board.get(j).getPosition().x > xLo &&
                        board.get(j).getPosition().x < xHi) {
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

    public static boolean isMouseOnTile(ArrayList<Tile> tiles, double mouseX, double mouseY) {
        for (Tile t : tiles) {
            float diameter = 0.25f; //Board tile diameter
            if (
                    mouseX > t.getPosition().x - diameter &&
                            mouseX < (t.getPosition().x + diameter) &&
                            mouseY > (t.getPosition().y - diameter) &&
                            mouseY < (t.getPosition().y + diameter)
            ) {
                //System.out.println("Tile name: " + t.getName() + " x: " + t.getCoords().x + " y: " + t.getCoords().y + "\nMouse x: " + mouseX + " y: " + mouseY);
                return true;
            }
        }
        return false;
    }

    public static Tile tileFromMouse(ArrayList<Tile> tiles, double mouseX, double mouseY) {
        for (Tile t : tiles) {
            float diameter = 0.25f; //Board tile diameter
            if (
                    mouseX > (t.getPosition().x - diameter) &&
                            mouseX < (t.getPosition().x + diameter) &&
                            mouseY > (t.getPosition().y - diameter) &&
                            mouseY < (t.getPosition().y + diameter)
            ) {
                return t;
            }
        }
        return new Tile();
    }

    public static ArrayList<Tile> tilesFromTile(ArrayList<Tile> tiles, Tile tile, int number, boolean direction) {
        ArrayList<Tile> ts = new ArrayList<>();
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).equals(tile))
                for (int j = 0; j < number; j++) {
                    if(direction && i + (j * 10) >= tiles.size()) return new ArrayList<>();
                    if(!direction && i + j >= tiles.size()) return new ArrayList<>();
                    if(direction && !checkAdjacent(tiles.get(i + (j * 10)), tiles)) return new ArrayList<>();
                    if(!direction && !checkAdjacent(tiles.get(i + j), tiles)) return new ArrayList<>();
                    if(direction) {
                        ts.add(tiles.get(i + (j * 10)));
                    } else if(tiles.get(i + j).getName().charAt(1) == tiles.get(i).getName().charAt(1))
                        ts.add(tiles.get(i + j));
                    else return new ArrayList<>();
                }
        }
        return ts;
    }
    public static void randClick(Player player, double mouseX, double mouseY, int mClick, int mAction, boolean direction) {
        Vector3f randCoords = randTilePosition(player.getBoard(), direction);
        mouseX = randCoords.x;
        mouseY = randCoords.y;
        mClick = GLFW_MOUSE_BUTTON_1;
        mAction = GLFW_PRESS;
    }

    public static Vector3f randTilePosition(ArrayList<Tile> board, boolean direction) {
        Random r = new Random();
        int x = r.nextInt(10);
        int y = r.nextInt(10);
        if(x % 2 == 0) direction = !direction;
        String s = Integer.toString(x) + Integer.toString(y);
        for(Tile t : board) {
            if(s.equals(t.getName())) return t.getPosition();
        }
        return new Vector3f();
    }
}
