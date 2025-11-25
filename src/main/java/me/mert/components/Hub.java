package me.mert.components;

public class Hub extends GameObject {
    int stored;
     public Hub(int i, int j, int orientation) {
        super(i, j, orientation, new int[] { 3, 3 }, "hub");
        this.stored = 0;
        loadImage("hub");
    }

    public void update() {
        // ig do nothing
    }
}
