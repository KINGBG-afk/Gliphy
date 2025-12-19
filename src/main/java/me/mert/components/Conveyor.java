package me.mert.components;

import me.mert.core.ComponentType;
import me.mert.core.Direction;
import me.mert.world.Glyph;

public class Conveyor extends Component {

    public final Port in;
    public final Port out;

    public Conveyor(int i, int j, Direction dir) {
        super(i, j, dir, new int[] { 1, 1 }, ComponentType.CONVEYOR);
        loadImage("conveyor");

        in = addinput(dir.opposite().getDi(), dir.opposite().getDj(), dir.opposite());
        out = addOutput(dir.getDi(), dir.getDj(), dir);

        in.connectTo(out);
    }

    @Override
    public void update() {
        if (!in.hasItem())
            return;

        Glyph g = in.getItem();

        if (out.getConnectedTo() != null && out.getConnectedTo().canAccept(g)) {
            in.wantsToEject = true;
            out.getConnectedTo().nextItem = g;
        }
    }
}
