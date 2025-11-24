package me.mert.components;

import me.mert.world.Glyph;

public class Collector extends GameObject {

    Collector(int i, int j, int orientation) {
        super(i, j, orientation);
        this.size = new int[] { 1, 1 };
        loadImage("collector");
    }

    @Override
    public void update() {
        item = new Glyph(1);
        for (GameObject gameObject : inputs) {
            if (gameObject.item == null) {
                gameObject.reveiveItem(item);
            }
        }

    }

}
