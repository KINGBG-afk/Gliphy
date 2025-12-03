package me.mert.components;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import me.mert.world.Glyph;

public abstract class GameObject {
    public int i, j;
    List<GameObject> inputs, outputs;
    public Glyph item;
    public final String type;
    public final int[] size;
    public int orientation; // 0 = north; 1 = east; 2 = south; 3 = west
    public BufferedImage img;

    protected GameObject(int i, int j, int orientation, int[] size, String type) {
        this.i = i;
        this.j = j;
        this.outputs = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.orientation = orientation;
        this.img = null;
        this.size = size;
        this.type = type;
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

    public void connectTo(GameObject other) {
        this.outputs.add(other);
        other.inputs.add(this);
    }

    public void receiveItem(Glyph item) {
        if (item != null) {
            this.item = item;
        }
    }

    @Override
    public String toString() {
        return String.format("%s(i=%d, j=%d, orientation=%d)", getClass().getName(), i, j, orientation);
    }
}
