package me.mert.input;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import me.mert.core.GameRenderer;
import me.mert.ui.Camera;
import me.mert.ui.GamePanel;

public class MouseInput extends MouseAdapter {
    private final Camera camera;
    private final GamePanel panel;
    private final GameRenderer renderer;
    private int lastX, lastY;

    public MouseInput(Camera camera, GamePanel panel, GameRenderer renderer) {
        this.camera = camera;
        this.panel = panel;
        this.renderer = renderer;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
        panel.placeSelectedComponentAt(camera.screenToCellY(lastY), camera.screenToCellX(lastX));

        // TEMP debug
        System.out.println("cell pos: " + camera.screenToCellX(lastX) + ", " + camera.screenToCellY(lastY));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - lastX;
        int dy = e.getY() - lastY;

        camera.x -= (int) (dx / camera.zoom);
        camera.y -= (int) (dy / camera.zoom);

        lastX = e.getX();
        lastY = e.getY();
        renderer.mouse = new Point(lastX, lastY);

        panel.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        double worldX = camera.screenToWorldX((int) mouseX);
        double worldY = camera.screenToWorldY((int) mouseY);

        double zoomDeleta = -e.getPreciseWheelRotation() * 0.1;
        camera.adjustZoom(zoomDeleta);

        camera.x = worldX - (mouseX / camera.zoom);
        camera.y = worldY - (mouseY / camera.zoom);

        camera.update();
        panel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        renderer.mouse = new Point(e.getX(), e.getY());
    }

}
