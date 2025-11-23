package me.mert;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class MouseInput extends MouseAdapter {
    private Camera camera;
    JPanel panel;
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

        camera.x += dx;
        camera.y += dy;

        lastX = e.getX();
        lastY = e.getY();

        panel.repaint();
    }
}
