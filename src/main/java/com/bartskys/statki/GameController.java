package com.bartskys.statki;

import com.bartskys.statki.model.Player;
import com.bartskys.statki.model.Tile;

import java.util.ArrayList;

public class GameController {



      private Player player1, player2;

      public GameController() {

            ViewRenderer.init();
      }

      public void mainLoop() {

            ViewRenderer.run();
      }
}
