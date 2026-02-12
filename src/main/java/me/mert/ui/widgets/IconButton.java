package me.mert.ui.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

// god forbid for this awful class
public class IconButton extends JButton {

    public IconButton(String text,
            ImageIcon icon,
            Color bgColo,
            Color highlight,
            boolean transparent) {

        super(text, icon);
        Color bgColor;

        setFont(new Font("Segoe UI", Font.PLAIN, 14)); // idk why this is here

        if (transparent) {
            bgColor = new Color(0, 0, 0, 0);
        } else {
            bgColor = bgColo;
        }

        setContentAreaFilled(false);
        setFocusPainted(false);
        setFocusable(false);
        setBackground(bgColor);
        setForeground(Color.BLACK);

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
                // im not answering any quesntions as to why is this here
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(highlight);
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

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

        if (!getText().isEmpty()) {
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int textWidht = fm.stringWidth(getText());
            int textHeight = fm.getAscent();

            // for the buttons in the explorer panel
            int textX;
            textX = switch (getHorizontalAlignment()) {
                case SwingConstants.LEFT -> 10;
                case SwingConstants.RIGHT -> getWidth() - textWidht - 10;
                default -> (getWidth() - textWidht) / 2;
            };

            g2.drawString(getText(), textX, (getHeight() + textHeight) / 2 - 2);
        }

        // remove referances
        g2.dispose();
    }

    // this empty so it wont draw the default border
    @Override
    protected void paintBorder(Graphics g) {
    }

}
