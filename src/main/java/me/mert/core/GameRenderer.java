package me.mert.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import me.mert.components.GameObject;
import me.mert.ui.Camera;
import me.mert.world.World;

// at some point i feel like there are gonna be issues with the performance here
// *detective-Doakes-meme.png*
public class GameRenderer {

    private final Camera camera;
    private final World world;

    public GameRenderer(Camera camera, World world) {
        this.camera = camera;
        this.world = world;

    }

    private void drawRotatedImage(Graphics g, BufferedImage img, int x, int y, int width, int height,
            int orientation) {
        Graphics2D g2d = (Graphics2D) g.create();

        switch (orientation) {
            case 0:
                // no rotation
                g2d.drawImage(img, x, y, width, height, null);
                break;

            case 1:
                // 90 degree
                g2d.translate(x + width, y);
                g2d.rotate(Math.PI / 2);
                g2d.drawImage(img, 0, 0, width, height, null);
                break;

            case 2:
                // 180 degree
                g2d.translate(x + width, y + height);
                g2d.rotate(Math.PI);
                g2d.drawImage(img, 0, 0, width, height, null);
                break;

            case 3:
                // 270 degree
                g2d.translate(x, y + height);
                g2d.rotate(Math.PI / 2);
                g2d.drawImage(img, 0, 0, width, height, null);
                break;
        }
    }

    // i want the compoenents and the grid to be drawn from 2 different methods
    // (it's slower i know)
    public void drawComponents(Graphics g, int screenWidth, int screenHeight) {
        int startX = Math.max(0, camera.x / Constants.CELL_SIZE);
        int startY = Math.max(0, camera.y / Constants.CELL_SIZE);
        int endX = (camera.x + screenWidth) / Constants.CELL_SIZE + 1;
        int endY = (camera.y + screenHeight) / Constants.CELL_SIZE + 1;

        // it's to avoid drawing something twice
        Set<GameObject> drawnComponents = new HashSet<>();

        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                GameObject obj = world.getTile(i, j).getComponent();

                if (obj != null && !drawnComponents.contains(obj)) {
                    drawnComponents.add(obj);

                    // get screen pos
                    int objSX = obj.i * Constants.CELL_SIZE - camera.x;
                    int objSY = obj.j * Constants.CELL_SIZE - camera.y;
                    int objWidth = obj.size[0] * Constants.CELL_SIZE;
                    int objHeight = obj.size[1] * Constants.CELL_SIZE;

                    if (obj.img != null) {

                        drawRotatedImage(g, obj.img, objSX, objSY, objWidth, objHeight, obj.orientation);
                    } else {
                        // this is like the 2nd protection of missing assets
                        // don't know if its good to have more than 1
                        g.setColor(Color.BLUE);
                        g.fillRect(objSX, objSY, objWidth, objHeight);
                    }
                }

            }
        }
    }

    public void drawGrid(Graphics g, int screenWidth, int screenHeight) {
        g.setColor(new Color(90, 90, 90));

        int cell = Constants.CELL_SIZE;

        // world limits in pixels
        int worldW = Constants.GRID_CELL_WIDTH * cell;
        int worldH = Constants.GRID_CELL_HEIGHT * cell;

        // camera-space offset
        int startX = -(camera.x % cell);
        int startY = -(camera.y % cell);

        // vertical lines inside world boundaries
        for (int i = startX; i < screenWidth; i += cell) {
            int worldX = i + camera.x;

            if (worldX >= 0 && worldX <= worldW) {
                int lineStartY = Math.max(0, -camera.y);
                int lineEndY = Math.min(screenHeight, worldH - camera.y);

                g.drawLine(i, lineStartY, i, lineEndY);
            }
        }

        // horizontal lines inside world boundaries
        for (int j = startY; j < screenHeight; j += cell) {
            int worldY = j + camera.y;

            if (worldY >= 0 && worldY <= worldH) {
                int lineStartX = Math.max(0, -camera.x);
                int lineEndX = Math.min(screenWidth, worldW - camera.x);

                g.drawLine(lineStartX, j, lineEndX, j);
            }
        }
    }
}
