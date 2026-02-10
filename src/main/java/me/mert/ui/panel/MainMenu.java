package me.mert.ui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import me.mert.ui.window.MainWindow;

public class MainMenu extends JPanel {

    JButton startButton;

    public MainMenu(MainWindow root) {
        setBackground(new Color(200, 200, 200));
        setFocusable(true);
        setLayout(new GridBagLayout());

        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(160, 60));
        startButton.addActionListener(e -> root.showWorld());

        add(startButton);

    }
}
