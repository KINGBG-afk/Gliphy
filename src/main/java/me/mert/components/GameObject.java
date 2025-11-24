package me.mert.components;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import me.mert.world.Glyph;

public abstract class GameObject {
    int i, j;
    List<GameObject> inputs, outputs;
    Glyph item;
    int[] size;
    int orientation; // 0 = north; 1 = east; 2 = south; 3 = west
    BufferedImage img;

    GameObject(int i, int j, int orientation) {
        this.i = i;
        this.j = j;
        this.outputs = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.orientation = orientation;
        this.img = null;
    }

    public abstract void update();

    protected void loadImage(String component) {
        String path = "component/" + component + ".png";

        try (InputStream iStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (iStream != null) {
                this.img = ImageIO.read(iStream);
            }
        } catch (IOException ignored) {
            System.err.print("Couldn't load " + component);
            System.out.println("Loading default image");
        }

        try (InputStream fallback = getClass().getClassLoader().getResourceAsStream("component/default.png")) {
            this.img = ImageIO.read(fallback);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load fallback image", e);
        }

    }

    public void connectTo(GameObject other) {
        this.outputs.add(other);
        other.inputs.add(this);
    }

    public void reveiveItem(Glyph item) {
        if (item != null) {
            this.item = item;
        }
    }
}
