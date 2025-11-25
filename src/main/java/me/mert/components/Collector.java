package me.mert.components;

import me.mert.world.Glyph;

public class Collector extends GameObject {

    public Collector(int i, int j, int orientation) {
        super(i, j, orientation, new int[] { 1, 1 }, "collector");
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
