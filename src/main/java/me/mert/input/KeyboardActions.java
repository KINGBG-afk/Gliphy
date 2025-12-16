package me.mert.input;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import me.mert.ui.Camera;

public class KeyboardActions {

    private final Camera camera;

    public KeyboardActions(Camera camera) {
        this.camera = camera;
    }

    public void register(JComponent panel) {
        InputMap im = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panel.getActionMap();

        int move = 12;

        // W
        im.put(KeyStroke.getKeyStroke("W"), "moveUp");
        am.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.y -= move;
                panel.repaint();
            }
        });

        // S
        im.put(KeyStroke.getKeyStroke("S"), "moveDown");
        am.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.y += move;
                panel.repaint();
            }
        });

        // A
        im.put(KeyStroke.getKeyStroke("A"), "moveRight");
        am.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.x -= move;
                panel.repaint();
            }
        });

        // D
        im.put(KeyStroke.getKeyStroke("D"), "moveLeft");
        am.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.x += move;
                panel.repaint();
            }
        });
    }
}