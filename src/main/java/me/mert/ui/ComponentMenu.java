package me.mert.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import me.mert.core.ComponentType;


public class ComponentMenu extends JPanel {

    public ComponentMenu(GamePanel gamePanel) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        // Create buttons for components
        JButton collectorButton = new JButton("Collector");
        JButton conveyorButton = new JButton("Conveyor");
        JButton hubButton = new JButton("Hub");

        // Optional: Add some spacing between buttons
        collectorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        conveyorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        hubButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(collectorButton);
        add(conveyorButton);
        add(hubButton);

        // Add action listeners
        collectorButton.addActionListener(e -> gamePanel.setSelectedComponent(ComponentType.COLLECTOR));
        conveyorButton.addActionListener(e -> gamePanel.setSelectedComponent(ComponentType.CONVEYOR));
        hubButton.addActionListener(e -> gamePanel.setSelectedComponent(ComponentType.HUB));
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
