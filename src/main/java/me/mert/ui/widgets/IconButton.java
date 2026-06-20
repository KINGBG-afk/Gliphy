package me.mert.ui.widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

// god forbid for this awful class
public class IconButton extends JButton {

    private ImageIcon pressedImg;
    private ImageIcon highlightIcon;

    // some buttons don't have hover state so that's why this is here
    public IconButton(String text, ImageIcon icon, ImageIcon pressedImg) {
        this(text, icon, pressedImg, null);
    }

    public IconButton(String text,
            ImageIcon icon,
            ImageIcon pressedIcon,
            ImageIcon highlightIcon) {

        super(text, icon);

        this.pressedImg = pressedIcon;
        this.highlightIcon = highlightIcon;

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFocusable(false);
        setBorderPainted(false);
        setRolloverEnabled(true);
    }

    public void setHighlightIcon(ImageIcon img) {
        highlightIcon = img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        ButtonModel mod = getModel();
        ImageIcon iconToDraw = (ImageIcon) getIcon();

        // pressed
        if (mod.isPressed() && pressedImg != null) {
            iconToDraw = pressedImg;
        }

        // hover
        boolean hovering = mod.isRollover() && !mod.isPressed();
        if (hovering && highlightIcon != null) {
            iconToDraw = highlightIcon;
        }

        // draw icon
        if (iconToDraw != null) {
            int iw = iconToDraw.getIconWidth();
            int ih = iconToDraw.getIconHeight();
            // (i use arch btw)
            int ix = (getWidth() - iw) / 2;
            int iy = (getHeight() - ih) / 2;

            iconToDraw.paintIcon(this, g2, ix, iy);
        }

        if (hovering && highlightIcon == null) {
            g2.setColor(new Color(255, 255, 255, 50));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // draw text
        if (!getText().isEmpty()) {
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textHeight = fm.getAscent();

            int textX = (getWidth() - textWidth) / 2;
            int textY = (getHeight() + textHeight) / 2 - 7;

            g2.drawString(getText(), textX, textY);
        }

        g2.dispose();
    }
}
