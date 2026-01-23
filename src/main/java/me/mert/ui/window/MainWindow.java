package me.mert.ui.window;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.Timer;

import me.mert.ui.Camera;
import me.mert.ui.GameRenderer;
import me.mert.ui.menu.ComponentMenu;
import me.mert.ui.panel.GamePanel;
import me.mert.ui.panel.GameUIPanel;
import me.mert.world.World;

// container holding UI and game panels
public class MainWindow extends JFrame {
    private final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
    GamePanel gamePanel;
    GameUIPanel uiPanel;
    private final int FPS = 120;

    public MainWindow() {
        setTitle("Gliphy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(screenDimension);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(true);
        setLayout(null);

        Camera camera = new Camera(0, 0);
        World world = new World();
        GameRenderer renderer = new GameRenderer(camera, world);

        gamePanel = new GamePanel(camera, world, renderer);
        gamePanel.setBounds(0, 0, getWidth(), getHeight());

        uiPanel = new GameUIPanel();
        uiPanel.setBounds(0, 0, getWidth(), getHeight());

        createMenu(uiPanel);

        JLayeredPane layers = new JLayeredPane();
        layers.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
        layers.add(uiPanel, JLayeredPane.PALETTE_LAYER);

        setContentPane(layers);

        Timer renderTimer = new Timer(1000 / FPS, e -> gamePanel.repaint());
        renderTimer.start();

        Timer updateTimer = new Timer(1000, e -> world.updateComponents());
        updateTimer.start();

    }

    private void createMenu(GameUIPanel uiPanel) {
        ComponentMenu menu = new ComponentMenu(gamePanel);
        menu.setSize(400, 70);
        menu.setLocation(
                (getWidth() - menu.getWidth()) / 2,
                getHeight() - menu.getHeight() - 20);

        uiPanel.add(menu);
    }

}
