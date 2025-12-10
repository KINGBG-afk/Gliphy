package me.mert.components;

import me.mert.world.Glyph;

public interface itemEjector {

    boolean canEject();
    Glyph eject();
}