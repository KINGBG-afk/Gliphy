package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.glyph.Glyph;

public class Splitter extends Component {

    private final Port out1, out2;
    private final Port in;;

    private boolean turn;

    private final boolean variant;

    public Splitter(int i, int j, Direction dir, boolean variant) {
        super(i, j, dir, new int[] { 1, 1 }, ComponentType.SPLITTER);
        this.variant = variant;
        out1 = addOutput(dir);

        if (variant) {
            out2 = addOutput(dir.right());
        } else {
            out2 = addOutput(dir.left());
        }

        in = addinput(dir.opposite());
        
        this.img = loadImage(variant ? "splitter-right" : "splitter-left");
        loadPreviewImage(img);

    }

    public boolean isVariant() {
        return variant;
    }

    @Override
    public void update() {
        if (!in.hasItem()) {
            turn = !turn;
            return;
        }

        if (turn) {
            Glyph g = in.getItem();
            if (out1.getConnectedTo() != null && out1.getConnectedTo().canAccept(g)) {
                in.wantsToEject = true;
                out1.getConnectedTo().nextItem = g;
            }
        } else {
            Glyph g = in.getItem();
            if (out2.getConnectedTo() != null && out2.getConnectedTo().canAccept(g)) {
                in.wantsToEject = true;
                out2.getConnectedTo().nextItem = g;
            }
        }
    }

}
