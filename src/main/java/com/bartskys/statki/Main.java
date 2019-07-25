package com.bartskys.statki;

import com.bartskys.statki.graphics.Shader;
import com.bartskys.statki.input.Input;
import com.bartskys.statki.level.Level;
import com.bartskys.statki.math.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.*;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main implements Runnable {

    private final int width = 1280;
    private final int height = 720;

    private boolean running = false;

    private long window;

    private Level level;

    private void start() {

        running = true;
        Thread thread = new Thread(this, "Game");
        thread.start();
    }

    private void init() {

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

    private void initGameObjects() {
        Shader.loadAll();
        Matrix4f pr_matrix = Matrix4f.orthographic(
                -10.0f, 10.0f,
                -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f,
                -1.0f, 1.0f
        );
        Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BG.setUniform1i("tex", 1);

        Shader.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BIRD.setUniform1i("tex", 1);

        Shader.PIPE.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.PIPE.setUniform1i("tex", 1);

        level = new Level();
    }

    public void run() {

        init();

        long lastTime = System.currentTimeMillis();
        long timer = System.currentTimeMillis();
        double delta = 0.0;
        double ns = 1000000000.0 / 60.0;
        int updates = 0;
        int frames = 0;

        while (running) {

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if ( delta >= 1.0 ) {
                update();
                updates++;
                delta--;
            }

            render();

            frames++;
            if ( System.currentTimeMillis() - timer > 1000 ) {
                timer += 1000;
                System.out.println(updates + " ups, " + frames + " fps" );
                updates = 0;
                frames = 0;

            }

            if(glfwWindowShouldClose(window))

                running = false;
        }

        terminate();
    }

    private void update() {

        glfwPollEvents();
        level.update();
        if (level.isGameOver() && Input.isKeyDown(GLFW_KEY_SPACE))
            level = new Level();
    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        level.render();

        checkForErrors();
        glfwSwapBuffers(window);
    }

    private void checkForErrors() {
        int error = glGetError();
        if (error != GL_NO_ERROR) {
            System.out.println(error);
        }
    }

    private void terminate() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
