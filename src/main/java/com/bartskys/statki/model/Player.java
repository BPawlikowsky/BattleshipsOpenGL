package com.bartskys.statki.model;

import java.util.ArrayList;

public class Player {

      private ArrayList<Tile> board;
      private String name;

      public Player(ArrayList<Tile> board, String name) {
            this.board = board;
            this.name = name;
      }

      public ArrayList<Tile> getBoard() {
            return board;
      }
}
