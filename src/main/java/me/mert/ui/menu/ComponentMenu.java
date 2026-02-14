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
import me.mert.ui.widgets.ComponentSlot;

public class ComponentMenu extends JPanel {
        public ComponentMenu(GamePanel gamePanel) {
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                setOpaque(false);

                // Create buttons for components
                ComponentSlot collectorButton = new ComponentSlot(getScaledImage(
                                ComponentType.COLLECTOR, false),
                                false);

                ComponentSlot conveyorButton = new ComponentSlot(
                                getScaledImage(ComponentType.CONVEYOR, false),
                                false);
                ComponentSlot hubButton = new ComponentSlot(
                                getScaledImage(ComponentType.HUB, false),
                                false);
                ComponentSlot cutterButton = new ComponentSlot(
                                getScaledImage(ComponentType.CUTTER, false),
                                false);
                ComponentSlot stackerButton = new ComponentSlot(
                                getScaledImage(ComponentType.STACKER, false),
                                false);
                ComponentSlot lMergerButton = new ComponentSlot(
                                getScaledImage(ComponentType.MERGER, false),
                                false);
                ComponentSlot rMergerButton = new ComponentSlot(
                                getScaledImage(ComponentType.MERGER, true),
                                true);

                collectorButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
                conveyorButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
                hubButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
                cutterButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
                stackerButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
                lMergerButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
                rMergerButton.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));

                collectorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                conveyorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                hubButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                cutterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                stackerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                lMergerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                rMergerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                add(collectorButton);
                add(conveyorButton);
                add(hubButton);
                add(cutterButton);
                add(stackerButton);
                add(lMergerButton);
                add(rMergerButton);

                setAction(gamePanel, collectorButton, ComponentType.COLLECTOR, false);
                setAction(gamePanel, conveyorButton, ComponentType.CONVEYOR, false);
                setAction(gamePanel, hubButton, ComponentType.HUB, false);
                setAction(gamePanel, cutterButton, ComponentType.CUTTER, false);
                setAction(gamePanel, stackerButton, ComponentType.STACKER, false);
                setAction(gamePanel, lMergerButton, ComponentType.MERGER, false);
                setAction(gamePanel, rMergerButton, ComponentType.MERGER, true);
        }

        private void setAction(GamePanel panel, ComponentSlot c, ComponentType ct, boolean v) {
                c.addActionListener(e -> {
                        if (!c.getLocked()) {
                                panel.setSelectedType(ct, v);
                        } else {
                                // TODO: implement this when currancy is done
                        }
                });
        }

        private ImageIcon getScaledImage(ComponentType ct, boolean variant) {
                ImageIcon icon;
                if (ct != ComponentType.CUTTER && ct != ComponentType.STACKER) {
                        icon = new ImageIcon(ComponentType.createComponent(ct, 0, 0, variant).getImage());
                } else {
                        System.out.println("/icons/" + ct.toString().toLowerCase() + ".png");
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
