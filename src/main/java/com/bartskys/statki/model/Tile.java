package com.bartskys.statki.model;

import com.bartskys.statki.graphics.Texture;
import com.bartskys.statki.graphics.VertexArray;
import com.bartskys.statki.math.Vector3f;
import lombok.Getter;
import lombok.Setter;

public class Tile {
      
      @Getter @Setter
      private Vector3f coords;
      @Getter @Setter
      private boolean shotAt;
      @Getter @Setter
      private boolean owned;
      @Getter @Setter
      private String ownedByShip;
      private final float SIZE = 0.24f;

      @Getter
      private final String name;
      @Getter
      private final Texture emptyTile;
      @Getter
      private final Texture shipTile;
      @Getter
      private final Texture shotAtTile;
      @Getter
      private final Texture hitTile;
      @Getter
      private final VertexArray mesh;
      @Getter
      private final String player;

      public Tile() {
            coords = new Vector3f();
            name = "Null";
            player = "";
            shotAt = false;
            owned = false;
            ownedByShip = "";

            float[] vertices = {
                    -SIZE,  SIZE, 0.0f,
                    -SIZE, -SIZE, 0.0f,
                    SIZE, -SIZE, 0.0f,
                    SIZE,  SIZE, 0.0f
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
            hitTile = new Texture("res/hit.png");
      }

      public Tile(String name, Vector3f coords, String player) {

            this.coords = coords;
            this.name = name;
            this.player = player;
            shotAt = false;
            owned = false;
            ownedByShip = "";

            float[] vertices = {
                    -SIZE,  SIZE, 0.0f,
                    -SIZE, -SIZE, 0.0f,
                     SIZE, -SIZE, 0.0f,
                     SIZE,  SIZE, 0.0f
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
            hitTile = new Texture("res/hit.png");
      }
}