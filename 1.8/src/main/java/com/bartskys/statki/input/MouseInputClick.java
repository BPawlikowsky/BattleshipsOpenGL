package com.bartskys.statki.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseInputClick extends GLFWMouseButtonCallback {
    public static int mbutton;
    public static int maction;

    @Override
    public void invoke(long window, int button, int action, int mods) {
        mbutton = button;
        maction = action;
    }
}
