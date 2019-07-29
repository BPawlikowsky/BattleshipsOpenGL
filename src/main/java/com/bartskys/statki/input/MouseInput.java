package com.bartskys.statki.input;

import org.lwjgl.glfw.*;

public class MouseInput extends GLFWCursorPosCallback {

      public static double xPos;
      public static double yPos;

      @Override
      public void invoke(long window, double xpos, double ypos) {
            xPos = xpos;
            yPos = ypos;
      }
}
