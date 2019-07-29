package com.bartskys.statki;

import com.bartskys.statki.graphics.Shader;
import com.bartskys.statki.input.Input;
import com.bartskys.statki.input.MouseInput;
import com.bartskys.statki.math.Matrix4f;
import com.bartskys.statki.math.Vector3f;
import com.bartskys.statki.model.Tile;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ViewRenderer {



      private static final int width = 1280;
      private static final int height = 720;

      private static long window;

      private static Shader TILE;

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

            try ( MemoryStack stack = MemoryStack.stackPush() ) {
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

            glfwSetKeyCallback(window, new Input());
            glfwSetCursorPosCallback(window, new MouseInput());

            glfwMakeContextCurrent(window);

            glfwSwapInterval(1);

            glfwShowWindow(window);
            GL.createCapabilities();
            System.out.println("OpenGL: " + glGetString(GL_VERSION));

            glClearColor(0f, 0f,0f, 1f);

            glEnable(GL_DEPTH_TEST);
            glEnable(GL_BLEND);
            glActiveTexture(GL_TEXTURE1);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            initGameObjects();
      }

      public static Vector3f mousePosition(long window, GLFWCursorPosCallback callback) {

            glfwSetCursorPosCallback(window, callback);
            double[] xPos = new double[1], yPos = new double[1];
            glfwGetCursorPos(window, xPos, yPos);
            return new Vector3f((float) xPos[0],(float) yPos[0], 0.0f);
      }

      private static void initGameObjects() {
            TILE = new Shader("shaders/bird.vert","shaders/bird.frag");
            Matrix4f pr_matrix = Matrix4f.orthographic(
                    0.0f, 20f,
                    0.0f, 20f * 9.0f / 16.0f,
                    -1.0f, 1.0f
            );

            TILE.setUniformMat4f("pr_matrix", pr_matrix);
            TILE.setUniform1i("tex", 1);
      }

      static void renderStart() {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glClearColor(0.4f, 0.7f, 0.9f, 1f);
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
            TILE.setUniformMat4f("ml_matrix", Matrix4f.translate(tile.getCoords()));
            tile.getMesh().render();
            tile.getMesh().unbind();
            tile.getEmptyTile().unbind();
            TILE.disable();
      }

      static void renderShot(Tile tile) {

            TILE.enable();
            tile.getShotAtTile().bind();
            TILE.setUniformMat4f("ml_matrix", Matrix4f.translate(tile.getCoords()));
            tile.getMesh().render();
            tile.getMesh().unbind();
            tile.getShotAtTile().unbind();
            TILE.disable();
      }

      public static void renderShip(Tile tile) {

            TILE.enable();
            tile.getShipTile().bind();
            TILE.setUniformMat4f("ml_matrix", Matrix4f.translate(tile.getCoords()));
            tile.getMesh().render();
            tile.getMesh().unbind();
            tile.getShipTile().unbind();
            TILE.disable();
      }

}
