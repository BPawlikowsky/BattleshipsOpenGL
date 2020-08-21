package com.bartskys.statki.model;

import com.bartskys.statki.graphics.Texture;
import com.bartskys.statki.graphics.VertexArray;
import com.bartskys.statki.math.Vector3f;
import lombok.Getter;
import lombok.Setter;

public class RenderBox {
    @Getter @Setter
    private float sizeX;
    @Getter @Setter
    private float sizeY;
    @Getter @Setter
    private Vector3f position;
    @Getter
    private final String name;
    @Getter
    private final Texture texture;
    @Getter
    private VertexArray mesh;

    public RenderBox() {

        name = "Empty";
        texture = new Texture("res/transp.png");
        position = new Vector3f();
        sizeX = 0.0f;
        sizeY = 0.0f;
    }

    public RenderBox(String name, String path, Vector3f position, float sizeX, float sizeY) {

        this.position = position;
        this.name = name;

        float[] vertices = {
                -sizeX,  sizeY, 0.0f,
                -sizeX, -sizeY, 0.0f,
                sizeX, -sizeY, 0.0f,
                sizeX,  sizeY, 0.0f
        };

        byte[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = {
                // right order to view with the above combo of vertices and indices
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };

        mesh = new VertexArray(vertices, indices, tcs);
        texture = new Texture(path);
    }

    public void updateSize() {
        float[] vertices = {
                -sizeX,  sizeY, 0.0f,
                -sizeX, -sizeY, 0.0f,
                sizeX, -sizeY, 0.0f,
                sizeX,  sizeY, 0.0f
        };

        byte[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = {
                // right order to view with the above combo of vertices and indices
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };

        mesh = new VertexArray(vertices, indices, tcs);
    }
}
