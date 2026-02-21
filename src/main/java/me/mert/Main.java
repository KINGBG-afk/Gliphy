package me.mert;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.SwingUtilities;

import me.mert.ui.window.MainWindow;

public class Main {
    public static void main(String[] args) {
        boolean isLinux = ("linux".equals(System.getProperty("os.name").toLowerCase()));
        // but what if the player is not playing on a 1920x1... SHUT
        System.setProperty("sun.java2d.uiScale", "1.0");

        if (isLinux) {
            System.setProperty("sun.java2d.opengl", "True");
        }

        SwingUtilities.invokeLater(() -> {
            MainWindow frame = new MainWindow();

            if (isLinux) {
                // this is so wayland panels (waybar and quick shell) doesn't cover the window
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice();
                gd.setFullScreenWindow(frame);
            }

            frame.setVisible(true);
        });
    }
}
