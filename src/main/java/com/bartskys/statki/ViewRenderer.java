package com.bartskys.statki;

import com.bartskys.statki.graphics.Shader;
import com.bartskys.statki.input.Input;
import com.bartskys.statki.input.MouseInputClick;
import com.bartskys.statki.input.MouseInputPos;
import com.bartskys.statki.model.RenderBox;
import com.bartskys.statki.model.Tile;

import lombok.Getter;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;


import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class ViewRenderer {

      private static final int width = 1280;
      private static final int height = 720;

      private static long window;

      private static final Input input = new Input();

      private static Shader TILE;
      @Getter
      private static final Matrix4f pro_matrix = new Matrix4f().ortho2D(
              -10.0f,
              10.0f,
              -10.0f * 9.0f / 16.0f,
              10.0f * 9.0f / 16.0f
      );

      static long getWindow() {
            return window;
      }
      
      static void init() {

            if (!glfwInit()) {
                  //TODO Handle it!
                  return;
            }
            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

            window = glfwCreateWindow(width, height, "Statki", NULL, NULL);

            if(window == NULL) {
                  throw new RuntimeException("Failed to create the GLFW" + window);
            }

            try ( MemoryStack stack = stackPush() ) {
                  IntBuffer pWidth = stack.mallocInt(1); // int*
                  IntBuffer pHeight = stack.mallocInt(1); // int*

                  // Get the window size passed to glfwCreateWindow
                  glfwGetWindowSize(window, pWidth, pHeight);

                  // Get the resolution of the primary monitor
                  GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

                  // Center the window
                  assert vidmode != null;
                  glfwSetWindowPos(
                          window,
                          (vidmode.width() - pWidth.get(0)) / 2,
                          (vidmode.height() - pHeight.get(0)) / 2
                  );
            } catch (NullPointerException e) {
                  throw new NullPointerException("One or both window sizes returned null");
            }// the stack frame is popped automatically

            setCallbacks();

            glfwMakeContextCurrent(window);

            glfwSwapInterval(1);

            glfwShowWindow(window);
            GL.createCapabilities();
            System.out.println("OpenGL: " + glGetString(GL_VERSION));

            glClearColor(0f, 0f,0f, 1f);

            TILE = new Shader("shaders/shader.vert","shaders/shader.frag");

            initGameObjects();
      }

      public static void setCallbacks() {
            glfwSetKeyCallback(window, input);
            glfwSetCursorPosCallback(window, new MouseInputPos());
            glfwSetMouseButtonCallback(window, new MouseInputClick());
      }

      public static void initGameObjects() {
            setCallbacks();

            TILE.setUniformMat4f("pr_matrix", pro_matrix);
            TILE.setUniform1i("tex", 1);
      }

      static void renderStart() {
            flagSetup();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glClearColor(0.4f, 0.7f, 0.9f, 1f);
      }

      public static void flagSetup() {
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_BLEND);
            glActiveTexture(GL_TEXTURE1);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
      }

      static void renderFinish() {
            checkForErrors();
            glfwSwapBuffers(window);
      }

      private static void checkForErrors() {
            int error = glGetError();
            if (error != GL_NO_ERROR) {
                  System.out.println(error);
            }
      }

      static void terminate() {
            glfwDestroyWindow(window);
            glfwTerminate();
      }

      static void renderEmptyTile(Tile tile) {
            TILE.enable();
            tile.getEmptyTile().bind();
            Matrix4f ml_matrix = new Matrix4f().identity();
            TILE.setUniformMat4f("ml_matrix", ml_matrix.translate(tile.getPosition().x, tile.getPosition().y, tile.getPosition().z));
            tile.getMesh().render();
            tile.getMesh().unbind();
            tile.getEmptyTile().unbind();
            TILE.disable();
      }

      static void renderShot(Tile tile) {
            TILE.enable();
            tile.getShotAtTile().bind();
            Matrix4f ml_matrix = new Matrix4f().identity();
            TILE.setUniformMat4f("ml_matrix",ml_matrix.translate(tile.getPosition().x, tile.getPosition().y, tile.getPosition().z));
            tile.getMesh().render();
            tile.getMesh().unbind();
            tile.getShotAtTile().unbind();
            TILE.disable();
      }

      static void renderHit(Tile tile) {
            TILE.enable();
            tile.getHitTile().bind();
            Matrix4f ml_matrix = new Matrix4f().identity();
            TILE.setUniformMat4f("ml_matrix",ml_matrix.translate(tile.getPosition().x, tile.getPosition().y, tile.getPosition().z));
            tile.getMesh().render();
            tile.getMesh().unbind();
            tile.getHitTile().unbind();
            TILE.disable();
      }

      public static void renderShip(Tile tile) {
            TILE.enable();
            tile.getShipTile().bind();
            Matrix4f ml_matrix = new Matrix4f().identity();

            TILE.setUniformMat4f("ml_matrix",ml_matrix.translate(tile.getPosition().x, tile.getPosition().y, tile.getPosition().z));
            tile.getMesh().render();
            tile.getMesh().unbind();
            tile.getShipTile().unbind();
            TILE.disable();
      }

      public static void renderBox(RenderBox box) {
            TILE.enable();
            box.getTexture().bind();

            Matrix4f ml_matrix = new Matrix4f().identity();
            TILE.setUniformMat4f("ml_matrix",ml_matrix.translate(box.getPosition().x, box.getPosition().y, box.getPosition().z));
            box.getMesh().render();
            box.getMesh().unbind();
            box.getTexture().unbind();
            TILE.disable();
      }

      public static void renderBoxScale(RenderBox box, float scale) {
            TILE.enable();
            box.getTexture().bind();

            Matrix4f ml_matrix = new Matrix4f().identity();
            TILE.setUniformMat4f("ml_matrix",
                    ml_matrix.translate(
                            box.getPosition().x,
                            box.getPosition().y,
                           0.0f
                    ).scaleXY(scale,scale));
            box.getMesh().render();
            box.getMesh().unbind();
            box.getTexture().unbind();
            TILE.disable();
      }
}
