package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.glyph.Glyph;

public class Cutter extends Component {
    public Port in;
    public Port out1, out2;

    public Cutter(int i, int j, Direction direction) {
        super(i, j, direction, new int[] { 2, 1 }, ComponentType.CUTTER);
        Direction inDir = direction.opposite();
        Direction side = direction.right();

        in = addinput(inDir.getDi(), inDir.getDj(), inDir);

        out1 = addOutput(direction.getDi(), direction.getDj(), direction);
        out2 = addOutput(
                direction.getDi() + side.getDi(),
                direction.getDj() + side.getDj(),
                direction);

        // eh it gets the job done
        in.connectTo(out1);
        // just for consistency
    }

    @Override
    public void update() {
        if (!in.hasItem())
            return;

        Glyph g = in.getItem();
        Glyph[] glyphs = Glyph.cut(g);

        if (out1.getConnectedTo() != null && out1.getConnectedTo().canAccept(glyphs[0])) {
            in.wantsToEject = true;
            out1.getConnectedTo().nextItem = glyphs[0];
        }

        if (out2.getConnectedTo() != null && out2.getConnectedTo().canAccept(glyphs[1])) {
            in.wantsToEject = true;
            out2.getConnectedTo().nextItem = glyphs[1];
        }

    }

}
