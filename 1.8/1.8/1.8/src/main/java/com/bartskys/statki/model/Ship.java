package com.bartskys.statki.model;

import lombok.Getter;
import java.util.List;

public class Ship {
      @Getter
      private final List<Tile> tiles;
      @Getter
      private final boolean direction;
      @Getter
      private final String name;

      public Ship(List<Tile> tiles, boolean direction, String name) {
            this.tiles = tiles;
            this.direction = direction;
            this.name = name;
      }
}
