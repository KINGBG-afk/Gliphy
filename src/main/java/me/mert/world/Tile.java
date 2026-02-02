package me.mert.world;

import me.mert.components.Component;
import me.mert.core.enums.LayerType;

// ive started this as a limited grid so these positions remain as i,j
// and no i wont be changing them
public class Tile {
    private final int i, j;
    LayerType recourse;
    Component component = null;

    Tile(int i, int j, LayerType recourse) {
        this.i = i;
        this.j = j;
        this.recourse = recourse;
    }

    protected void setComponent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return this.component;
    }

    protected boolean isEmpty() {
        return (this.component == null);
    }

    public LayerType getRecourse() {
        return recourse;
    }

    @Override
    public String toString() {
        if (component != null) {
            return String.format("Tile(i=%d, j=%d, component=%s)", i, j, component.type);
        }
        return String.format("Tile(i=%d, j=%d, component=null)", i, j);
    }
}
