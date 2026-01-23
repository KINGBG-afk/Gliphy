package me.mert.glyph;

import me.mert.core.enums.LayerType;
import me.mert.core.enums.Primitive;

public class GlyphLayer {
    // at this point this is here to just remind me what a quarter is
    public static final int QUARTERS = 4;

    // quarter pos:
    // [0] - TR
    // [1] - TL
    // [2] - BL
    // [3] - BR

    Primitive[] q = new Primitive[QUARTERS];
    LayerType type = LayerType.CUSTOM;

    public GlyphLayer() {
        for (int i = 0; i < 4; i++)
            q[i] = Primitive.EMPTY;
    }

    public static GlyphLayer createLayer(LayerType type) {
        GlyphLayer l = new GlyphLayer();

        if (type == LayerType.LINE) {
            l.q[0] = Primitive.LINE;
            l.q[1] = Primitive.LINE;
            l.type = LayerType.LINE;
        } else {
            Primitive shape = Primitive.EMPTY;
            switch (type) {
                case SQUARE -> shape = Primitive.SQUARE;
                case CIRCLE -> shape = Primitive.CIRCLE;
            }

            // not the best thing but for now it gets the job done
            l.type = shape == Primitive.SQUARE ? LayerType.SQUARE : LayerType.CIRCLE;

            for (int i = 0; i < 4; i++) {
                l.q[i] = shape;
            }
        }
        return l;
    }

    public static GlyphLayer createLayer(Primitive... quarters) {
        if (quarters.length != 4) {
            throw new IllegalArgumentException("Need 4 quarters");
        }

        GlyphLayer l = new GlyphLayer();
        l.type = LayerType.CUSTOM;
        System.arraycopy(quarters, 0, l.q, 0, 4);
        return l;
    }


    public LayerType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("TR: %s; TL: %s; BL: %s; BR: %s", q[0], q[1], q[2], q[3]);
    }

}
