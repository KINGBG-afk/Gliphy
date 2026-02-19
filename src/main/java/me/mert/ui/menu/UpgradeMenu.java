package me.mert.ui.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import me.mert.ui.panel.GamePanel;
import me.mert.ui.panel.RoundedPanel;
import me.mert.ui.widgets.RoundedButton;

public class UpgradeMenu extends RoundedPanel {
    public UpgradeMenu(GamePanel gamePanel) {
        setLayout(null);
        setOpaque(false);
        setBackground(new Color(236, 236, 236));

        RoundedButton machineSpeed = new RoundedButton("UPGRADE",
                new Color(213, 213, 213),
                new Color(194, 194, 194));
        machineSpeed.setBounds(220, 55, 80, 40);
        machineSpeed.setBackground(new Color(213, 213, 213));
        machineSpeed.addActionListener(e -> gamePanel.upgradeSpeed());
        add(machineSpeed);
    }

    private void drawText(Graphics2D g2d, String text, int x, int y) {
        String[] lines = text.split("\n");

        FontMetrics fm = g2d.getFontMetrics();
        int lineHeight = fm.getHeight();

        for (String line : lines) {
            g2d.drawString(line, x, y);
            y += lineHeight;
        }
    }

    // god forgive me but there is 2 days left for the deadline
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.setFont(new Font("sans-serif", Font.PLAIN, 19));

        drawText((Graphics2D) g, "Improve the speed\nof machines", 20, 70);
    }

}
