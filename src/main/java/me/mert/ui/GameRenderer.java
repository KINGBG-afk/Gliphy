package me.mert.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;

import me.mert.components.Component;
import me.mert.core.Constants;
import me.mert.core.enums.Direction;
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

    private int getSize() {
        return (int) (CELL_SIZE * camera.zoom);
    }

    // --- MOUSE ------------------------------------------------------------
    public void drawPreviewComponent(Graphics g, Component c, boolean correct) {
        if (!correct)
            return;

        Graphics2D g2d = (Graphics2D) g;

        int worldX = camera.screenToCellX(mouse.x) * CELL_SIZE;
        int worldY = camera.screenToCellY(mouse.y) * CELL_SIZE;

        int screenX = camera.worldToScreenX(worldX);
        int screenY = camera.worldToScreenY(worldY);
        int size = getSize();

        // Save original transform
        AffineTransform oldTransform = g2d.getTransform();

        // Move origin to center of the cell
        g2d.translate(screenX + size / 2.0, screenY + size / 2.0);

        // Rotate based on direction
        g2d.rotate(directionToRadians(c.direction));

        g2d.drawImage(
                c.previewImage,
                -size / 2,
                -size / 2,
                size,
                size,
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

    // --- GRID ------------------------------------------------------------
    public void drawGrid(Graphics g, int screenWidth, int screenHeight) {
        g.setColor(new Color(90, 90, 90));

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
            g.drawLine(sx, screenWorldMinY, sx, screenWorldMaxY);
        }

        // horizontal lines
        for (int y = firstGridY; y <= worldEndY; y += CELL_SIZE) {
            int sy = camera.worldToScreenY(y);
            g.drawLine(screenWorldMinX, sy, screenWorldMaxX, sy);
        }
    }

    // --- COMPONENTS ------------------------------------------------------------
    public void drawComponents(Graphics g, int screenWidth, int screenHeight) {
        double zoom = camera.zoom;

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

                // TEMP to visualize the items flowing through
                if (obj.hasItem()) {
                    g.setColor(Color.RED);
                    g.fillOval(screenX, screenY, 20, 20);
                }
            }
        }
    }
}
