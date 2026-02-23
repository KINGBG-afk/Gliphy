package me.mert.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import me.mert.components.Component;
import me.mert.core.enums.ComponentType;
import me.mert.game.CurrencyManager;
import me.mert.game.LevelManager;
import me.mert.game.UpgradeManager;
import me.mert.game.save.SaveData;
import me.mert.game.save.SaveManager;
import me.mert.ui.Camera;
import me.mert.ui.menu.CreateWorldDialog;
import me.mert.ui.widgets.RoundedButton;
import me.mert.ui.widgets.ScrollBar;
import me.mert.ui.widgets.ScrollableElement;
import me.mert.ui.window.MainWindow;
import me.mert.world.Tile;
import me.mert.world.World;

public class WorldSelectionMenu extends JPanel {
    private final MainWindow root;

    RoundedPanel mainPanel;

    JScrollPane scrollPane;
    JPanel scrollContent;

    public WorldSelectionMenu(MainWindow root) {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        this.root = root;

        mainPanel = new RoundedPanel(20);
        mainPanel.setBackground(new Color(180, 180, 180));
        mainPanel.setPreferredSize(new Dimension(1500, 1000));
        mainPanel.setLayout(new BorderLayout());

        createScrollArea();
        createButtons();

        add(mainPanel);
    }

    // there are probably better solutions for this but it works
    private void createScrollArea() {
        scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(new Color(150, 150, 150));

        // the wrapper and gbc is just so the elements are centered
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(150, 150, 150));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        wrapper.add(scrollContent, gbc);
        scrollContent.add(Box.createVerticalStrut(20));

        UpgradeManager upgradeManager = UpgradeManager.getInstance();
        LevelManager levelManager = LevelManager.getInstance();

        for (SaveData save : SaveManager.loadAllSaves()) {
            ScrollableElement element = new ScrollableElement(20, save.name, String.valueOf(save.coins),
                    String.valueOf(save.level));
            element.setPreferredSize(new Dimension(1470, 130));
            element.setBackground(new Color(215, 215, 215));
            element.setPlayAction(e -> {
                // set level
                levelManager.setLevel(save.level);

                // set coinds
                CurrencyManager.getInstance().setCoins(save.coins);

                // generate world with name and seed
                World w = new World(save.seed, save.name);
                w.setComponents(save.components);
                System.out.println("Loading components");
                for (Component c : save.components) {
                    Tile tile = w.getTile(c.i, c.j);
                    if (tile != null) {
                        tile.setComponent(c);
                        
                        if (c.type != ComponentType.MERGER) {
                            c.loadImage(c.type);
                        } else {
                            // at this point just load both
                            c.loadImage(c.type + "merger-right");
                            c.loadImage(c.type + "merger-left");
                        }
                    }
                }

                // set unlocked components
                upgradeManager.setUnlockedComponents(save.unlockedComponents);

                // set upgrades
                for (Map.Entry<String, Integer> entry : save.upgradeLevels.entrySet()) {
                    String k = entry.getKey();
                    Integer v = entry.getValue();
                    upgradeManager.getUpgrade(k).setLevel(v);
                }

                // camera positions
                root.showGame(new Camera(save.cameraX, save.cameraY), w);
            });
            scrollContent.add(element);
            scrollContent.add(Box.createVerticalStrut(20));
        }

        scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(new ScrollBar());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createButtons() {
        RoundedButton backButton = new RoundedButton("BACK",
                new Color(213, 213, 213),
                new Color(194, 194, 194));
        backButton.setFont(new Font("SansSerif", Font.BOLD, 40));
        backButton.setPreferredSize(new Dimension(700, 65));
        backButton.addActionListener(e -> root.showMainMenu());

        RoundedButton newButton = new RoundedButton("NEW WORLD",
                new Color(213, 213, 213),
                new Color(194, 194, 194));
        newButton.setFont(new Font("SansSerif", Font.BOLD, 40));
        newButton.setPreferredSize(new Dimension(700, 65));
        newButton.addActionListener(e -> {
            CreateWorldDialog dialog = new CreateWorldDialog(root);
            dialog.setVisible(true);

            String name = dialog.getWorldName();
            if (name != null && !name.isEmpty()) {
                System.out.println("Created world: " + name);
                root.showGame(name);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        bottomPanel.add(newButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

}
