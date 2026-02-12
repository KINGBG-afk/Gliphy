package me.mert.ui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import me.mert.core.GliphyUtilities;
import me.mert.ui.widgets.IconButton;
import me.mert.ui.window.MainWindow;

public class MainMenu extends JPanel {

    IconButton startButton;

    public MainMenu(MainWindow root) {
        setBackground(new Color(200, 200, 200));
        setFocusable(true);
        setLayout(new GridBagLayout());

        startButton = new IconButton("PLAY", GliphyUtilities.loadIcon("/ui/button.png", 300, 100), null,
                new Color(0, 0, 0, 0), true);
        startButton.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        startButton.setPreferredSize(new Dimension(300, 100));
        startButton.setRolloverIcon(GliphyUtilities.loadIcon("/ui/pressed-button.png", 300, 100));
        startButton.addActionListener(e -> root.showWorld());

        add(startButton);

    }
}
