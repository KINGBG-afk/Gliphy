package me.mert.glyph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import me.mert.core.enums.LayerType;
import me.mert.core.enums.Primitive;

public class Glyph {
    public List<GlyphLayer> layers = new ArrayList<>();
    static final int[] QX = { 1, 0, 0, 1 };
    static final int[] QY = { 0, 0, 1, 1 };

    public Glyph(GlyphLayer l) {
        if (l == null)
            return; // layers will be added later
        layers.add(l);
    }

    public Glyph(LayerType l) {
        if (l == null) {
            return;
        }
        layers.add(GlyphLayer.createLayer(l));
    }

    private Glyph copy() {
        Glyph copy = new Glyph((GlyphLayer) null);

        for (GlyphLayer l : this.layers) {
            GlyphLayer nl = new GlyphLayer();
            System.arraycopy(l.q, 0, nl.q, 0, GlyphLayer.QUARTERS);

            copy.layers.add(nl);
        }
        return copy;

    }

    // an idiot admires complexity
    // a genius admires simplicity

    // rotating
    // #region
    private static void rotateLayerCW(GlyphLayer l) {
        Primitive[] q = l.q;

        Primitive tmp = q[1];
        q[1] = q[2];
        q[2] = q[3];
        q[3] = q[0];
        q[0] = tmp;
    }

    private static void rotateLayerCCW(GlyphLayer l) {
        Primitive[] q = l.q;

        Primitive tmp = q[1];
        q[1] = q[0];
        q[0] = q[3];
        q[3] = q[2];
        q[2] = tmp;
    }

    public static void rotateCW(Glyph g) {
        for (GlyphLayer l : g.layers) {
            rotateLayerCW(l);
        }
    }

    public static void rotateCCW(Glyph g) {
        for (GlyphLayer l : g.layers) {
            rotateLayerCCW(l);
        }
    }
    // #endregion

    // stacking
    /**
     * This method accepts 2 glyphs and stacks g2 on top of g1
     * 
     * @param g1 first glyph as base
     * @param g2 the glyph that is added on top of g1
     */
    public static Glyph stack(Glyph g1, Glyph g2) {

        Glyph base = g1.copy();
        Glyph added = g2.copy();

        if (added.layers.isEmpty())
            return base;

        GlyphLayer bottomG2 = added.layers.get(0);

        if (base.layers.isEmpty()) {
            base.layers.add(new GlyphLayer());
        }

        GlyphLayer topG1 = base.layers.get(base.layers.size() - 1);

        for (int i = 0; i < GlyphLayer.QUARTERS; i++) {

            Primitive p = bottomG2.q[i];

            if (p != Primitive.EMPTY) {

                if (topG1.q[i] == Primitive.EMPTY) {
                    topG1.q[i] = p;
                    bottomG2.q[i] = Primitive.EMPTY;
                }
            }
        }

        return base;
    }

    public static Glyph[] cut(Glyph g) {
        Glyph left = new Glyph((GlyphLayer) null);
        Glyph right = new Glyph((GlyphLayer) null);

        for (GlyphLayer l : g.layers) {
            left.layers.add(GlyphLayer.createLayer(Primitive.EMPTY, l.q[1], l.q[2], Primitive.EMPTY));
            right.layers.add(GlyphLayer.createLayer(l.q[0], Primitive.EMPTY, Primitive.EMPTY, l.q[3]));
        }
        return new Glyph[] { left, right };
    }

    // render
    // #region
    public static void render(
            Graphics2D g2d,
            Glyph g,
            int x,
            int y,
            int size) {
        if (g == null) {
            return;
        }
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(
                RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);

        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        // give illusion for depth
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

        int mask = (layer.q[0] == Primitive.LINE ? 1 : 0) |
                (layer.q[1] == Primitive.LINE ? 2 : 0) |
                (layer.q[2] == Primitive.LINE ? 4 : 0) |
                (layer.q[3] == Primitive.LINE ? 8 : 0);

        switch (mask) {
            case 0b0011 -> {
                drawHorizontalLine(g2d, x, y, size);
                if (layer.q[2] == Primitive.EMPTY && layer.q[3] == Primitive.EMPTY)
                    return;
            }
            case 0b1100 -> {
                drawHorizontalLine(g2d, x, y, size);
                if (layer.q[0] == Primitive.EMPTY && layer.q[1] == Primitive.EMPTY)
                    return;
            }
            case 0b1001 -> {
                drawVerticalLine(g2d, x, y, size);
                if (layer.q[1] == Primitive.EMPTY && layer.q[2] == Primitive.EMPTY)
                    return;
            }
            case 0b0110 -> {
                drawVerticalLine(g2d, x, y, size);
                if (layer.q[0] == Primitive.EMPTY && layer.q[3] == Primitive.EMPTY)
                    return;
            }
        }

        int qs = size / 2;

        for (int q = 0; q < 4; q++) {
            Primitive p = layer.q[q];

            if (p == Primitive.EMPTY)
                continue;

            switch (p) {
                case CIRCLE -> drawCircleQuarter(g2d, x, y, size, q);
                case SQUARE -> {
                    int px = x + QX[q] * qs;
                    int py = y + QY[q] * qs;
                    drawSquareQuarter(g2d, px, py, qs, q);
                }
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
        int x2 = x + s - 1;
        int y2 = y + s - 1;

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
    // #endregion

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        // just in case
        if (getClass() != obj.getClass())
            return false;

        Glyph other = (Glyph) obj;

        if (this.layers.size() != other.layers.size())
            return false;

        for (int i = 0; i < other.layers.size(); i++) {
            GlyphLayer l1 = this.layers.get(i);
            GlyphLayer l2 = other.layers.get(i);

            for (int q = 0; q < GlyphLayer.QUARTERS; q++) {
                if (l1.q[q] != l2.q[q]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;

        for (GlyphLayer layer : layers) {
            for (int i = 0; i < GlyphLayer.QUARTERS; i++) {
                result = 31 * result + (layer.q[i] != null ? layer.q[i].hashCode() : 0);
            }
        }
        return result;
    }
}
