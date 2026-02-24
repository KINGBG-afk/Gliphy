package me.mert.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;

import me.mert.components.Component;
import me.mert.components.Conveyor;
import me.mert.components.Port;
import me.mert.core.Constants;
import me.mert.core.GliphyUtilities;
import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.core.enums.LayerType;
import me.mert.core.enums.PortType;
import me.mert.game.CurrencyManager;
import me.mert.game.LevelManager;
import me.mert.glyph.Glyph;
import me.mert.world.Tile;
import me.mert.world.World;

public class GameRenderer {

    private final Camera camera;
    private final World world;
    public Point mouse;
    private final int CELL_SIZE;
    private final Image coinImage = GliphyUtilities.loadIcon("/icons/coin.png", 40, 40).getImage();
    private final Image arrowImage = GliphyUtilities.loadIcon("/icons/arrow.png", 64, 64).getImage();
    private static final Color TILE_COLOR = new Color(0, 0, 0, 17);

    public GameRenderer(Camera camera, World world) {
        this.camera = camera;
        this.world = world;
        this.mouse = new Point(0, 0);
        this.CELL_SIZE = Constants.CELL_SIZE;
    }

    // with the amount of math in this file i deserve at least a bit of appreciation
    // but sadly nobody will see it :(
    private int getSize() {
        return (int) (CELL_SIZE * camera.zoom);
    }

    public void debugDraw(Graphics g, int w, int h) {

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(LevelManager.getInstance().getLevel()), 400, 400);
        g.drawString(String.valueOf(LevelManager.getInstance().getGoalAmount()), 400, 430);

    }

    // --- MOUSE ------------------------------------------------------------
    public void drawPreviewComponent(Graphics g, Component c) {
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

        for (Port p : c.getAllPorts()) {
            // quick and dirty
            if (c.type == ComponentType.COLLECTOR && p.type == PortType.INPUT) {
                continue;
            }

            int px = p.localJ * cellPx;
            int py = p.localI * cellPx;

            int centerX = -unrotW / 2 + px + cellPx / 2;
            int centerY = -unrotH / 2 + py + cellPx / 2;

            AffineTransform arrowTransform = g2d.getTransform();

            g2d.translate(centerX, centerY);

            // rotate based on port type
            double portAngle = directionToRadians(p.getDirection())
                    + (p.type == PortType.INPUT ? Math.PI : 0);
            g2d.rotate(portAngle);

            int arrowSize = cellPx / 2;
            g2d.drawImage(
                    arrowImage,
                    -arrowSize / 2,
                    -arrowSize / 2,
                    arrowSize,
                    arrowSize,
                    null);

            g2d.setTransform(arrowTransform);
        }
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
                new Color(0, 0, 0, 60),
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
        int worldStartY = (int) camera.screenToWorldY(0);
        int worldEndX = (int) camera.screenToWorldX(screenWidth);
        int worldEndY = (int) camera.screenToWorldY(screenHeight);

        // first world grid line
        int firstGridX = Math.floorDiv(worldStartX, CELL_SIZE) * CELL_SIZE;
        int firstGridY = Math.floorDiv(worldStartY, CELL_SIZE) * CELL_SIZE;

        // vertical lines
        for (int x = firstGridX; x <= worldEndX; x += CELL_SIZE) {
            int sx = camera.worldToScreenX(x);
            g2d.drawLine(sx, 0, sx, screenHeight);
        }

        // horizontal lines
        for (int y = firstGridY; y <= worldEndY; y += CELL_SIZE) {
            int sy = camera.worldToScreenY(y);
            g2d.drawLine(0, sy, screenWidth, sy);
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
        int startX = (int) Math.floor(worldStartX / CELL_SIZE);
        int endX = (int) Math.ceil(worldEndX / CELL_SIZE);

        int startY = (int) Math.floor(worldStartY / CELL_SIZE);
        int endY = (int) Math.ceil(worldEndY / CELL_SIZE);

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

                obj.render(g, screenX, screenY, zoom, CELL_SIZE);

                if (obj.hasItem() && (obj.type == ComponentType.CONVEYOR || obj.type == ComponentType.COLLECTOR)) {
                    Glyph glyph = obj.getItem();
                    int glyphSize = (int) (getSize() * 0.6f);
                    int gx = screenX + (int) (CELL_SIZE * zoom / 2 - glyphSize / 2);
                    int gy = screenY + (int) (CELL_SIZE * zoom / 2 - glyphSize / 2);

                    Glyph.render(g2d, glyph, gx, gy, glyphSize);
                }
            }
        }
        // update animation for conveyor
        Conveyor.updateAnimation();
    }

    // --- RECOURSES ------------------------------------------------------------
    public void drawTiles(Graphics2D g2d, int screenWidth, int screenHeight) {
        double zoom = camera.zoom;

        double worldStartX = camera.x;
        double worldEndX = camera.x + screenWidth / zoom;
        double worldStartY = camera.y;
        double worldEndY = camera.y + screenHeight / zoom;

        int startX = (int) Math.floor(worldStartX / CELL_SIZE);
        int endX = (int) Math.ceil(worldEndX / CELL_SIZE);
        int startY = (int) Math.floor(worldStartY / CELL_SIZE);
        int endY = (int) Math.ceil(worldEndY / CELL_SIZE);

        int cellPx = getSize();
        int glyphSize = (int) (cellPx * 0.6f);
        int glyphOffset = (cellPx - glyphSize) / 2;

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {

                Tile tile = world.getTile(i, j);
                if (tile == null)
                    continue;

                LayerType r = tile.getRecourse();
                if (r == null)
                    continue;

                int screenX = camera.worldToScreenX(j * CELL_SIZE);
                int screenY = camera.worldToScreenY(i * CELL_SIZE);

                g2d.setColor(TILE_COLOR);
                g2d.fillRect(screenX, screenY, cellPx, cellPx);

                Glyph.render(g2d,
                        new Glyph(r),
                        screenX + glyphOffset,
                        screenY + glyphOffset,
                        glyphSize);
            }
        }
    }

    public void drawGoal(Graphics g, int screenWidth, int screenHeight) {
        LevelManager mgr = LevelManager.getInstance();
        Glyph glyph = mgr.getLevelGoal();

        g.setColor(new Color(215, 215, 215));
        g.fillOval(10, screenHeight / 2 - 70, 50, 50);

        Glyph.render((Graphics2D) g, glyph, 18, screenHeight / 2 - 63, 35);

        g.setFont(new Font("sans-serif", Font.PLAIN, 27));
        g.setColor(Color.BLACK);
        g.drawString("/" + mgr.getGoalAmount(), 69, screenHeight / 2 - 20);

        g.drawString(String.valueOf(mgr.getStored()), 69, screenHeight / 2 - 50);
    }

    public void drawCoins(Graphics g, int screenWidth, int screenHeight) {
        g.setColor(new Color(215, 215, 215));
        CurrencyManager mgr = CurrencyManager.getInstance();

        g.fillRoundRect(
                20, 20,
                160, 65,
                15, 15);
        g.drawImage(coinImage, 30, 30, null);

        g.setFont(new Font("sans-serif", Font.PLAIN, 38));
        g.setColor(Color.BLACK);
        g.drawString(mgr.getCoinsString(), 75, 65);
    }
}
