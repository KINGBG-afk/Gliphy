package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.glyph.Glyph;

public class Merger extends Component {

    private final Port in1, in2;
    private final Port out;

    private boolean turn;
    private final boolean variant;

    // variant = false = takes from left and vise versa
    public Merger(int i, int j, Direction dir, boolean variant) {
        super(i, j, dir, new int[] { 1, 1 }, ComponentType.MERGER);
        this.variant = variant;

        in1 = addinput(dir.opposite());

        if (variant) {
            in2 = addinput(dir.right());
        } else {
            in2 = addinput(dir.left());
        }

        out = addOutput(dir);
        this.img = loadImage(variant ? "merger-right" : "merger-left");
        loadPreviewImage(img);
    }

    public boolean isVariant() {
        return variant;
    }

    @Override
    public void update() {
        if (turn) {
            if (!in1.hasItem()) {
                turn = !turn;
                return;
            }

            Glyph g = in1.getItem();
            if (out.getConnectedTo() != null && out.getConnectedTo().canAccept(g)) {
                in1.wantsToEject = true;
                out.getConnectedTo().nextItem = g;
            }
        } else {
            if (!in2.hasItem()) {
                turn = !turn;
                return;
            }
            Glyph g = in2.getItem();
            if (out.getConnectedTo() != null && out.getConnectedTo().canAccept(g)) {
                in2.wantsToEject = true;
                out.getConnectedTo().nextItem = g;
            }

        }
    }

}
