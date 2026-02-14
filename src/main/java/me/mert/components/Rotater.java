package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.glyph.Glyph;

public class Rotater extends Component {

    private final Port in;
    private final Port out;

    public Rotater(int i, int j, Direction dir) {
        super(i, j, dir, new int[] { 1, 1 }, ComponentType.ROTATER);
        in = addinput(dir.opposite());
        out = addOutput(dir);

    }

    @Override
    public void update() {

        if (!in.hasItem()) {
            return;
        }

        Glyph g = in.getItem();
        Glyph.rotateCCW(g);
        if (out.getConnectedTo() != null && out.getConnectedTo().canAccept(g)) {
            in.wantsToEject = true;
            out.getConnectedTo().nextItem = g;
        }
    }

}
