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

        in = addinput(dir.getOpposite().getDi(), dir.getOpposite().getDj(), dir.getOpposite());
        out = addOutput(dir.getDi(), dir.getDj(), dir);

        in.connectedTo = out;
    }

    @Override
    public void update() {
        if (!in.hasItem())
            return;

        Glyph g = in.getItem();

        if (out.connectedTo != null && out.connectedTo.canAccept(g)) {
            in.wantsToEject = true;
            out.connectedTo.nextItem = g;
        }
    }
}
