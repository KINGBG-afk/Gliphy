package me.mert.ui.menu;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import me.mert.core.GliphyUtilities;
import me.mert.game.LanguageManager;
import me.mert.ui.widgets.IconButton;
import me.mert.ui.window.MainWindow;

public class CreditsPanel extends JPanel {

    private final MainWindow root;
    private IconButton backButton;
    private final LanguageManager languageManager;

    public CreditsPanel(MainWindow root, LanguageManager languageManager) {
        this.root = root;
        this.languageManager = languageManager;

        setLayout(null);
        setOpaque(false);
        initButtons();
    }

    private void initButtons() {
        ImageIcon buttonIcon = GliphyUtilities.loadIcon("/ui/button.png", 300, 100);
        ImageIcon pressedButtonIcon = GliphyUtilities.loadIcon("/ui/pressed-button.png", 300, 100);

        backButton = new IconButton(
                languageManager.getString("back"),
                buttonIcon,
                pressedButtonIcon);

        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        backButton.setBounds(1550, 940, 300, 100);
        backButton.addActionListener(e -> root.showMainMenu());
        add(backButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int y = 100;

        g2.setFont(new Font("SansSerif", Font.BOLD, 60));
        drawString(g2, languageManager.getString("credits").toUpperCase(), width, y);

        y += 420;
        g2.setFont(new Font("SansSerif", Font.BOLD, 40));
        drawString(g2, languageManager.getString("programmer"), width, y);

        y += 50;
        g2.setFont(new Font("SansSerif", Font.PLAIN, 35));
        drawString(g2, "Мерт Ниязиев", width, y);
    }

    private void drawString(Graphics2D g2, String text, int width, int y) {
        FontMetrics metrics = g2.getFontMetrics();
        int x = (width - metrics.stringWidth(text)) / 2;
        g2.drawString(text, x, y);
    }
}