package me.mert.input;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import me.mert.game.CurrencyManager;
import me.mert.ui.Camera;
import me.mert.ui.panel.GamePanel;

public class KeyboardActions {

    private final Camera camera;

    public KeyboardActions(Camera camera) {
        this.camera = camera;
    }

    public void register(GamePanel panel) {
        InputMap im = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panel.getActionMap();

        int move = 12;

        // R
        im.put(KeyStroke.getKeyStroke("R"), "rotate");
        am.put("rotate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.rotateDirection();
            }
        });

        // Q
        im.put(KeyStroke.getKeyStroke("Q"), "orotate");
        am.put("orotate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.rotateOtherDirection();
            }
        });

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

        // ESCAPE
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "esc");
        am.put("esc", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
                // panel.showWorldMenu();
            }
        });

        // SAVE
        im.put(KeyStroke.getKeyStroke("E"), "save");
        am.put("save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.saveWorld();
            }
        });

        // TEMP: remove later
        im.put(KeyStroke.getKeyStroke("C"), "cheat");
        am.put("cheat", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrencyManager.getInstance().add(9999);
            }
        });

    }
}