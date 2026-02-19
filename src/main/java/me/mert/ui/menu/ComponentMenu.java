package me.mert.ui.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import me.mert.core.enums.ComponentType;
import me.mert.ui.panel.GamePanel;
import me.mert.ui.widgets.ComponentSlot;

public class ComponentMenu extends JPanel {
    public ComponentMenu(GamePanel gamePanel) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        setBackground(new Color(236, 236, 236));

        // Create buttons for components
        addButton(gamePanel, ComponentType.COLLECTOR, false, false);
        addButton(gamePanel, ComponentType.CONVEYOR, false, false);
        addButton(gamePanel, ComponentType.HUB, false, false);
        addButton(gamePanel, ComponentType.CUTTER, false, false);
        addButton(gamePanel, ComponentType.STACKER, false, false);
        addButton(gamePanel, ComponentType.MERGER, false, false);
        addButton(gamePanel, ComponentType.MERGER, true, false);
        addButton(gamePanel, ComponentType.ROTATER, false, true);
    }

    private void addButton(GamePanel gamePanel, ComponentType ct, boolean variant, boolean locked) {
        ComponentSlot button = new ComponentSlot(
                getScaledImage(ct, variant),
                locked);
        button.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        setAction(gamePanel, button, ComponentType.COLLECTOR, variant);

        add(button);

    }

    private void setAction(GamePanel panel, ComponentSlot c, ComponentType ct, boolean v) {
        c.addActionListener(e -> {
            if (!c.getLocked()) {
                panel.setSelectedType(ct, v, null);
            } else {
                // TODO: implement this when currancy is done
            }
        });
    }

    private ImageIcon getScaledImage(ComponentType ct, boolean variant) {
        ImageIcon icon;
        if (ct != ComponentType.CUTTER && ct != ComponentType.STACKER) {
            icon = new ImageIcon(ComponentType.createComponent(ct, 0, 0, variant, null).getImage());
        } else {
            icon = new ImageIcon(
                    ComponentMenu.class.getResource(
                            "/icons/" + ct.toString().toLowerCase() + "-icon.png"));
        }
        Image sImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        return new ImageIcon(sImage);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        g2.dispose();
    }

}
