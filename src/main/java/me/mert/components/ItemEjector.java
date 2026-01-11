package me.mert.components;

import me.mert.glyph.Glyph;

public interface ItemEjector {

    boolean canEject();

    Glyph eject();
}