package me.mert.core;

import java.awt.Color;
import java.awt.Graphics;

import me.mert.Camera;

public class GameRenderer {

    private final Camera camera;
    public GameRenderer(Camera camera) {
        this.camera = camera;
    }

    public void drawGrid(Graphics g, int screenWidth, int screenHeight) {
        g.setColor(new Color(90, 90, 90));

        int startX = camera.x - screenWidth;
        int startY = camera.y - screenHeight;

        for (int x = startX; x < screenWidth; x += Constants.CELL_SIZE) {
            for (int y = startY; y < screenHeight; y += Constants.CELL_SIZE){
                g.drawRect(x, y, screenWidth, screenHeight);
            }
        }
    }
}