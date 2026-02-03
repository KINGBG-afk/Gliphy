package me.mert;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.SwingUtilities;

import me.mert.ui.window.MainWindow;

public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "True");

        SwingUtilities.invokeLater(() -> {

            MainWindow frame = new MainWindow();
            frame.setUndecorated(true);
            frame.setResizable(false);

            // this is wayland panels doesn't cover the window
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice();
            gd.setFullScreenWindow(frame);

            frame.setVisible(true);

        });
    }
}
