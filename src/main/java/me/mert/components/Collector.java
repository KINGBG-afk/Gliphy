package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.core.enums.LayerType;
import me.mert.glyph.Glyph;
import me.mert.glyph.GlyphLayer;

public class Collector extends Component {

    private final Port out;
    private final Port in;

    public Collector(int i, int j, Direction dir) {
        super(i, j, dir, new int[] { 1, 1 }, ComponentType.COLLECTOR);

        out = addOutput(dir);
        in = addinput(0, 0, dir);
    }

    @Override
    public void update() {
        // if emtpy create new one
        if (!out.hasItem() && out.nextItem == null) {

            GlyphLayer l = GlyphLayer.createLayer(LayerType.SQUARE);

            // GlyphLayer l = GlyphLayer.createLayer(Primitive.SQUARE, Primitive.CIRCLE,
            // Primitive.SQUARE,Primitive.SQUARE);

            Glyph glyph = new Glyph(l);
            Glyph.rotateCCW(glyph);
            out.nextItem = glyph;

            // quick and dirty to display the glyph
            if (!in.hasItem()) {
                in.accept(glyph);
            }
        }

        if (out.hasItem() && out.getConnectedTo() != null) {
            Port target = out.getConnectedTo();

            if (!target.hasItem() && target.nextItem == null) {
                out.wantsToEject = true;
                target.nextItem = out.getItem();
            }
        }

    }

}
