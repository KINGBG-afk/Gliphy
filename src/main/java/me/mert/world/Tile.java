package me.mert.world;

import me.mert.components.Component;

public class Tile {
    int i, j;
    Component component = null;

    Tile(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return this.component;
    }

    public boolean isEmpty() {
        return (this.component != null);
    }
}
