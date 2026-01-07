package me.mert.components;

import java.awt.image.BufferedImage;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.core.enums.PortType;
import me.mert.world.Glyph;

public class Conveyor extends Component {

    public final Port in;
    public Port out; // well no you are not final

    public final BufferedImage leftImg;
    public final BufferedImage rightImg;

    public BufferedImage usedImg;

    public Conveyor(int i, int j, Direction dir) {
        super(i, j, dir, new int[] { 1, 1 }, ComponentType.CONVEYOR);

        in = addinput(dir.opposite().getDi(), dir.opposite().getDj(), dir.opposite());
        out = addOutput(dir.getDi(), dir.getDj(), dir);

        in.connectTo(out);

        leftImg = loadImage("conveyor-left");
        rightImg = loadImage("conveyor-right");

        // currenctly used image
        usedImg = this.img;
    }

    public boolean isStraight() {
        return in.getDirection() == out.getDirection().opposite();
    }

    public boolean isCorner() {
        return !isStraight();
    }

    // kinda messy but gets the job done
    private boolean checkValidOutputPort(Port np) {
        if (in.getWorldI() == np.getWorldI() &&
                in.getWorldJ() == np.getWorldJ()) {
            return false;
        }

        Direction inDir = in.getDirection();
        Direction npDir = np.getDirection();

        return npDir == inDir.left()
                || npDir == inDir.right()
                || npDir == inDir.opposite();
    }

    private void changeUsedImage() {
        Direction inDir = in.getDirection();
        Direction outDir = out.getDirection();

        if (inDir == outDir.left()) {
            usedImg = leftImg;
        } else if (inDir == outDir.right()) {
            usedImg = rightImg;
        } else if (inDir == outDir.opposite()) {
            usedImg = img;
        }

    }

    public boolean  changeOutputPort(int i, int j, Direction direction) {
        Port p = new Port(direction.getDi(), direction.getDj(), this, direction, PortType.OUTPUT);

        if (!checkValidOutputPort(p))
            return false;

        removePort(out);
        out = addOutput(p);
        changeUsedImage();
        return true;
    }

    @Override
    public BufferedImage getImage() {
        return usedImg;
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
