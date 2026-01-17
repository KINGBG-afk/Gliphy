package me.mert.glyph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import me.mert.core.enums.LayerType;
import me.mert.core.enums.Primitive;

public class Glyph {
    List<GlyphLayer> layers = new ArrayList<>();
    static final int[] QX = { 1, 0, 0, 1 };
    static final int[] QY = { 0, 0, 1, 1 };

    public Glyph(GlyphLayer l) {
        layers.add(l);
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

        Stroke old = g2d.getStroke();
        g2d.setStroke(new BasicStroke(Math.max(1, size / 8)));
        g2d.setColor(Color.BLACK);
        for (int l = 0; l < g.layers.size(); l++) {
            int ly = y - l * layerOffset;
            GlyphLayer layer = g.layers.get(l);

            renderLayer(g2d, layer, x, ly, size);
        }
        g2d.setStroke(old);
    }

    private static void renderLayer(
            Graphics2D g2d,
            GlyphLayer layer,
            int x,
            int y,
            int size) {

        if (layer.getType() == LayerType.SQUARE) {
            drawSquare(g2d, x, y, size);
            return;
        }

        if (layer.getType() == LayerType.CIRCLE) {
            drawCircle(g2d, x, y, size);
            return;
        }

        // well yeah at least i won't worry about performance for this one
        // hehe...
        int mask = (layer.q[0] == Primitive.LINE ? 1 : 0) |
                (layer.q[1] == Primitive.LINE ? 2 : 0) |
                (layer.q[2] == Primitive.LINE ? 4 : 0) |
                (layer.q[3] == Primitive.LINE ? 8 : 0);

        switch (mask) {
            case 0b0011 -> {
                drawHorizontalLine(g2d, x, y, size);
                return;
            }
            case 0b1100 -> {
                drawHorizontalLine(g2d, x, y, size);
                return;
            }
            case 0b1001 -> {
                drawVerticalLine(g2d, x, y, size);
                return;
            }
            case 0b0110 -> {
                drawVerticalLine(g2d, x, y, size);
                return;
            }
        }

        int qs = size / 2;

        for (int q = 0; q < 4; q++) {
            Primitive p = layer.q[q];

            if (p == Primitive.EMPTY)
                continue;

            int px = x + QX[q] * qs;
            int py = y + QY[q] * qs;

            switch (p) {
                case CIRCLE -> drawCircleQuarter(g2d, px, py, qs, q);
                case SQUARE -> drawSquareQuarter(g2d, px, py, qs, q);
            }
        }
    }

    // ----------------- LINE -----------------
    private static void drawHorizontalLine(Graphics2D g, int x, int y, int s) {
        g.drawLine(
                x + s / 6, y + s / 2,
                x + 5 * s / 6, y + s / 2);
    }

    private static void drawVerticalLine(Graphics2D g, int x, int y, int s) {
        g.drawLine(
                x + s / 2, y + s / 6,
                x + s / 2, y + 5 * s / 6);
    }

    // ----------------- SQUARE -----------------
    private static void drawSquare(Graphics2D g2d, int x, int y, int s) {
        g2d.drawRect(x + 1, y + 1, s - 2, s - 2);
    }

    private static void drawSquareQuarter(Graphics2D g2d, int x, int y, int s, int q) {
        int x1 = x + 1;
        int y1 = y + 1;
        int x2 = x + s - 2;
        int y2 = y + s - 2;

        switch (q) {
            case 0 -> { // TR
                g2d.drawLine(x1, y1, x2, y1);
                g2d.drawLine(x2, y1, x2, y2);
            }
            case 1 -> { // TL
                g2d.drawLine(x1, y1, x2, y1);
                g2d.drawLine(x1, y1, x1, y2);
            }
            case 2 -> { // BL
                g2d.drawLine(x1, y2, x2, y2);
                g2d.drawLine(x1, y1, x1, y2);
            }
            case 3 -> { // BR
                g2d.drawLine(x1, y2, x2, y2);
                g2d.drawLine(x2, y1, x2, y2);
            }
        }

    }

    // ----------------- CIRLCE -----------------
    private static void drawCircle(Graphics2D g2d, int x, int y, int s) {
        g2d.drawOval(x + 1, y + 1, s - 2, s - 2);
    }

    private static void drawCircleQuarter(Graphics2D g2d, int x, int y, int s, int q) {
        int startAngle = switch (q) {
            case 0 -> 0; // TR
            case 1 -> 90; // TL
            case 2 -> 180; // BL
            case 3 -> 270; // BR
            default -> 0;
        };

        g2d.drawArc(x + 1, y + 1,
                s - 2, s - 2,
                startAngle,
                90);
    }

}
