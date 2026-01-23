package me.mert;

import javax.swing.SwingUtilities;

import me.mert.ui.window.MainWindow;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow frame = new MainWindow();
            frame.setUndecorated(true);
            frame.setResizable(false);
            frame.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
            
        });
    }
}
