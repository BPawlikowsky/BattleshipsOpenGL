package com.bartskys.statki.graphics;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.BufferUtils.createIntBuffer;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

public class Texture {

      private int width, height;
      private final int texture;

      public Texture(String path) {
            texture = load(path);
      }

      public int getWidth() {
            return width;
      }

      public void setWidth(int width) {
            this.width = width;
      }

      public int getHeight() {
            return height;
      }

      public void setHeight(int height) {
            this.height = height;
      }

      private int load(String path) {

            stbi_set_flip_vertically_on_load(false);

            IntBuffer mWidth = createIntBuffer(1);
            IntBuffer mHeight = createIntBuffer(1);
            IntBuffer mBPP = createIntBuffer(1);

            ByteBuffer mLocalBuffer = stbi_load(path, mWidth, mHeight, mBPP, 4);

            int result = glGenTextures();


            glBindTexture(GL_TEXTURE_2D, result);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D,0, GL_RGBA, mWidth.get(0), mHeight.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, mLocalBuffer);
            glBindTexture(GL_TEXTURE_2D, 0);

            width = mWidth.get(0);
            height = mHeight.get(0);
            return result;
      }

      public void bind() {
            glBindTexture(GL_TEXTURE_2D, texture);
      }

      public void unbind() {
            glBindTexture(GL_TEXTURE_2D, 0);
      }
}
