package com.bartskys.statki.level;

import com.bartskys.statki.graphics.Shader;
import com.bartskys.statki.graphics.Texture;
import com.bartskys.statki.graphics.VertexArray;
import com.bartskys.statki.math.Matrix4f;
import com.bartskys.statki.math.Vector3f;

import java.util.Random;

public class Level {

      private VertexArray mesh, fade;
      private Texture bgTexture;

      private int xScroll = 0;
      private int map;

      private Bird bird;

      private Random random = new Random();

      private Pipe[] pipes = new Pipe[5 * 2];

      private static int index = 0;

      private float OFFSET = 5.0f;

      private float time = 0.0f;

      private boolean playerControl = true;

      public Level() {
            index = 0;
            float[] vertices = {
                    -10f, -10f  * 9.0f / 16.0f, -0.4f,
                    -10f,  10f  * 9.0f / 16.0f, -0.4f,
                     0f,  10f  * 9.0f / 16.0f,  -0.4f,
                     0f,  -10f  * 9.0f / 16.0f, -0.4f
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

            fade = new VertexArray(6);
            mesh = new VertexArray(vertices, indices, tcs);
            bgTexture = new Texture("res/bg.jpeg");
            bird = new Bird();

            createPipes();
      }

      private void createPipes() {

            Pipe.create();

            for (int i = 0; i < 5 * 2; i+=2) {
                  pipes[i] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
                  pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 11.5f);
                  index += 2;
            }
      }

      private void updatePipes() {
            pipes[index % 10] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
            pipes[(index + 1) % 10] = new Pipe(pipes[index % 10].getX(), pipes[index % 10].getY() - 11.5f);
            index += 2;

      }

      public void update() {

            if (playerControl) {
                  xScroll--;
                  if (-xScroll % 335 == 0) map++;

                  if (-xScroll > 250 && -xScroll % 120 == 0)
                        updatePipes();
            }

            bird.update(playerControl);

            if (playerControl && collision()) {
                  bird.fall();
                  playerControl = false;
            }
            time += 0.01f;
      }

      private void renderPipes() {
            Shader.PIPE.enable();
            Shader.PIPE.setUniform2f("bird", 0, bird.getPosition().y);
            Shader.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.05f, 0.0f, 0.0f)));
            Pipe.getTexture().bind();
            Pipe.getMesh().bind();

            for (int i = 0; i < 5 * 2; i++) {
                  Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
                  Shader.PIPE.setUniform1i("top", i % 2 == 0 ? 1 : 0);
                  Pipe.getMesh().draw();
            }
            Pipe.getMesh().unbind();
            Pipe.getTexture().unbind();
      }

      public void render() {

            Shader.BG.enable();
            Shader.BG.setUniform2f("bird", 0, bird.getPosition().y);
            bgTexture.bind();
            mesh.bind();
            for (int i = map; i < map + 4; i++) {
                  Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
                  mesh.draw();
            }
            mesh.unbind();
            Shader.BG.disable();
            bgTexture.unbind();

            renderPipes();
            bird.render();

            Shader.FADE.enable();
            Shader.FADE.setUniform1f("time", time);
            fade.render();
            Shader.FADE.disable();
      }

      private boolean collision() {
            for (int i = 0; i < 5 * 2; i++) {
                  float bx = -xScroll * 0.05f;
                  float by = bird.getPosition().y;
                  float px = pipes[i].getX();
                  float py = pipes[i].getY();

                  float bx0 = bx - bird.getSIZE() / 2.0f;
                  float bx1 = bx + bird.getSIZE() / 2.0f;
                  float by0 = by - bird.getSIZE() / 2.0f;
                  float by1 = by + bird.getSIZE() / 2.0f;

                  float px0 = px;
                  float px1 = px + Pipe.getWidth();
                  float py0 = py;
                  float py1 = py + Pipe.getHeight();

                  if (bx1 > px0 && bx0 < px1) {
                        if (by1 > py0 && by0 < py1) {
                              return true;
                        }
                  }
            }
            return false;
      }

      public boolean isGameOver() {
            return !playerControl;
      }
}
