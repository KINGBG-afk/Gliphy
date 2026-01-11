package me.mert.glyph;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import me.mert.core.enums.Primitive;

public class Glyph {
    List<GlyphLayer> layers;
    static final int[] QX = { 1, 0, 0, 1 };
    static final int[] QY = { 0, 0, 1, 1 };

    public Glyph(Primitive shape) {
        layers = new ArrayList<>();
        fillPrimitives(shape);
    }

    private void fillPrimitives(Primitive shape) {
        GlyphLayer layer = new GlyphLayer();

        layer.q[0] = shape;
        layer.q[1] = shape;
        layer.q[2] = shape;
        layer.q[3] = shape;

        layers.add(layer);
    }

    public static void render(
            Graphics2D g2d,
            Glyph g,
            int x,
            int y,
            int size) {
        if (g == null) {
            return;
        }

        int layerOffset = size / 6;
        

        for (int l = 0; l < g.layers.size(); l++) {
            int ly = y - l * layerOffset;
            GlyphLayer layer = g.layers.get(l);

            renderLayer(g2d, layer, x, ly, size);
        }

    }

    private static boolean isUniform(GlyphLayer layer, Primitive p) {
        for (int q = 0; q < 4; q++) {
            if (layer.q[q] != p)
                return false;

        }
        return true;
    }

    private static void renderLayer(
            Graphics2D g2d,
            GlyphLayer layer,
            int x,
            int y,
            int size) {

        if (isUniform(layer, Primitive.SQUARE)) {
            drawSquare(g2d, x, y, size);
            return;
        }

        if (isUniform(layer, Primitive.CIRCLE)) {
            drawCircle(g2d, x, y, size);
            return;
        }

        int qs = size / 2;

        for (int q = 0; q < 4; q++) {
            Primitive p = layer.q[q];

            if (p == Primitive.EMPTY)
                continue;

            int px = x + QX[q] * qs;
            int py = y + QY[q] * qs;

            renderPrimitive(g2d, p, px, py, qs);
        }
    }

    // tbh idk if this is good to do (idk what else)
    @SuppressWarnings("incomplete-switch")
    private static void renderPrimitive(
            Graphics2D g2d,
            Primitive p,
            int x,
            int y,
            int size) {

        switch (p) {
            case SQUARE -> drawSquare(g2d, x, y, size);
            case CIRCLE -> drawCircle(g2d, x, y, size);
            case LINE -> drawLine(g2d, x, y, size);
        }

    }

    private static void drawSquare(Graphics2D g2d, int x, int y, int s) {
        g2d.drawRect(x + 1, y + 1, s - 2, s - 2);
    }

    private static void drawCircle(Graphics2D g2d, int x, int y, int s) {
        g2d.drawOval(x + 1, y + 1, s - 2, s - 2);
    }

    private static void drawLine(Graphics2D g, int x, int y, int s) {
        Stroke old = g.getStroke();
        g.setStroke(new BasicStroke(Math.max(2, s / 6)));

        g.drawLine(
                x + s / 4, y + s / 4,
                x + 3 * s / 4, y + 3 * s / 4);

        g.setStroke(old);
    }

}
