package com.bartskys.statki.level;

import com.bartskys.statki.graphics.Shader;
import com.bartskys.statki.graphics.Texture;
import com.bartskys.statki.graphics.VertexArray;
import com.bartskys.statki.input.Input;
import com.bartskys.statki.math.Matrix4f;
import com.bartskys.statki.math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Bird {

      private float SIZE = 1.0f;
      private Texture texture;
      private VertexArray mesh;

      private Vector3f position = new Vector3f();
      private float rot;
      private float delta = 0.0f;

      public Bird() {
            float[] vertices = {
                    -SIZE / 2.0f, -SIZE / 2.0f, 0.1f,
                    -SIZE / 2.0f,  SIZE / 2.0f, 0.1f,
                     SIZE / 2.0f,  SIZE / 2.0f, 0.1f,
                     SIZE / 2.0f, -SIZE / 2.0f, 0.1f
            };

            byte[] indices = {
                    0, 1, 2,
                    2, 3, 0
            };

            float[] tcs = {
                    0, 1,
                    0, 0,
                    1, 0,
                    1, 1,
            };

            mesh = new VertexArray(vertices, indices, tcs);
            texture = new Texture("res/bird.png");
      }

      public Vector3f getPosition() {
            return position;
      }

      public float getSIZE() {
            return SIZE;
      }

      void update(boolean playerControl) {
            position.y -= delta;
            if (playerControl && Input.isKeyDown(GLFW_KEY_SPACE))
                  delta = -0.15f;
            else
                  delta += 0.01f;

            rot = -delta * 90.0f;

      }

      void fall() {
            delta = -0.15f;
      }

      void render() {

            Shader.BIRD.enable();
            texture.bind();
            Shader.BIRD.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rot)));
            mesh.render();
            mesh.unbind();
            texture.unbind();
            Shader.BIRD.disable();
      }

}
