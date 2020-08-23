package com.bartskys.statki.model;

import com.bartskys.statki.math.Vector3f;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

import static com.bartskys.statki.utils.GameUtils.tilesFromTile;

@Getter@Setter
public class Player {
      @Getter@Setter
      private ArrayList<Tile> board;
      @Getter@Setter
      private String name;
      private List<Ship> ships = new ArrayList<>();

      public Player(float posX, float posY, float scale, String name) {
            this.board = generateTiles(posX, posY, scale);
            this.name = name;
      }

      public boolean assembleShip(Tile t, int number, int shipnum, ShipEnum shipType, boolean dir) {
            ArrayList<Tile> tiles = tilesFromTile(this.getBoard(), t, number, dir);
            if(tiles.size() == 0) return false;
            addShip(shipType, shipnum, tiles, dir);

            for (Tile ptiles : this.getBoard()) {
                  for (Tile tile : tiles) {
                        if (tile.equals(ptiles)) {
                              ptiles.setOwned(true);
                              ptiles.setOwnedByShip(shipType + String.valueOf(shipnum));
                        }
                  }
            }
            return true;
      }

      void addShip(ShipEnum shipType, int shipNumber, ArrayList<Tile> t, boolean dir) {
            this.getShips().add(new Ship(t, dir, shipType + String.valueOf(shipNumber)));
            System.out.println(this.getName() + " added ship " + shipType + " of number " + shipNumber +
                    " in dir: " + ((dir) ? "horizontal":"vertical"));
      }
      private ArrayList<Tile> generateTiles(float posX, float posY, float scale) {
            ArrayList<Tile> tiles = new ArrayList<>();

            for (int y = 9; y >= 0; y--) {
                  for (int x = 0; x < 10; x++) {
                        tiles.add(new Tile(
                                String.valueOf(x) + (9 - y),
                                new Vector3f(
                                        (x * scale) + posY,
                                        (y * scale) + posX,
                                        0.0f), name)
                        );
                  }
            }
            return tiles;
      }
}
