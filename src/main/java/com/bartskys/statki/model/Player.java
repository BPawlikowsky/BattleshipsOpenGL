package com.bartskys.statki.model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter@Setter
public class Player {
      @Getter@Setter
      private ArrayList<Tile> board;
      @Getter@Setter
      private String name;
      private List<Ship> ships = new ArrayList<>();

      public Player(ArrayList<Tile> board, String name) {
            this.board = board;
            this.name = name;
      }
}
