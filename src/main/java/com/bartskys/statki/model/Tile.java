package com.bartskys.statki.model;

import com.bartskys.statki.math.Vector3f;

public class Tile {

      private String name;
      private Vector3f coords;
      private boolean shotAt;
      private boolean owned;
      private String ownedByShip;

      public Tile(String name, Vector3f coords) {
            this.coords = coords;
            this.name = name;
            shotAt = false;
            owned = false;
            ownedByShip = "";
      }
}
