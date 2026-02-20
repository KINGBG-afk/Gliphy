package me.mert.components;

import java.io.Serializable;

import me.mert.core.enums.Direction;
import me.mert.core.enums.PortType;
import me.mert.glyph.Glyph;

// Port uses 2 phase update so items won't travel the whole path in 1 frame
public class Port implements ItemAcceptor, ItemEjector, Serializable {

    // this is where each port is physically located based on the compoent position
    public int localI, localJ;

    public PortType type;

    private Glyph item;
    private final Component owner;
    private final Direction direction;
    private Port connectedTo;

    public Glyph nextItem = null; // items ready to get accepted in here
    public boolean wantsToEject = false;

    public Port(int localI, int localJ, Component owner, Direction direction, PortType type) {
        this.owner = owner;
        this.direction = direction;
        this.type = type;
        this.localI = localI;
        this.localJ = localJ;
    }

    public Component getOwner() {
        return owner;
    }

    public int getWorldI() {
        return owner.i + localI;
    }

    public int getWorldJ() {
        return owner.j + localJ;
    }

    public Port getConnectedTo() {
        return connectedTo;
    }

    public void connectTo(Port other) {
        connectedTo = other;
    }

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
    public Glyph eject() {
        if (item == null && connectedTo == null)
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

    @Override
    public String toString() {
        return String.format("Port(i=%d, j=%d, dir=%s, type=%s)", getWorldI(), getWorldJ(), direction, type);
    }
}
