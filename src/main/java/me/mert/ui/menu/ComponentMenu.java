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
import me.mert.game.CurrencyManager;
import me.mert.game.UpgradeManager;
import me.mert.ui.panel.GamePanel;
import me.mert.ui.widgets.ComponentSlot;

public class ComponentMenu extends JPanel {
    public ComponentMenu(GamePanel gamePanel) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        setBackground(new Color(236, 236, 236));

        // Create buttons for components
        addButton(gamePanel, ComponentType.COLLECTOR, false);
        addButton(gamePanel, ComponentType.CONVEYOR, false);
        addButton(gamePanel, ComponentType.HUB, false);
        addButton(gamePanel, ComponentType.CUTTER, false);
        addButton(gamePanel, ComponentType.STACKER, false);
        addButton(gamePanel, ComponentType.MERGER, false);
        addButton(gamePanel, ComponentType.MERGER, true);
        addButton(gamePanel, ComponentType.ROTATER, false);
    }

    private void addButton(GamePanel gamePanel, ComponentType ct, boolean variant) {
        ComponentSlot button = new ComponentSlot(
                getScaledImage(ct, variant), ct.toString().toLowerCase());
        button.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        setAction(gamePanel, button, ct, variant);

        if (button.isLocked()) {
            button.setToolTipText(
                    "<html><span style='font-size:14px;'>Costs 100 coins</span></html>");
        }

        add(button);
    }

    private void setAction(GamePanel panel, ComponentSlot c, ComponentType ct, boolean v) {
        CurrencyManager cmgr = CurrencyManager.getInstance();
        c.addActionListener(e -> {
            if (!c.isLocked()) {
                panel.setSelectedType(ct, v, null);
            } else {
                if (cmgr.canAfford(100)) {
                    cmgr.spend(100);
                    c.setToolTipText(null);
                    UpgradeManager.getInstance().unlockComponent(ct.toString().toLowerCase());
                }
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
