package com.bartskys.statki.model;

import com.bartskys.statki.math.Vector3f;
import lombok.Getter;

import java.util.ArrayList;

public class Sign {
    @Getter
    ArrayList<RenderBox> sign;

    public Sign(String text, float posX, float posY) {
        float offset = 0.0f;
        sign = new ArrayList<>();
        for(char c : text.toCharArray()) {
            if(c != ' ')
                sign.add(new RenderBox(
                   String.valueOf(c),
                        "res/letters/" + String.valueOf(c) + ".png",
                        new Vector3f(
                                posX + offset,
                                posY,
                                0.0f
                        ),
                        0.24f,
                        0.24f
                ));
            else sign.add(new RenderBox(
                    String.valueOf(c),
                    "res/transp.png",
                    new Vector3f(
                            posX + offset,
                            posY,
                            0.0f
                    ),
                    0.24f,
                    0.24f
            ));
            offset += 0.49f;
        }

    }
}
