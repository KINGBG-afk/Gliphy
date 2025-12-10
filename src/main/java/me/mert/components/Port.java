package me.mert.components;

import me.mert.core.Direction;
import me.mert.world.Glyph;

// Port uses 2 phase update so items won't travel the whole path in 1 frame
public class Port implements itemAcceptor, itemEjector {

    // this is where each port is physically located based on the compoent position
    public int localI, localJ;

    private Glyph item;
    private final Component owner;
    private final Direction direction;
    public Port connectedTo;

    public Glyph nextItem = null; // items ready to get accepted in here
    public boolean wantsToEject = false;

    public Port(int i, int j, Component owner, Direction direction) {
        this.owner = owner;
        this.direction = direction;
        this.localI = i;
        this.localJ = j;
    }

    public void commitMovement() {
        if (wantsToEject) {
            item = null;
            wantsToEject = false;
        }

        if (nextItem != null) {
            item = nextItem;
            nextItem = null;
        }
    }

    @Override
    public boolean canEject() {
        return item != null && connectedTo != null;
    }

    @Override
    public Glyph eject() {
        if (!canEject())
            return null;

        Glyph tmp = item;
        item = null;
        return tmp;
    }

    @Override
    public boolean canAccept(Glyph g) {
        return item == null && g != null;
    }

    @Override
    public void accept(Glyph g) {
        if (canAccept(g))
            this.item = g;
    }

    // helpers
    public boolean hasItem() {
        return item != null;
    }

    public Glyph getItem() {
        return item;
    }

    public Component getComponent() {
        return owner;
    }

    public Direction getDirection() {
        return direction;
    }
}
