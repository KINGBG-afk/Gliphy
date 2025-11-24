package me.mert.components;

public class Hub extends GameObject {
    int stored;
    Hub(int i, int j, int orientation) {
        super(i, j, orientation);
        this.size = new int[] { 3, 3 };
        this.stored = 0;
        loadImage("hub");
    }

    public void update() {
        // ig do nothing
    }
}
