package me.mert.ui.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import me.mert.core.GliphyUtilities;

// god forbid for this awful class
public class ComponentSlot extends JButton {
    boolean locked;
    ImageIcon lock = GliphyUtilities.loadIcon("/icons/lock.png", 40, 40);

    public ComponentSlot(ImageIcon icon, boolean locked) {

        super(icon);
        this.locked = locked;
        Color bgColor = new Color(0, 0, 0, 0);

        setFont(new Font("Segoe UI", Font.PLAIN, 14)); // idk why this is here
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFocusable(false);
        setBackground(bgColor);
        setForeground(Color.BLACK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(201, 201, 201));
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
                setBackground(new Color(201, 201, 201));
            }
        });

    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    public boolean getLocked() {
        return locked;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        // Draw main icon
        if (getIcon() != null) {
            int iconWidth = getIcon().getIconWidth();
            int iconHeight = getIcon().getIconHeight();
            int iconX = (getWidth() - iconWidth) / 2;
            int iconY = (getHeight() - iconHeight) / 2;

            getIcon().paintIcon(this, g2, iconX, iconY);

            if (locked) {
                // Semi-transparent overlay (over the main icon)
                g2.setColor(new Color(255, 255, 255, 120));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Draw lock icon centered
                int lockW = lock.getIconWidth();
                int lockH = lock.getIconHeight();
                int lockX = (getWidth() - lockW) / 2;
                int lockY = (getHeight() - lockH) / 2;

                lock.paintIcon(this, g2, lockX, lockY);
            }
        }

        g2.dispose();
    }

    // this empty so it wont draw the default border
    @Override
    protected void paintBorder(Graphics g) {
    }

}
