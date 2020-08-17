package com.bartskys.statki.model;

import com.bartskys.statki.math.Vector3f;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

public class Particle {
    @Getter@Setter
    private int lifespan;
    @Getter
    private Vector3f position;
    @Getter
    private RenderBox renderBox;
    @Getter@Setter
    private float speed;
    @Getter@Setter
    private float size;
    @Getter@Setter
    private boolean alive = true;

    public Particle() {
       lifespan = new Random().nextInt(10);
       position = new Vector3f();
       size = 0.0f;
       renderBox = new RenderBox(
                "Particle",
                "res/shot.png",
                position,
                size,
                size
       );
    }

    public Particle(int lifespan, Vector3f position, float speed, float size) {
        this.lifespan = lifespan;
        this.position = position;
        this.speed = speed;
        this.size = size;
        renderBox = new RenderBox(
                "Particle",
                "res/boom.png",
                position,
                size,
                size
        );
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
        if(this.lifespan <= 25) alive = false;
    }

    public void setPosition(Vector3f newPosition) {
        position = newPosition;
        renderBox.setPosition(newPosition);
    }
}
