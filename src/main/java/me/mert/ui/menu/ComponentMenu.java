package me.mert.ui.menu;

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
import me.mert.ui.widgets.IconButton;

public class ComponentMenu extends JPanel {

    public ComponentMenu(GamePanel gamePanel) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        // Create buttons for components
        IconButton collectorButton = new IconButton(getScaledImage(ComponentType.COLLECTOR));
        IconButton conveyorButton = new IconButton(getScaledImage(ComponentType.CONVEYOR));
        IconButton hubButton = new IconButton(getScaledImage(ComponentType.HUB));

        collectorButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
        conveyorButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
        hubButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));

        collectorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        conveyorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        hubButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(collectorButton);
        add(conveyorButton);
        add(hubButton);

        // Add action listeners
        collectorButton.addActionListener(e -> gamePanel.setSelectedType(ComponentType.COLLECTOR));
        conveyorButton.addActionListener(e -> gamePanel.setSelectedType(ComponentType.CONVEYOR));
        hubButton.addActionListener(e -> gamePanel.setSelectedType(ComponentType.HUB));
    }

    private ImageIcon getScaledImage(ComponentType ct) {
        ImageIcon icon = new ImageIcon(ComponentType.createComponent(ct, 0, 0).getImage());
        Image sImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        return new ImageIcon(sImage);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();
    }

}
