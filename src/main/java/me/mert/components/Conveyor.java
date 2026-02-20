package me.mert.components;

import java.awt.image.BufferedImage;
import java.io.IOException;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.core.enums.PortType;
import me.mert.glyph.Glyph;

public class Conveyor extends Component {
    // frames do not match with the turns:/
    public static final int FRAME_COUNT = 11;
    public static final int FRAME_TICK = 6;

    private final Port in;
    private Port out; // well no you are not final

    private static final BufferedImage[] straightFrames;
    private static final BufferedImage[] leftFrames;
    private static final BufferedImage[] rightFrames;

    private static int globalFrame = 0;
    private static int tickCounter = 0;

    static {
        straightFrames = new BufferedImage[FRAME_COUNT];
        leftFrames = new BufferedImage[FRAME_COUNT];
        rightFrames = new BufferedImage[FRAME_COUNT];

        for (int i = 0; i < FRAME_COUNT; i++) {
            String idx = String.format("%02d", i);

            straightFrames[i] = loadStatic(
                    "/components/conveyor/forward_" + idx + ".png");

            leftFrames[i] = loadStatic(
                    "/components/conveyor/left_" + idx + ".png");

            rightFrames[i] = loadStatic(
                    "/components/conveyor/right_" + idx + ".png");
        }
    }

    public Conveyor(int i, int j, Direction dir) {
        super(i, j, dir, new int[] { 1, 1 }, ComponentType.CONVEYOR);

        in = addinput(dir.opposite());
        out = addOutput(dir);

        in.connectTo(out);
    }

    private static BufferedImage loadStatic(String path) {
        try {
            return javax.imageio.ImageIO.read(
                    Conveyor.class.getResourceAsStream(path));

        } catch (IOException e) {
            throw new RuntimeException("Failed to load: " + path, e);
        }
    }

    public static void updateAnimation() {
        tickCounter++;

        if (tickCounter >= FRAME_TICK) {
            tickCounter = 0;
            globalFrame = (globalFrame + 1) % FRAME_COUNT;
        }
    }

    public boolean isStraight() {
        return in.getDirection() == out.getDirection().opposite();
    }

    private boolean checkValidOutputPort(Port np) {
        if (in.getWorldI() == np.getWorldI() &&
                in.getWorldJ() == np.getWorldJ()) {
            return false;
        }

        Direction inDir = in.getDirection();
        Direction npDir = np.getDirection();

        return (npDir == inDir.left()
                || npDir == inDir.right()) && npDir != inDir;
    }

    public boolean changeOutputPort(int i, int j, Direction direction) {
        Port p = new Port(direction.getDi(), direction.getDj(), this, direction, PortType.OUTPUT);

        if (!checkValidOutputPort(p))
            return false;

        System.out.println("Removing port: " + out);
        removePort(out);
        out = addOutput(p);
        System.out.println("Adding new port: " + p);
        return true;
    }

    @Override
    public BufferedImage getImage() {

        Direction inDir = in.getDirection();
        Direction outDir = out.getDirection();

        if (inDir == outDir.left())
            return leftFrames[globalFrame];

        if (inDir == outDir.right())
            return rightFrames[globalFrame];

        return straightFrames[globalFrame];
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
