package me.mert.ui.window;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import me.mert.game.CurrencyManager;
import me.mert.game.Upgrade;
import me.mert.game.UpgradeManager;
import me.mert.ui.Camera;
import me.mert.ui.GameRenderer;
import me.mert.ui.panel.GamePanel;
import me.mert.ui.panel.MainMenu;
import me.mert.ui.panel.WorldSelectionMenu;
import me.mert.world.World;

// container holding UI and game panels
public class MainWindow extends JFrame {
    private final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

    private CardLayout cl;
    private JPanel root;

    GamePanel gamePanel;
    MainMenu mainMenu;
    WorldSelectionMenu worldMenu;

    Timer renderTimer;
    Timer updateTimer;

    private final int FPS = 120;
    private final int BASE_UPDATE_DELAY = 1000;

    public MainWindow() {
        setTitle("Gliphy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setResizable(false);

        String os = System.getProperty("os.name").toLowerCase();

        if (!os.contains("linux")) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            setSize(screenDimension);
        }

        cl = new CardLayout();
        root = new JPanel(cl);

        createScreens();
        setContentPane(root);
        showMainMenu();

    }

    public void upgradeSpeed() {
        Upgrade up = UpgradeManager.getInstance().getUpgrade("speed");
        CurrencyManager cmgr = CurrencyManager.getInstance();

        if (cmgr.canAfford(up.getCost())) {
            up.levelUp();
            updateTimer.setDelay((int) (BASE_UPDATE_DELAY - (up.getLevel() * 50)));
            System.out.println((int) (BASE_UPDATE_DELAY - (up.getLevel() * 50)));
        }
    }

    private void createScreens() {
        mainMenu = new MainMenu(this);
        gamePanel = new GamePanel();
        worldMenu = new WorldSelectionMenu(this);

        root.add(mainMenu, "menu");
        root.add(gamePanel, "game");
        root.add(worldMenu, "worlds");
    }

    public void showMainMenu() {
        stopTimer();
        cl.show(root, "menu");
    }

    public void showGame(Camera c, World w) {
        GameRenderer renderer = new GameRenderer(c, w);
        gamePanel.init(c, w, renderer, this);
        startTimer();
        cl.show(root, "game");
    }

    public void showGame(String name) {
        Camera camera = new Camera(0, 0);
        World world = new World(name);
        GameRenderer renderer = new GameRenderer(camera, world);
        gamePanel.init(camera, world, renderer, this);
        startTimer();
        cl.show(root, "game");
    }

    public void showWorldMenu() {
        cl.show(root, "worlds");
    }

    private void startTimer() {
        if (renderTimer == null) {
            renderTimer = new Timer(1000 / FPS, e -> gamePanel.repaint());
            updateTimer = new Timer(BASE_UPDATE_DELAY, e -> gamePanel.getWorld().updateComponents());
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
