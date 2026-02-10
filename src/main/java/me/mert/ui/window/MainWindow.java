package me.mert.ui.window;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import me.mert.ui.Camera;
import me.mert.ui.GameRenderer;
import me.mert.ui.panel.GamePanel;
import me.mert.ui.panel.MainMenu;
import me.mert.world.World;

// container holding UI and game panels
public class MainWindow extends JFrame {
    private final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

    private CardLayout cl;
    private JPanel root;

    GamePanel gamePanel;
    MainMenu mainMenu;

    Timer renderTimer;
    Timer updateTimer;

    private final int FPS = 120;

    public MainWindow(boolean startGame) {
        setTitle("Gliphy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(screenDimension);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        // setFocusable(true);
        // setLayout(null);

        cl = new CardLayout();
        root = new JPanel(cl);

        createScreens();
        setContentPane(root);

        // TEMP: remove before release
        if (startGame) {
            showWorld();
        } else {
            showMainMenu();
        }

        setVisible(true);

    }

    private void createScreens() {
        mainMenu = new MainMenu(this);
        Camera camera = new Camera(0, 0);
        World world = new World();
        GameRenderer renderer = new GameRenderer(camera, world);

        gamePanel = new GamePanel(camera, world, renderer);

        root.add(mainMenu, "menu");
        root.add(gamePanel, "world");
    }

    public void showMainMenu() {
        stopTimer();
        cl.show(root, "menu");
    }

    public void showWorld() {
        startTimer();
        cl.show(root, "world");
    }

    private void startTimer() {
        if (renderTimer == null) {
            renderTimer = new Timer(1000 / FPS, e -> gamePanel.repaint());
            updateTimer = new Timer(1000, e -> gamePanel.getWorld().updateComponents());
        }
        renderTimer.start();
        updateTimer.start();
    }

    private void stopTimer() {
        if (renderTimer != null) {
            renderTimer.stop();
            updateTimer.stop();
        }
    }

}
