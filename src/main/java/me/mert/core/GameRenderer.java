package me.mert.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import me.mert.components.Component;
import me.mert.ui.Camera;
import me.mert.world.Tile;
import me.mert.world.World;

public class GameRenderer {

    private final Camera camera;
    private final World world;

    public GameRenderer(Camera camera, World world) {
        this.camera = camera;
        this.world = world;
    }

    // --- GRID ------------------------------------------------------------
    public void drawGrid(Graphics g, int screenWidth, int screenHeight) {
        g.setColor(new Color(90, 90, 90));

        int cell = Constants.CELL_SIZE;

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
        int firstGridX = (worldStartX / cell) * cell;
        int firstGridY = (worldStartY / cell) * cell;

        // vertical lines
        for (int x = firstGridX; x <= worldEndX; x += cell) {
            int sx = camera.worldToScreenX(x);
            g.drawLine(sx, screenWorldMinY, sx, screenWorldMaxY);
        }

        // horizontal lines
        for (int y = firstGridY; y <= worldEndY; y += cell) {
            int sy = camera.worldToScreenY(y);
            g.drawLine(screenWorldMinX, sy, screenWorldMaxX, sy);
        }
    }

    // --- COMPONENTS ------------------------------------------------------------

    private void drawRotatedImage(Graphics g, BufferedImage img,
            int x, int y, int width, int height, Direction dir) {

        Graphics2D g2d = (Graphics2D) g.create();

        switch (dir) {
            case Direction.NORTH -> // 0 degree
                g2d.drawImage(img, x, y, width, height, null);

            case Direction.EAST -> {
                // 90 degrees
                g2d.translate(x + width, y);
                g2d.rotate(Math.PI / 2);
                g2d.drawImage(img, 0, 0, width, height, null);
            }

            case Direction.SOUTH -> {
                // 180 degrees
                g2d.translate(x + width, y + height);
                g2d.rotate(Math.PI);
                g2d.drawImage(img, 0, 0, width, height, null);
            }

            case Direction.WEST -> {
                // 270 degree
                g2d.translate(x, y + height);
                g2d.rotate(Math.PI / 2);
                g2d.drawImage(img, 0, 0, width, height, null);
            }
        }

        g2d.dispose();
    }

    public void drawComponents(Graphics g, int screenWidth, int screenHeight) {
        int cell = Constants.CELL_SIZE;
        double zoom = camera.zoom;

        // visible world range
        double worldStartX = camera.x;
        double worldEndX = camera.x + screenWidth / zoom;

        double worldStartY = camera.y;
        double worldEndY = camera.y + screenHeight / zoom;

        // translate to tile indices
        int startX = Math.max(0, (int) (worldStartX / cell));
        int endX = Math.min(Constants.GRID_CELL_WIDTH, (int) (worldEndX / cell) + 1);

        int startY = Math.max(0, (int) (worldStartY / cell));
        int endY = Math.min(Constants.GRID_CELL_HEIGHT, (int) (worldEndY / cell) + 1);

        // avoid drawing same object multiple times
        Set<Component> drawn = new HashSet<>();

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {

                Tile tile = world.getTile(i, j);
                if (tile == null)
                    continue;
                Component obj = tile.getComponent();
                if (obj == null)
                    continue;

                if (drawn.contains(obj))
                    continue;
                drawn.add(obj);

                // object world coordinates
                int worldX = (obj.j * cell);
                int worldY = (obj.i * cell);

                int screenX = camera.worldToScreenX(worldX);
                int screenY = camera.worldToScreenY(worldY);

                int screenW = (int) (obj.size[0] * cell * zoom);
                int screenH = (int) (obj.size[1] * cell * zoom);

                if (obj.img != null) {
                    drawRotatedImage(g, obj.img, screenX, screenY, screenW, screenH, obj.direction);
                } else {
                    g.setColor(Color.BLUE);
                    g.fillRect(screenX, screenY, screenW, screenH);
                }

                // TEMP to visualize the items flowing through
                if (obj.hasItem()) {
                    g.setColor(Color.RED);
                    g.fillOval(screenX, screenY, 20, 20);
                }
            }
        }
    }
}
