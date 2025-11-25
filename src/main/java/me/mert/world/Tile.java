package me.mert.world;

import me.mert.components.GameObject;

public class Tile {
    int i,j;
    GameObject component = null;

    Tile(int i, int j) {
        this. i = i;
        this. j = j;
    }

    public void setComponent(GameObject component) {
        this.component = component;
    }

    public GameObject getComponent() {
        return this.component;
    }

    public boolean isEmpty() {
        return (this.component != null) ? true : false;
    }
}
