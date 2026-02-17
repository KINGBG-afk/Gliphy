package me.mert.ui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import me.mert.components.Component;
import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.core.enums.LayerType;
import me.mert.game.LevelManager;
import me.mert.input.KeyboardActions;
import me.mert.input.MouseInput;
import me.mert.ui.Camera;
import me.mert.ui.GameRenderer;
import me.mert.ui.menu.ComponentMenu;
import me.mert.world.World;

public class GamePanel extends JPanel {
    private final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

    // i swaer at some point imma run into performance issues
    private final World world;
    private final GameRenderer gameRenderer;
    private final GameUIPanel uiPanel;

    private ComponentType selectedType = ComponentType.COLLECTOR;
    private Direction selectedDirection = Direction.NORTH;
    private Component selectedComponent = ComponentType.createComponent(
            selectedType,
            selectedDirection,
            0, 0,
            false,
            null);

    private boolean variant = false;

    // lesson relearned - do not use getWIdth and getHeight in the constructor :)
    public GamePanel(Camera camera, World world, GameRenderer gameRenderer) {
        this.world = world;
        this.gameRenderer = gameRenderer;

        setLayout(null);
        setBackground(Color.WHITE);
        setFocusable(true);

        JLayeredPane layers = new JLayeredPane();
        layers.setBounds(0, 0, screenDimension.width, screenDimension.height);
        add(layers);

        uiPanel = new GameUIPanel();
        uiPanel.setBounds(0, 0, screenDimension.width, screenDimension.height);
        layers.add(uiPanel, JLayeredPane.PALETTE_LAYER);
        createMenu();

        LevelManager levelManager = new LevelManager();

        // mouse
        MouseInput mouseInput = new MouseInput(camera, this, gameRenderer);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        addMouseWheelListener(mouseInput);

        // keyboard
        KeyboardActions kActions = new KeyboardActions(camera);
        setVisible(true);
        kActions.register(this);
    }

    private void createMenu() {
        ComponentMenu menu = new ComponentMenu(this);
        menu.setSize(600, 70);
        menu.setLocation(
                (uiPanel.getWidth() - menu.getWidth()) / 2,
                uiPanel.getHeight() - menu.getHeight() - 20);
        uiPanel.add(menu);

    }

    public void setSelectedType(ComponentType c, boolean v, LayerType r) {
        selectedType = c;
        variant = v;
        selectedComponent = ComponentType.createComponent(selectedType, selectedDirection, 0, 0, v, r);
    }

    public void rotateDirection() {
        selectedDirection = selectedDirection.right();
        selectedComponent.direction = selectedDirection;
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

        // gameRenderer.debugDraw(g, sWidth, sHeight);
    }
}
