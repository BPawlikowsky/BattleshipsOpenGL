package com.bartskys.statki;

import com.bartskys.statki.graphics.Shader;
import com.bartskys.statki.input.Input;
import com.bartskys.statki.input.MouseInput;
import com.bartskys.statki.model.Tile;
import glm_.vec2.Vec2;
import glm_.vec4.Vec4;
import lombok.Getter;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import uno.glfw.GlfwWindow;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL33.GL_TEXTURE1;
import static org.lwjgl.opengl.GL33.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ViewRenderer {

      private static float[] f = {0f};
      private static Vec4 clearColor = new Vec4(0.45f, 0.55f, 0.6f, 1f);
      // Java users can use both a MutableProperty0 or a Boolean Array
      private static MutableProperty0<Boolean> showAnotherWindow = new MutableProperty0<>(false);
      private static boolean[] showDemo = {true};
      private static int[] counter = {0};

      private static final int width = 1280;
      private static final int height = 720;

      private static long window;;

      private static ImGui imgui = ImGui.INSTANCE;
      private static IO io;
      private static Context ctx;
      private static ImplGlfw implGlfw;
      private static ImplGL3 implGl3;
      static GlfwWindow glfwWindow;

      private static Shader TILE;
      @Getter
      private static Matrix4f pro_matrix = new Matrix4f().ortho2D(
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

            // Setup Dear ImGui context
            ctx = new Context();
            //io.configFlags = io.configFlags or ConfigFlag.NavEnableKeyboard  // Enable Keyboard Controls
            //io.configFlags = io.configFlags or ConfigFlag.NavEnableGamepad   // Enable Gamepad Controls

            // Setup Dear ImGui style
            imgui.styleColorsDark(null);
//        imgui.styleColorsClassic(null)

            // Setup Platform/Renderer bindings
            glfwWindow = GlfwWindow.from(window);
            implGlfw = new ImplGlfw( glfwWindow, true, null);
            implGl3 = new ImplGL3();

            io = imgui.getIo();

            initGameObjects();
      }

      private static void initGameObjects() {
            TILE = new Shader("shaders/bird.vert","shaders/bird.frag");

            TILE.setUniformMat4f("pr_matrix", pro_matrix);
            TILE.setUniform1i("tex", 1);
      }

      static void renderStart() {

            implGl3.newFrame();
            implGlfw.newFrame();

            imgui.newFrame();

            imgui.text("Hello, world!");                                // Display some text (you can use a format string too)
            imgui.sliderFloat("float", f, 0, 0f, 1f, "%.3f", 1f);       // Edit 1 float using a slider from 0.0f to 1.0f
            imgui.colorEdit3("clear color", clearColor, 0);               // Edit 3 floats representing a color

            imgui.checkbox("Demo Window", showDemo);                 // Edit bools storing our windows open/close state
            imgui.checkbox("Another Window", showAnotherWindow);

            if (imgui.button("Button", new Vec2())) // Buttons return true when clicked (NB: most widgets return true when edited/activated)
                  counter[0]++;

            imgui.sameLine(0f, -1f);
            imgui.text("counter = " + counter[0]);

            imgui.text("Application average %.3f ms/frame (%.1f FPS)", 1_000f / io.getFramerate(), io.getFramerate());

            // 2. Show another simple window. In most cases you will use an explicit begin/end pair to name the window.
            if (showAnotherWindow.get()) {
                  imgui.begin("Another Window", showAnotherWindow, 0);
                  imgui.text("Hello from another window!");
                  if (imgui.button("Close Me", new Vec2()))
                        showAnotherWindow.set(false);
                  imgui.end();
            }

        /*  3. Show the ImGui demo window. Most of the sample code is in imgui.showDemoWindow().
                Read its code to learn more about Dear ImGui!  */
            if (showDemo[0]) {
            /*  Normally user code doesn't need/want to call this because positions are saved in .ini file anyway.
                    Here we just want to make the demo initial state a bit more friendly!                 */
                  imgui.setNextWindowPos(new Vec2(650, 20), Cond.FirstUseEver, new Vec2());
                  imgui.showDemoWindow(showDemo);
            }

            // Rendering
            imgui.render();
            glViewport(
                    glfwWindow.getFramebufferSize().getX(),
                    glfwWindow.getFramebufferSize().getY(),
                    1280, 720
            );
            glClearColor(clearColor.getX(), clearColor.getY(), clearColor.getZ(), clearColor.getW());
            glClear(GL_COLOR_BUFFER_BIT);
            implGl3.renderDrawData(imgui.getDrawData());

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
            Matrix4f ml_matrix = new Matrix4f().identity();
            TILE.setUniformMat4f("ml_matrix", ml_matrix.translate(tile.getCoords().x, tile.getCoords().y, tile.getCoords().z));
            tile.getMesh().render();
            tile.getMesh().unbind();
            tile.getEmptyTile().unbind();
            TILE.disable();
      }

      static void renderShot(Tile tile) {

            TILE.enable();
            tile.getShotAtTile().bind();
            Matrix4f ml_matrix = new Matrix4f().identity();
            TILE.setUniformMat4f("ml_matrix",ml_matrix.translate(tile.getCoords().x, tile.getCoords().y, tile.getCoords().z));
            tile.getMesh().render();
            tile.getMesh().unbind();
            tile.getShotAtTile().unbind();
            TILE.disable();
      }

      public static void renderShip(Tile tile) {

            TILE.enable();
            tile.getShipTile().bind();
            Matrix4f ml_matrix = new Matrix4f().identity();

            TILE.setUniformMat4f("ml_matrix",ml_matrix.translate(tile.getCoords().x, tile.getCoords().y, tile.getCoords().z));
            tile.getMesh().render();
            tile.getMesh().unbind();
            tile.getShipTile().unbind();
            TILE.disable();
      }

}
