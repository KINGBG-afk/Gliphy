package me.mert.glyph;

import me.mert.core.enums.Primitive;

public class GlyphLayer {
    public static final int QUARTERS = 4;

    // quarter pos:
    // [0] - TR
    // [1] - TL
    // [2] - BL
    // [3] - BR

    Primitive[] q = new Primitive[QUARTERS];

    public GlyphLayer() {
        for (int i = 0; i < 4; i++)
            q[i] = Primitive.EMPTY;
    }

}
