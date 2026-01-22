package me.mert.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;

import me.mert.components.Component;
import me.mert.components.Conveyor;
import me.mert.core.Constants;
import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.glyph.Glyph;
import me.mert.world.Tile;
import me.mert.world.World;

public class GameRenderer {

    private final Camera camera;
    private final World world;
    public Point mouse;
    private final int CELL_SIZE;

    public GameRenderer(Camera camera, World world) {
        this.camera = camera;
        this.world = world;
        this.mouse = new Point(0, 0);
        this.CELL_SIZE = Constants.CELL_SIZE;
    }

    // with the amount of math in this file i deserve at least a bit of appreciation
    // but sadly nobody is here to see it :(
    private int getSize() {
        return (int) (CELL_SIZE * camera.zoom);
    }

    // --- MOUSE ------------------------------------------------------------
    public void drawPreviewComponent(Graphics g, Component c, boolean correct) {
        if (!correct)
            return;

        Graphics2D g2d = (Graphics2D) g;

        int cellPx = getSize();

        int worldX = camera.screenToCellX(mouse.x) * CELL_SIZE;
        int worldY = camera.screenToCellY(mouse.y) * CELL_SIZE;

        int screenX = camera.worldToScreenX(worldX);
        int screenY = camera.worldToScreenY(worldY);

        int cellsW = c.size[0];
        int cellsH = c.size[1];

        // for 90/270 rotations the footprint swaps on screen
        boolean swap = (c.direction == Direction.EAST || c.direction == Direction.WEST);
        int drawW = (swap ? cellsH : cellsW) * cellPx;
        int drawH = (swap ? cellsW : cellsH) * cellPx;

        AffineTransform oldTransform = g2d.getTransform();

        g2d.translate(screenX + drawW / 2.0, screenY + drawH / 2.0);
        g2d.rotate(directionToRadians(c.direction));

        // after rotation draw the unrotated img
        int unrotW = cellsW * cellPx;
        int unrotH = cellsH * cellPx;

        g2d.drawImage(
                c.previewImage,
                -unrotW / 2,
                -unrotH / 2,
                unrotW,
                unrotH,
                null);

        g2d.setTransform(oldTransform);
    }

    private double directionToRadians(Direction d) {
        return switch (d) {
            case NORTH -> 0;
            case EAST -> Math.PI / 2;
            case SOUTH -> Math.PI;
            case WEST -> -Math.PI / 2;
        };
    }

    // --- BACKGROUND ------------------------------------------------------------
    public void drawVignette(Graphics2D g2d, int w, int h) {
        float radius = Math.max(w, h) * 0.75f;

        float[] dist = { 0.0f, 0.4f, 1.0f };
        Color[] colors = {
                new Color(0, 0, 0, 0),
                new Color(0, 0, 0, 70),
                new Color(0, 0, 0, 129)
        };

        RadialGradientPaint paint = new RadialGradientPaint(
                w / 2f,
                h / 2f,
                radius,
                dist,
                colors);

        Paint old = g2d.getPaint();
        g2d.setPaint(paint);
        g2d.fillRect(0, 0, w, h);
        g2d.setPaint(old);

    }

    // --- GRID ------------------------------------------------------------
    public void drawGrid(Graphics2D g2d, int screenWidth, int screenHeight) {
        g2d.setColor(new Color(230, 230, 230));
        g2d.setStroke(new BasicStroke(Math.max(1, (int) ((20 * camera.zoom * 0.6f) / 6))));

        // find the visible world bouds
        int worldStartX = (int) camera.screenToWorldX(0);
        int worldEndX = (int) camera.screenToWorldX(Constants.GRID_PIXEL_WIDTH);
        int worldStartY = (int) camera.screenToWorldY(0);
        int worldEndY = (int) camera.screenToWorldY(Constants.GRID_PIXEL_HEIGHT);

        // clamp boundaries
        worldStartX = Math.max(worldStartX, 0);
        worldEndX = Math.min(worldEndX, Constants.GRID_PIXEL_WIDTH);

        worldStartY = Math.max(worldStartY, 0);
        worldEndY = Math.min(worldEndY, Constants.GRID_PIXEL_HEIGHT);

        int screenWorldMinX = camera.worldToScreenX(0);
        int screenWorldMinY = camera.worldToScreenY(0);

        int screenWorldMaxX = camera.worldToScreenX(Constants.GRID_PIXEL_WIDTH);
        int screenWorldMaxY = camera.worldToScreenY(Constants.GRID_PIXEL_HEIGHT);

        // first world grid line
        int firstGridX = (worldStartX / CELL_SIZE) * CELL_SIZE;
        int firstGridY = (worldStartY / CELL_SIZE) * CELL_SIZE;

        // vertical lines
        for (int x = firstGridX; x <= worldEndX; x += CELL_SIZE) {
            int sx = camera.worldToScreenX(x);
            g2d.drawLine(sx, screenWorldMinY, sx, screenWorldMaxY);
        }

        // horizontal lines
        for (int y = firstGridY; y <= worldEndY; y += CELL_SIZE) {
            int sy = camera.worldToScreenY(y);
            g2d.drawLine(screenWorldMinX, sy, screenWorldMaxX, sy);
        }
    }

    // --- COMPONENTS ------------------------------------------------------------
    public void drawComponents(Graphics g, int screenWidth, int screenHeight) {
        double zoom = camera.zoom;

        // still don't get why there is Graphics and Graphics2D :/
        Graphics2D g2d = (Graphics2D) g;

        // visible world range
        double worldStartX = camera.x;
        double worldEndX = camera.x + screenWidth / zoom;

        double worldStartY = camera.y;
        double worldEndY = camera.y + screenHeight / zoom;

        // translate to tile indices
        int startX = Math.max(0, (int) (worldStartX / CELL_SIZE));
        int endX = Math.min(Constants.GRID_CELL_WIDTH, (int) (worldEndX / CELL_SIZE) + 1);

        int startY = Math.max(0, (int) (worldStartY / CELL_SIZE));
        int endY = Math.min(Constants.GRID_CELL_HEIGHT, (int) (worldEndY / CELL_SIZE) + 1);

        // avoid drawing same object multiple times
        Set<Component> drawn = new HashSet<>();

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {

                Tile tile = world.getTile(i, j);
                if (tile == null)
                    continue;

                Component obj = tile.getComponent();
                if (obj == null || drawn.contains(obj))
                    continue;

                drawn.add(obj);

                // object world coordinates
                int worldX = (obj.j * CELL_SIZE);
                int worldY = (obj.i * CELL_SIZE);

                int screenX = camera.worldToScreenX(worldX);
                int screenY = camera.worldToScreenY(worldY);

                // it's impossible for obj to not have an image
                // except if you are retarted and remove the resource folder
                obj.render(g, screenX, screenY, zoom, CELL_SIZE);

                // the collector doesn't have input port so it doesn't render anyway
                if (obj.hasItem() && (obj.type != ComponentType.HUB || obj.type != ComponentType.CUTTER)) {

                    Glyph glyph = obj.getItem();

                    if (glyph == null)
                        continue;

                    int glyphSize = (int) (CELL_SIZE * zoom * 0.6);
                    int gx = screenX + (int) (CELL_SIZE * zoom / 2 - glyphSize / 2);
                    int gy = screenY + (int) (CELL_SIZE * zoom / 2 - glyphSize / 2);

                    Glyph.render(g2d, glyph, gx, gy, glyphSize);
                }
            }
        }
        // update animation for conveyor
        Conveyor.updateAnimation();
    }
}
