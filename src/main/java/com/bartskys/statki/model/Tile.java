package com.bartskys.statki.model;

import com.bartskys.statki.graphics.Texture;
import com.bartskys.statki.graphics.VertexArray;
import com.bartskys.statki.math.Vector3f;

public class Tile {

      private String name;
      private Vector3f coords;
      private boolean shotAt;
      private boolean owned;
      private String ownedByShip;
      private float SIZE = 1f;

      private Texture emptyTile;
      private Texture shipTile;
      private Texture shotAtTile;
      private VertexArray mesh;

      public Tile(String name, Vector3f coords) {

            this.coords = coords;
            this.name = name;
            shotAt = false;
            owned = false;
            ownedByShip = "";

            float[] vertices = {
                    0.0f, 0.0f, 0.1f,
                    0.0f,  SIZE / 2.0f, 0.1f,
                    SIZE / 2.0f,  SIZE / 2.0f, 0.1f,
                    SIZE / 2.0f, 0.0f, 0.1f
            };

            byte[] indices = {
                    0, 1, 2,
                    2, 3, 0
            };

            float[] tcs = {
                    0, 1,
                    0, 0,
                    1, 0,
                    1, 1,
            };

            mesh = new VertexArray(vertices, indices, tcs);
            emptyTile = new Texture("res/tile.png");
            shipTile = new Texture("res/ship.png");
            shotAtTile = new Texture("res/shot.png");
      }

      public Vector3f getCoords() {
            return coords;
      }

      public void setCoords(Vector3f coords) {
            this.coords = coords;
      }

      public boolean isShotAt() {
            return shotAt;
      }

      public void setShotAt(boolean shotAt) {
            this.shotAt = shotAt;
      }

      public boolean isOwned() {
            return owned;
      }

      public void setOwned(boolean owned) {
            this.owned = owned;
      }

      public String getOwnedByShip() {
            return ownedByShip;
      }

      public void setOwnedByShip(String ownedByShip) {
            this.ownedByShip = ownedByShip;
      }

      public Texture getEmptyTile() {
            return emptyTile;
      }

      public Texture getShipTile() {
            return shipTile;
      }

      public Texture getShotAtTile() {
            return shotAtTile;
      }

      public VertexArray getMesh() {
            return mesh;
      }

      public String getName() {
            return name;
      }
}
