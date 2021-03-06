package com.bartskys.statki.model;

import com.bartskys.statki.graphics.Texture;
import com.bartskys.statki.graphics.VertexArray;
import com.bartskys.statki.math.Vector3f;
import lombok.Getter;
import lombok.Setter;

public class Tile {
      private final float SIZE = 0.243f;
      @Getter @Setter
      private Vector3f position;
      @Getter @Setter
      private boolean shotAt;
      @Getter @Setter
      private boolean owned;
      @Getter @Setter
      private String ownedByShip;
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
      @Getter @Setter
      private VertexArray mesh;
      @Getter
      private final String player;

      public Tile(Tile t) {
            this.name = t.getName();
            this.emptyTile = t.emptyTile;
            this.shipTile = t.shipTile;
            this.hitTile = t.hitTile;
            this.shotAtTile = t.shotAtTile;
            this.mesh = t.mesh;
            this.owned = t.isOwned();
            this.shotAt = t.isShotAt();
            this.player = t.getPlayer();
            this.position = t.getPosition();
            this.ownedByShip = t.getOwnedByShip();
      }

      public Tile() {
            position = new Vector3f();
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
            emptyTile = new Texture("res/transp.png");
            shipTile = new Texture("res/ship.png");
            shotAtTile = new Texture("res/shot.png");
            hitTile = new Texture("res/hit.png");
      }

      public Tile(String name, Vector3f position, String player) {

            this.position = position;
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
            emptyTile = new Texture("res/transp.png");
            shipTile = new Texture("res/ship.png");
            shotAtTile = new Texture("res/shot.png");
            hitTile = new Texture("res/hit.png");
      }
}