package me.mert.components;

import me.mert.world.Glyph;

public interface ItemEjector {

    boolean canEject();

    Glyph eject();
}