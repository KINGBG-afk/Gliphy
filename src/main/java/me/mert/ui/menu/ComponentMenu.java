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
import me.mert.ui.widgets.IconButton;

public class ComponentMenu extends JPanel {

    public ComponentMenu(GamePanel gamePanel) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        // Create buttons for components
        IconButton collectorButton = new IconButton("",
                getScaledImage(ComponentType.COLLECTOR),
                null,
                new Color(201, 201, 201),
                true);
        IconButton conveyorButton = new IconButton("",
                getScaledImage(ComponentType.CONVEYOR),
                null,
                new Color(201, 201, 201),
                true);
        IconButton hubButton = new IconButton("",
                getScaledImage(ComponentType.HUB),
                null,
                new Color(201, 201, 201),
                true);
        IconButton cutterButton = new IconButton("",
                getScaledImage(ComponentType.CUTTER),
                null,
                new Color(201, 201, 201),
                true);
        IconButton stackerButton = new IconButton("",
                getScaledImage(ComponentType.STACKER),
                null,
                new Color(201, 201, 201),
                true);

        collectorButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
        conveyorButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
        hubButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
        cutterButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
        stackerButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));

        collectorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        conveyorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        hubButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cutterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        stackerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(collectorButton);
        add(conveyorButton);
        add(hubButton);
        add(cutterButton);
        add(stackerButton);

        // Add action listeners
        collectorButton.addActionListener(e -> gamePanel.setSelectedType(ComponentType.COLLECTOR));
        conveyorButton.addActionListener(e -> gamePanel.setSelectedType(ComponentType.CONVEYOR));
        hubButton.addActionListener(e -> gamePanel.setSelectedType(ComponentType.HUB));
        cutterButton.addActionListener(e -> gamePanel.setSelectedType(ComponentType.CUTTER));
        stackerButton.addActionListener(e -> gamePanel.setSelectedType(ComponentType.STACKER));
    }

    private ImageIcon getScaledImage(ComponentType ct) {
        ImageIcon icon;
        if (ct != ComponentType.CUTTER && ct != ComponentType.STACKER) {
            icon = new ImageIcon(ComponentType.createComponent(ct, 0, 0).getImage());
        } else {
            System.out.println("/icons/" + ct.toString().toLowerCase() + ".png");
            icon = new ImageIcon(
                    ComponentMenu.class.getResource("/icons/" + ct.toString().toLowerCase() + "-icon.png"));
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
