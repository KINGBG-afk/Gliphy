package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.core.enums.LayerType;
import me.mert.glyph.Glyph;

public class Collector extends Component {

    private final Port out;
    private final Port in;
    private final LayerType resource;

    public Collector(int i, int j, Direction dir, LayerType resource) {
        super(i, j, dir, new int[] { 1, 1 }, ComponentType.COLLECTOR);
        this.resource = resource;

        out = addOutput(dir);
        in = addinput(0, 0, dir);
    }

    @Override
    public void update() {
        // if emtpy create new one
        if (!out.hasItem() && out.nextItem == null) {

            Glyph glyph = new Glyph(resource);
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
