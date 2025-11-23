package me.mert.components;

import java.util.ArrayList;
import java.util.List;

import me.mert.world.Glyph;

public abstract class GameObject {
    int i, j;
    List<GameObject> inputs, outputs;
    Glyph item;

    GameObject(int i, int j) {
        this.i = i;
        this.j = j;
        this.outputs = new ArrayList<>();
        this.inputs = new ArrayList<>();
    }

    public abstract void update();

    public void connectTo(GameObject other) {
        this.outputs.add(other);
        other.inputs.add(this);
    }

    public void reveiveItem(Glyph item) {
        if (item != null) {
            this.item = item;
        }
    }
}
