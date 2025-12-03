package me.mert.ui;

import me.mert.core.Constants;

// idk if this class being here is the best option
public class Camera {
    public double x = 0;
    public double y = 0;
    public double zoom = 1.0;

    final double MIN_ZOOM = Constants.MIN_ZOOM;
    final double MAX_ZOOM = Constants.MAX_ZOOM;

    public Camera(int x, int y) {
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
        double worldX = screenToWorldX(screenX);
        return (int) Math.floor(worldX / Constants.CELL_SIZE);
    }

    public int screenToCellY(int screenY) {
        double worldY = screenToWorldY(screenY);
        return (int) Math.floor(worldY / Constants.CELL_SIZE);
    }

    public void update() {
        x = Math.round(x * 100000.0) / 100000.0;
        y = Math.round(y * 100000.0) / 100000.0;
    }
}
