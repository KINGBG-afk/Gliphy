package me.mert.ui.panel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import me.mert.core.GliphyUtilities;
import me.mert.ui.widgets.IconButton;
import me.mert.ui.window.MainWindow;

public class MainMenu extends JPanel {

    private final IconButton startButton;
    private final IconButton quitButton;
    private final IconButton soundButton;
    private final IconButton languageButton;
    private final IconButton creditsButton;
    private final Image bgImage;

    // TODO: make objects of keyboard and mouse here
    public MainMenu(MainWindow root) {
        setFocusable(true);
        setLayout(null);
        String path = "/ui/button.png";
        String pressedPath = "/ui/pressed-button.png";

        // HACK: i know hard coded it but.. it works
        ImageIcon speakerIcon = GliphyUtilities.loadIcon("/icons/speaker.png", 64, 64);
        ImageIcon muteIcon = GliphyUtilities.loadIcon("/icons/speaker-mute.png", 64, 64);

        ImageIcon bgIcon = GliphyUtilities.loadIcon("/icons/bg.png", 70, 70);
        ImageIcon bgHoverIcon = GliphyUtilities.loadIcon("/icons/bg-hover.png", 70, 70);
        ImageIcon enIcon = GliphyUtilities.loadIcon("/icons/en.png", 70, 70);
        ImageIcon enHoverIcon = GliphyUtilities.loadIcon("/icons/en-hover.png", 70, 70);
        
        // i know the size should be dynamic but we have to cut corners
        bgImage = GliphyUtilities.loadIcon("/ui/background.png", 1920, 1080).getImage();

        startButton = new IconButton(
                "Play",
                GliphyUtilities.loadIcon(path, 300, 100),
                GliphyUtilities.loadIcon(pressedPath, 300, 100));
        startButton.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        startButton.setBounds(380, 650, 300, 100);
        startButton.addActionListener(e -> root.showWorld());

        quitButton = new IconButton(
                "Quit",
                GliphyUtilities.loadIcon(path, 300, 100),
                GliphyUtilities.loadIcon(pressedPath, 300, 100));
        quitButton.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        quitButton.setBounds(380, 790, 300, 100);
        quitButton.addActionListener(e -> System.exit(0));

        soundButton = new IconButton(
                "",
                speakerIcon,
                speakerIcon);
        soundButton.setBounds(1650, 20, 64, 64);
        soundButton.addActionListener(e -> {
            if (soundButton.getIcon() == speakerIcon) {
                soundButton.setIcon(muteIcon);
            } else {
                soundButton.setIcon(speakerIcon);
            }
        });

        languageButton = new IconButton(
                "",
                enIcon,
                enIcon,
                enHoverIcon);
        languageButton.setBounds(1750, 20, 70, 70);
        languageButton.addActionListener(e -> {
            if (languageButton.getIcon() == enIcon) {
                languageButton.setIcon(bgIcon);
                languageButton.setHighlightIcon(bgHoverIcon);
            } else {
                languageButton.setIcon(enIcon);
                languageButton.setHighlightIcon(enHoverIcon);
            }
        });

        creditsButton = new IconButton(
                "Credits",
                GliphyUtilities.loadIcon(path, 300, 100),
                GliphyUtilities.loadIcon(pressedPath, 300, 100));
        creditsButton.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        creditsButton.setBounds(1550, 940, 300, 100);
        // TODO: creditsButton.addActionListener(e ->);

        add(startButton);
        add(quitButton);
        add(creditsButton);
        add(soundButton);
        add(languageButton);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
