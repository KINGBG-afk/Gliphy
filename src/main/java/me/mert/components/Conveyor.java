package me.mert.components;

import me.mert.world.Glyph;

public class Conveyor extends GameObject {

    Glyph bufferedItem;

    public Conveyor(int i, int j, int orientation) {
        super(i, j, orientation);
        this.bufferedItem = null;
        this.size = new int[] { 1, 1 };
        loadImage("conveyor");
    }

    @Override
    public void update() {
        if (item != null) {
            GameObject target = outputs.get(0);
            if (target.item == null) {
                bufferedItem = item;
                item = null;
            }
        }

        if (bufferedItem != null) {
            for (GameObject target : outputs) {
                if (target.item != null) {
                    target.reveiveItem(bufferedItem);
                    bufferedItem = null;
                    return;
                }
            }
        }
    }

}
