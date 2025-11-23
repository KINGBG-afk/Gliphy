package me.mert.components;

import me.mert.world.Glyph;

public class Collector extends GameObject {

    Collector(int i, int j) {
        super(i, j);
    }

    @Override
    public void update() {
        item = new Glyph(1);
        for (GameObject gameObject : inputs) {
            if (gameObject.item == null) {
                gameObject.item = item;
            }
        }

    }

}
