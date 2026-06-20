package me.mert.ui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import me.mert.components.Component;
import me.mert.core.GliphyUtilities;
import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.core.enums.LayerType;
import me.mert.core.logger.Logger;
import me.mert.game.CurrencyManager;
import me.mert.game.LanguageManager;
import me.mert.game.LevelManager;
import me.mert.game.Upgrade;
import me.mert.game.UpgradeManager;
import me.mert.game.save.SaveData;
import me.mert.game.save.SaveManager;
import me.mert.input.KeyboardActions;
import me.mert.input.MouseInput;
import me.mert.ui.Camera;
import me.mert.ui.GameRenderer;
import me.mert.ui.menu.ComponentMenu;
import me.mert.ui.menu.UpgradeMenu;
import me.mert.ui.widgets.IconButton;
import me.mert.ui.window.MainWindow;
import me.mert.world.World;

public class GamePanel extends JPanel {
    private final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

    private LanguageManager languageManager;
    private World world;
    private GameRenderer gameRenderer;
    private GameUIPanel uiPanel;
    private Camera camera;

    // just so this class can access the updateTimer
    private MainWindow root;

    private ComponentType selectedType = ComponentType.COLLECTOR;
    private Direction selectedDirection = Direction.NORTH;
    private Component selectedComponent = ComponentType.createComponent(
            selectedType,
            selectedDirection,
            0, 0,
            false,
            null);

    private boolean variant = false;

    // this is so the ui can generate without generating the whole world
    public GamePanel(LanguageManager languageManager) {
        // might as well load it
        this.languageManager = languageManager;
    }

    public GamePanel(Camera camera, World world, GameRenderer gameRenderer, MainWindow mainWindow) {
        this.world = world;
        this.gameRenderer = gameRenderer;
        this.root = mainWindow;
        this.camera = camera;

        setLayout(null);
        setBackground(Color.WHITE);
        setFocusable(true);

        createUI(camera, gameRenderer);
        setVisible(true);
    }

    private void createUI(Camera camera, GameRenderer gameRenderer) {
        JLayeredPane layers = new JLayeredPane();
        layers.setBounds(0, 0, screenDimension.width, screenDimension.height);
        add(layers);

        uiPanel = new GameUIPanel();
        uiPanel.setBounds(0, 0, screenDimension.width, screenDimension.height);
        layers.add(uiPanel, JLayeredPane.PALETTE_LAYER);

        createComponentMenu();
        createUpgradeUI();
        createExitButton();

        // mouse
        MouseInput mouseInput = new MouseInput(camera, this, gameRenderer);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        addMouseWheelListener(mouseInput);

        // keyboard
        KeyboardActions kActions = new KeyboardActions(camera);
        kActions.register(this);
    }

    public void init(Camera camera, World world, GameRenderer renderer, MainWindow mainWindow) {
        this.world = world;
        this.gameRenderer = renderer;
        this.root = mainWindow;
        this.camera = camera;
        createUI(camera, gameRenderer);
    }

    private void createComponentMenu() {
        ComponentMenu menu = new ComponentMenu(this, languageManager);
        menu.setSize(820, 80);
        menu.setLocation(
                (uiPanel.getWidth() - menu.getWidth()) / 2,
                uiPanel.getHeight() - menu.getHeight() - 20);
        uiPanel.add(menu);
    }

    private void createExitButton() {
        ImageIcon icon = GliphyUtilities.loadIcon("/icons/close.png", 60, 60);
        IconButton xButton = new IconButton("",
                icon, icon);

        xButton.setBounds(1800, 20, 60, 60);
        xButton.addActionListener(e -> {
            saveWorld();
            showWorldMenu();

        });
        uiPanel.add(xButton);
    }

    private void createUpgradeUI() {
        UpgradeMenu menu = new UpgradeMenu(this, languageManager);
        menu.setSize(400, 800);
        menu.setLocation(
                (uiPanel.getWidth() - menu.getWidth()) - 20,
                (uiPanel.getHeight() - menu.getHeight()) / 2);
        menu.setVisible(false);

        ImageIcon starIcon = GliphyUtilities.loadIcon("/icons/star.png", 70, 70);
        IconButton upgradeButton = new IconButton("",
                starIcon,
                starIcon);
        upgradeButton.setBounds(
                (uiPanel.getWidth() + 840) / 2,
                uiPanel.getHeight() - 95,
                70, 70);
        upgradeButton.addActionListener(e -> {
            menu.setVisible(!menu.isVisible());
            uiPanel.revalidate(); // not that there is a layout but still
            uiPanel.repaint();
        });
        uiPanel.add(upgradeButton);
        uiPanel.add(menu);
    }

    public void setSelectedType(ComponentType c, boolean v, LayerType r) {
        selectedType = c;
        variant = v;
        selectedComponent = ComponentType.createComponent(selectedType, selectedDirection, 0, 0, v, r);
    }

    public void rotateDirection() {
        selectedDirection = selectedDirection.right();
        selectedComponent = ComponentType.createComponent(selectedType, selectedDirection, 0, 0, variant, null);
    }

    public void rotateOtherDirection() {
        selectedDirection = selectedDirection.left();
        selectedComponent = ComponentType.createComponent(selectedType, selectedDirection, 0, 0, variant, null);

    }

    public void setChunkLocation(int x, int y) {
        world.updateCenter(x, y);
    }

    public void placeSelectedComponentAt(int i, int j) {
        world.placeComponent(i, j, selectedType, selectedDirection, variant);
    }

    public void removeComponentAt(int i, int j) {
        world.removeComponent(i, j);
    }

    // it gets the job done
    public World getWorld() {
        return world;
    }

    public void upgradeSpeed() {
        root.upgradeSpeed();
    }

    public void saveWorld() {
        SaveData data = new SaveData();
        LevelManager levelManager = LevelManager.getInstance();

        Logger.info("Saving " + world.worldName);
        data.name = world.worldName;
        data.level = levelManager.getLevel();
        data.levelProgress = levelManager.getStored();
        data.seed = world.getSeed();
        data.coins = CurrencyManager.getInstance().getCoins();

        data.cameraX = camera.x;
        data.cameraY = camera.y;

        data.components = world.getComponents();

        data.upgradeLevels = new HashMap<>();
        for (Upgrade u : UpgradeManager.getInstance().getAllUpgrades()) {
            data.upgradeLevels.put(u.getId(), u.getLevel());
        }

        data.unlockedComponents = UpgradeManager.getInstance().getUnlockedComponents();

        SaveManager.save(data);
    }

    public void showWorldMenu() {
        // unload everything when exiting the world
        if (gameRenderer != null) {
            gameRenderer = null;
        }

        if (camera != null) {
            camera = null;
        }

        if (world != null) {
            world.resetChunks();
            world = null;
        }

        for (MouseListener ml : getMouseListeners()) {
            removeMouseListener(ml);
        }

        for (MouseMotionListener ml : getMouseMotionListeners()) {
            removeMouseMotionListener(ml);
        }

        for (MouseWheelListener ml : getMouseWheelListeners()) {
            removeMouseWheelListener(ml);
        }

        getInputMap().clear();
        getActionMap().clear();

        root.showWorldMenu();
    }

    // not that you should move the window but
    // just future proofing
    @Override
    public void doLayout() {
        super.doLayout();
        if (getComponentCount() > 0) {
            getComponent(0).setBounds(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int sWidth = getWidth();
        int sHeight = getHeight();
        gameRenderer.drawGrid((Graphics2D) g, sWidth, sHeight);
        gameRenderer.drawTiles((Graphics2D) g, sWidth, sHeight);
        gameRenderer.drawComponents(g, sWidth, sHeight);
        gameRenderer.drawPreviewComponent(g, selectedComponent);
        gameRenderer.drawVignette((Graphics2D) g, sWidth, sHeight);
        gameRenderer.drawGoal(g, sWidth, sHeight);
        gameRenderer.drawCoins(g, sWidth, sHeight);

        // gameRenderer.debugDraw(g, sWidth, sHeight);
    }
}
