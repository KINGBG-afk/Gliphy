package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.core.enums.Primitive;
import me.mert.glyph.Glyph;

public class Collector extends Component {

    public final Port out;

    public Collector(int i, int j, Direction dir) {
        super(i, j, dir, new int[] { 1, 1 }, ComponentType.COLLECTOR);

        out = addOutput(dir.getDi(), dir.getDj(), dir);
    }

    @Override
    public void update() {
        // if emtpy create new one
        if (!out.hasItem() && out.nextItem == null) {
            Glyph glyph = new Glyph(Primitive.LINE);
            out.nextItem = glyph;
        }

        if (out.hasItem() && out.getConnectedTo() != null) {
            Port target = out.getConnectedTo();

            // we skip "out" having an item at all
            if (!target.hasItem() && target.nextItem == null) {
                out.wantsToEject = true;
                target.nextItem = out.getItem();
            }
        }

    }

}
