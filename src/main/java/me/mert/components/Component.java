package me.mert.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.core.enums.PortType;
import me.mert.glyph.Glyph;

public abstract class Component implements Serializable {
    public int i, j;
    public final ComponentType type;
    public final int[] size;
    public Direction direction;
    protected transient BufferedImage img;
    public transient BufferedImage previewImage;

    public final List<Port> inputs;
    public final List<Port> outputs;

    // transient means it won't get saved

    // uh this class is total chaos
    protected Component(int i, int j, Direction direction, int[] size, ComponentType type) {
        this.i = i;
        this.j = j;
        this.outputs = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.direction = direction;
        this.img = null;
        this.size = size;
        this.type = type;

        loadImage(type);

        // i swaer im going too far just for one component
        if (this.type != ComponentType.MERGER) {
            loadPreviewImage(img);
        }
    }

    protected final Port addinput(int i, int j, Direction dir) {
        Port p = new Port(i, j, this, dir, PortType.INPUT);
        inputs.add(p);
        return p;
    }

    protected final Port addinput(Direction dir) {
        Port p = new Port(dir.getDi(), dir.getDj(), this, dir, PortType.INPUT);
        inputs.add(p);
        return p;
    }

    // but what if there is a cosmic ray and it flips one of the bits?
    protected final Port addOutput(int i, int j, Direction dir) {
        Port p = new Port(i, j, this, dir, PortType.OUTPUT);
        outputs.add(p);
        return p;
    }

    protected final Port addOutput(Direction dir) {
        Port p = new Port(dir.getDi(), dir.getDj(), this, dir, PortType.OUTPUT);
        outputs.add(p);
        return p;
    }

    // this is just so conveyor can change its port
    protected final Port addOutput(Port p) {
        outputs.add(p);
        return p;
    }

    protected final void removePort(Port p) {
        switch (p.type) {
            case INPUT -> inputs.remove(p);
            case OUTPUT -> outputs.remove(p);
        }
    }

    // classic case of polymorphism
    public BufferedImage getImage() {
        return this.img;
    }

    public abstract void update();

    public void render(Graphics g, int x, int y, double zoom, int cellSize) {
        Graphics2D g2d = (Graphics2D) g.create();

        int w = (int) (size[0] * cellSize * zoom);
        int h = (int) (size[1] * cellSize * zoom);

        BufferedImage renderImg = getImage();

        switch (direction) {
            case NORTH -> // 0 degree
                g2d.drawImage(renderImg, x, y, w, h, null);

            case EAST -> {
                // 90 degrees
                g2d.translate(x + h, y);
                g2d.rotate(Math.PI / 2);
                g2d.drawImage(renderImg, 0, 0, w, h, null);
            }

            case SOUTH -> {
                // 180 degrees
                g2d.translate(x + w, y + h);
                g2d.rotate(Math.PI);
                g2d.drawImage(renderImg, 0, 0, w, h, null);
            }

            case WEST -> {
                // 270 degree
                g2d.translate(x, y + w);
                g2d.rotate(-Math.PI / 2);
                g2d.drawImage(renderImg, 0, 0, w, h, null);
            }
        }
        g2d.dispose();
    }

    protected final BufferedImage loadImage(String p) {
        String path = "components/" + p.toLowerCase() + ".png";

        try (InputStream iStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (iStream != null) {
                return ImageIO.read(iStream);
            }
        } catch (IOException ignored) {
            System.err.print("Couldn't load " + p + ".png");
        }
        return null; // well too bad it doesn't load

    }

    public  final void loadImage(ComponentType ct) {
        String path = "components/" + ct.toString().toLowerCase() + ".png";

        try (InputStream iStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (iStream != null) {
                this.img = ImageIO.read(iStream);
            }
        } catch (IOException ignored) {
            System.err.print("Couldn't load " + ct);
            System.out.println("Loading default image");
        }
        // we do need to remember that this will be checked by my dream university
        // we have to keep our profesionalism
    }

    protected final void loadPreviewImage(BufferedImage src) {
        // random bullshit go
        BufferedImage out = new BufferedImage(
                src.getWidth(),
                src.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                int argb = src.getRGB(x, y);

                /*
                 * little explanation here
                 * there is 8 bits per channle (RGBA)
                 * so if we want to get alpha channle we shift by 24 positions
                 * so alpha bits will be the first byte and (& 0xFF) masks out everything
                 * except the first 8 bits
                 */
                int a = (argb >> 24 & 0xFF);
                // skip if transparrent
                if (a == 0)
                    continue;

                // scale alpha by 60%
                int previewA = (int) (a * 0.6f);

                // get rgb values
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                // idk, got this fomula from the internet and it works
                int luminance = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
                int green = (previewA << 24) | (luminance << 8);
                out.setRGB(x, y, green);
            }
        }
        previewImage = out;
    }

    public final void connectTo(Component other) {
        for (Port out : this.outputs) {
            int outI = out.getWorldI();
            int outJ = out.getWorldJ();

            for (Port in : other.inputs) {
                if (outI == in.getWorldI() && outJ == in.getWorldJ()) {
                    out.connectTo(out);
                }
            }
        }
    }

    public final List<Port> getAllPorts() {
        List<Port> all = new ArrayList<>(inputs.size() + outputs.size());
        all.addAll(inputs);
        all.addAll(outputs);
        return all;
    }

    // we only have to check the input ports bc each update
    // the item goes from input *update* -> output -> other.input
    public final boolean hasItem() {
        for (Port p : inputs) {
            if (p.hasItem()) {
                return true;
            }
        }
        return false;
    }

    public final Glyph getItem() {
        if (hasItem()) {
            for (Port p : inputs) {
                if (p.hasItem()) {
                    return p.getItem();
                }
            }
        }
        return null;
    }

    public final List<Port> getInputPorts() {
        return List.copyOf(inputs);
    }

    public final List<Port> getOutputPorts() {
        return List.copyOf(outputs);
    }

    @Override
    public String toString() {
        return String.format("%s(i=%d, j=%d, dir=%s)", type, i, j, direction);
    }
}
