package me.mert.ui.widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class RoundedButton extends JButton {

    public RoundedButton(String text, Color bgColor, Color highlight) {
        super(text);
        setBackground(bgColor);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

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
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // rendering algorithm
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        if (!getText().isEmpty()) {
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int textWidht = fm.stringWidth(getText());
            int textHeight = fm.getAscent();

            g2.drawString(getText(),
                    (getWidth() - textWidht) / 2,
                    (getHeight() + textHeight) / 2 - 2);
        }

        g2.dispose();
    }

    // this empty so it wont draw the default border
    @Override
    protected void paintBorder(Graphics g) {
    }

}
