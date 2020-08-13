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
      private float SIZE = 0.49f;

      @Getter
      private String name;
      @Getter
      private Texture emptyTile;
      @Getter
      private Texture shipTile;
      @Getter
      private Texture shotAtTile;
      @Getter
      private VertexArray mesh;

<<<<<<< Updated upstream
      public Tile(String name, Vector3f coords) {
=======
      public Tile() {
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
      }

      public Tile(String name, Vector3f coords, String player) {
>>>>>>> Stashed changes

            this.coords = coords;
            this.name = name;
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
      }
}