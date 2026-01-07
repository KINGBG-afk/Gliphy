package me.mert.components;

import me.mert.world.Glyph;

public interface ItemAcceptor {
    boolean canAccept(Glyph g);

    void accept(Glyph g);
}
