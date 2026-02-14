package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.glyph.Glyph;

public class Stacker extends Component {
    private final Port in1, in2;
    private final Port out;

    public Stacker(int i, int j, Direction direction) {
        super(i, j, direction, new int[] { 2, 1 }, ComponentType.STACKER);
        Direction inDir = direction.opposite();
        Direction side = direction.right();

        in1 = addinput(inDir);
        in2 = addinput(
                inDir.getDi() + side.getDi(),
                inDir.getDj() + side.getDj(),
                inDir);

        out = addOutput(direction);
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
