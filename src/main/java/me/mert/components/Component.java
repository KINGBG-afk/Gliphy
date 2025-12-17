package me.mert.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import me.mert.core.ComponentType;
import me.mert.core.Direction;
import me.mert.world.Glyph;

public abstract class Component {
    public int i, j;
    public Glyph item;
    public final ComponentType type;
    public final int[] size;
    public Direction direction;
    public BufferedImage img;

    public final List<Port> inputs;
    public final List<Port> outputs;

    protected Component(int i, int j, Direction direction, int[] size, ComponentType type) {
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

    public void render(Graphics g, int x, int y, double zoom, int cellSize) {
        Graphics2D g2d = (Graphics2D) g.create();

        int width = (int) (size[0] * cellSize * zoom);
        int height = (int) (size[1] * cellSize * zoom);

        switch (direction) {
            case NORTH -> // 0 degree
                g2d.drawImage(img, x, y, width, height, null);

            case EAST -> {
                // 90 degrees
                System.out.println("render east");
                g2d.translate(x + width, y);
                g2d.rotate(Math.PI / 2);
                g2d.drawImage(img, 0, 0, width, height, null);
            }

            case SOUTH -> {
                // 180 degrees
                g2d.translate(x + width, y + height);
                g2d.rotate(Math.PI);
                g2d.drawImage(img, 0, 0, width, height, null);
            }

            case WEST -> {
                // 270 degree
                g2d.translate(x, y + height);
                g2d.rotate(Math.PI / 2);
                g2d.drawImage(img, 0, 0, width, height, null);
            }
        }

        g2d.dispose();
    }

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
        return String.format("%s(i=%d, j=%d, dir=%s)", type, i, j, direction);
    }
}
