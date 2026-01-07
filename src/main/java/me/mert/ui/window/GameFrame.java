package me.mert.ui.window;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
    public GameFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setFocusable(true);
        setVisible(true);
        setTitle("Gliphy");
        add(new MainWindow());
        pack();

    }
}
