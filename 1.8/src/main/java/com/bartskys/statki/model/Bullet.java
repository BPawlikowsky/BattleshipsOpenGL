package com.bartskys.statki.model;

import com.bartskys.statki.math.Vector3f;
import lombok.Getter;
import lombok.Setter;

public class Bullet {
    @Getter
    private String name;
    @Getter @Setter
    private Vector3f from, to;
    @Getter @Setter
    private boolean animation;
    @Getter
    private RenderBox bullet;
    @Setter @Getter
    private float speed;
    @Getter@Setter
    private float size;

    public Bullet() {
        name = "bullet";
        from = to = new Vector3f();
        animation = false;
        bullet = new RenderBox();
    }

    public Bullet(Vector3f from, Vector3f to, boolean animation, int num, RenderBox b) {
        name = "bullet" + num;
        this.from = from;
        this.to = to;
        this.animation = animation;
        bullet = b;
    }

    public void updatePos(float speed) {
        from = new Vector3f(
                from.x + (to.x - from.x) * speed,
                from.y + (to.y - from.y) * speed,
                0.0f
        );
        bullet.setPosition(from);
    }
}
