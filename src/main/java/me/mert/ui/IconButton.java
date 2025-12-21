package me.mert.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class IconButton extends JButton {
    Color bgColor;
    Color highlight;

    IconButton(ImageIcon icon) {
        
        super(icon);

        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bgColor = new Color(0, 0, 0, 0);
        highlight = new Color(255);

        setContentAreaFilled(false);
        setFocusPainted(false);
        setFocusable(false);
        setBackground(bgColor);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(highlight);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(bgColor);
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // im not amswering any quesntions as to why is this here
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(bgColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(highlight);
            }
        });

    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // rendering algorithm
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        if (getIcon() != null) {
            int iconWidth = getIcon().getIconWidth();
            int iconHeight = getIcon().getIconHeight();
            int iconx = (getWidth() - iconWidth) / 2;
            int icony = (getHeight() - iconHeight) / 2;
            getIcon().paintIcon(this, g2, iconx, icony);
        }

        // remove referances
        g2.dispose();
    }

    // this empty so it wont draw the default border
    @Override
    protected void paintBorder(Graphics g) {
    }

}
