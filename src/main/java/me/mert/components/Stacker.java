package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.glyph.Glyph;

public class Stacker extends Component {
    private final Port in1, in2;
    private final Port out;

    public Stacker(int i, int j, Direction dir) {
        super(i, j, dir, new int[] { 2, 1 }, ComponentType.STACKER);
        int dirI = dir.getDi();
        int dirJ = dir.getDj();

        Direction inDir = dir.opposite();
        int inI = inDir.getDi();
        int inJ = inDir.getDj();

        Direction side = dir.right();

        if (dir == Direction.SOUTH) {
            inJ += 1;
            dirJ += 1;

        } else if (dir == Direction.WEST) {
            inI += 1;
            dirI += 1;
        }

        in1 = addinput(inI, inJ, inDir);
        in2 = addinput(
                inI + side.getDi(),
                inJ + side.getDj(),
                inDir);

        out = addOutput(dirI, dirJ, dir);

        in1.connectTo(out);
    }

    @Override
    public void update() {
        if (!in1.hasItem() || !in2.hasItem()) {
            return;
        }

        Glyph result = Glyph.stack(in1.getItem(), in2.getItem());

        if (out.getConnectedTo() != null && out.getConnectedTo().canAccept(result)) {
            out.getConnectedTo().nextItem = result;
        }
    }

}
