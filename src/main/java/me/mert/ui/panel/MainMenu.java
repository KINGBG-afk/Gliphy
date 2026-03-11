package me.mert.ui.panel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import me.mert.core.GliphyUtilities;
import me.mert.game.LanguageManager;
import me.mert.game.SoundManager;
import me.mert.ui.widgets.IconButton;
import me.mert.ui.window.MainWindow;

public class MainMenu extends JPanel {

    private final IconButton startButton;
    private final IconButton quitButton;
    private final IconButton soundButton;
    private final IconButton languageButton;
    private final IconButton creditsButton;
    private final Image bgImage;

    public MainMenu(MainWindow root, LanguageManager languageManager) {
        setFocusable(true);
        setLayout(null);

        ImageIcon buttonIcon = GliphyUtilities.loadIcon("/ui/button.png", 300, 100);
        ImageIcon pressedButtonIcon = GliphyUtilities.loadIcon("/ui/pressed-button.png", 300, 100);

        ImageIcon speakerIcon = GliphyUtilities.loadIcon("/icons/speaker.png", 64,
                64);
        ImageIcon muteIcon = GliphyUtilities.loadIcon("/icons/speaker-mute.png", 64,
                64);

        ImageIcon bgIcon = GliphyUtilities.loadIcon("/icons/bg.png", 70, 70);
        ImageIcon enIcon = GliphyUtilities.loadIcon("/icons/en.png", 70, 70);
        ImageIcon bgHoverIcon = GliphyUtilities.loadIcon("/icons/bg-hover.png", 70, 70);
        ImageIcon enHoverIcon = GliphyUtilities.loadIcon("/icons/en-hover.png", 70, 70);

        // i know the size should be dynamic but we have to cut corners
        bgImage = GliphyUtilities.loadIcon("/ui/background.png", 1920, 1080).getImage();

        startButton = new IconButton(
                languageManager.getString("play"),
                buttonIcon,
                pressedButtonIcon);
        startButton.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        startButton.setBounds(380, 650, 300, 100);
        startButton.addActionListener(e -> root.showWorldMenu());

        quitButton = new IconButton(
                languageManager.getString("quit"),
                buttonIcon,
                pressedButtonIcon);
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
                SoundManager.setCanPlay(false);
            } else {
                soundButton.setIcon(speakerIcon);
                SoundManager.setCanPlay(true);
            }
        });

        ImageIcon currIcon = (languageManager.getCurrentLanguage().equals("en") ? enIcon : bgIcon);
        ImageIcon currHoverIcon = (languageManager.getCurrentLanguage().equals("en") ? enHoverIcon : bgHoverIcon);
        languageButton = new IconButton(
                "",
                currIcon,
                currIcon,
                currHoverIcon);
        languageButton.setBounds(1750, 20, 70, 70);
        languageButton.addActionListener(e -> {
            if (languageButton.getIcon() == enIcon) {
                languageButton.setIcon(bgIcon);
                languageButton.setHighlightIcon(bgHoverIcon);

                LanguageManager.getInstance().loadLanguage("bg");
                root.reloadUI();
            } else {
                languageButton.setIcon(enIcon);
                languageButton.setHighlightIcon(enHoverIcon);

                LanguageManager.getInstance().loadLanguage("en");
                root.reloadUI();
            }
        });

        creditsButton = new IconButton(
                languageManager.getString("credits"),
                buttonIcon,
                pressedButtonIcon);
        creditsButton.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        creditsButton.setBounds(1550, 940, 300, 100);
        creditsButton.addActionListener(e -> root.showCredits());

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
