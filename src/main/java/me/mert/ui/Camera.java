package me.mert.ui;

import me.mert.world.World;

// idk if this class being here is the best option
public class Camera {
    public double x = 0;
    public double y = 0;
    public double zoom = 1.0;

    final double MIN_ZOOM = 0.05f;
    final double MID_ZOOM = 0.25f;
    final double MAX_ZOOM = 3.0f;

    public Camera(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Camera(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setZoom(double zoom) {
        this.zoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, zoom));
    }

    public void adjustZoom(double delta) {
        setZoom(zoom + delta);
    }

    // to the guy(me) who doubts all these methods...
    // they are correct
    public double screenToWorldX(int screenX) {
        return screenX / zoom + x;
    }

    public double screenToWorldY(int screenY) {
        return screenY / zoom + y;
    }

    public int worldToScreenX(int worldX) {
        return (int) ((worldX - x) * zoom);
    }

    public int worldToScreenY(int worldY) {
        return (int) ((worldY - y) * zoom);
    }

    public int screenToCellX(int screenX) {
        return (int) Math.floorDiv((int) Math.floor(screenToWorldX(screenX)), World.CELL_SIZE);
    }

    public int screenToCellY(int screenY) {
        return (int) Math.floorDiv((int) Math.floor(screenToWorldY(screenY)), World.CELL_SIZE);
    }

    public int cellToScreenX(int cellX) {
        return worldToScreenX(cellX * World.CELL_SIZE);
    }

    public int cellToScreenY(int cellY) {
        return worldToScreenY(cellY * World.CELL_SIZE);
    }

    public void update() {
        x = Math.round(x * 100000.0) / 100000.0;
        y = Math.round(y * 100000.0) / 100000.0;
    }
}
