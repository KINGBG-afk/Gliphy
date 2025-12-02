package me.mert.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

import me.mert.ui.Camera;

public class MouseInput extends MouseAdapter {
    private Camera camera;
    private JPanel panel;
    private int lastX, lastY;

    public MouseInput(Camera camera, JPanel panel) {
        this.camera = camera;
        this.panel = panel;
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

        panel.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        double worldX = camera.screenToWorldX((int) mouseX);
        double worldY = camera.screenToWorldX((int) mouseY);

        double zoomDeleta = -e.getPreciseWheelRotation() * 0.1;
        camera.adjustZoom(zoomDeleta);

        camera.x = worldX - (mouseX / camera.zoom);
        camera.y = worldY - (mouseY / camera.zoom);

        camera.update();
        panel.repaint();
    }

}
