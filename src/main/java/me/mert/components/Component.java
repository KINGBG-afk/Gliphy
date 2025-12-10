package me.mert.components;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import me.mert.core.Direction;
import me.mert.world.Glyph;

public abstract class Component {
    public int i, j;
    public Glyph item;
    public final String type;
    public final int[] size;
    public Direction direction;
    public BufferedImage img;

    public final List<Port> inputs;
    public final List<Port> outputs;

    protected Component(int i, int j, Direction direction, int[] size, String type) {
        this.i = i;
        this.j = j;
        this.outputs = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.direction = direction;
        this.img = null;
        this.size = size;
        this.type = type;
    }

    protected Port addinput(int i, int j, Direction dir) {
        Port p = new Port(i, j, this, dir);
        inputs.add(p);
        return p;
    }

    protected Port addOutput(int i, int j, Direction dir) {
        Port p = new Port(i, j, this, dir);
        outputs.add(p);
        return p;
    }

    public abstract void update();

    protected void loadImage(String component) {
        String path = "components/" + component + ".png";

        try (InputStream iStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (iStream != null) {
                this.img = ImageIO.read(iStream);
            }
        } catch (IOException ignored) {
            System.err.print("Couldn't load " + component);
            System.out.println("Loading default image");
        }

        if (this.img != null)
            return;
        try (InputStream fallback = getClass().getClassLoader().getResourceAsStream("components/default.png")) {
            this.img = ImageIO.read(fallback);
        } catch (IOException e) {
            this.img = null;
        }

    }

    public List<Port> getPorts() {
        List<Port> all = new ArrayList<>(inputs.size() + outputs.size());
        all.addAll(inputs);
        all.addAll(outputs);
        return all;
    }

    // we only have to check the input ports bc each update
    // the item goes from input *update* -> output -> other.input
    public boolean hasItem() {
        for (Port p : inputs) {
            if (p.hasItem()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s(i=%d, j=%d, orientation=%s)", getClass().getName(), i, j, direction);
    }
}
