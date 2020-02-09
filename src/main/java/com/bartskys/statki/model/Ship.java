package com.bartskys.statki.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Ship {
      @Getter
      private List<Tile> tiles = new ArrayList<>();
      @Getter
      private boolean direction;
      @Getter
      private String name;

      public Ship(List<Tile> tiles, boolean direction, String name) {
            this.tiles = tiles;
            this.direction = direction;
            this.name = name;
      }
}
