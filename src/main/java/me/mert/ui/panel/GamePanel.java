package me.mert.ui.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import me.mert.components.Component;
import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.input.KeyboardActions;
import me.mert.input.MouseInput;
import me.mert.ui.Camera;
import me.mert.ui.GameRenderer;
import me.mert.world.World;

public class GamePanel extends JPanel {
    // i swaer at some point imma run into performance issues
    private final World world;
    private final GameRenderer gameRenderer;

    protected ComponentType selectedType = ComponentType.COLLECTOR;
    protected Direction selectedDirection = Direction.NORTH;
    protected Component selectedComponent = ComponentType.createComponent(selectedType, selectedDirection, 0, 0);

    public GamePanel(Camera camera, World world, GameRenderer gameRenderer) {
        this.world = world;
        this.gameRenderer = gameRenderer;

        // setBackground(new Color(120, 120, 120));
        // setBackground(new Color(234, 235, 237));
        setBackground(new Color(255, 255, 255));
        setFocusable(true);

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

    public void setSelectedType(ComponentType c) {
        selectedType = c;
        selectedComponent = ComponentType.createComponent(selectedType, selectedDirection, 0, 0);
    }

    public void rotateDirection() {
        selectedDirection = selectedDirection.right();
        selectedComponent.direction = selectedDirection;
    }

    public void placeSelectedComponentAt(int i, int j) {
        world.placeComponent(i, j, selectedType, selectedDirection);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int sWidth = getWidth();
        int sHeight = getHeight();
        gameRenderer.drawGrid((Graphics2D) g, sWidth, sHeight);
        gameRenderer.drawTiles((Graphics2D) g, sWidth, sHeight);
        gameRenderer.drawComponents(g, sWidth, sHeight);
        gameRenderer.drawPreviewComponent(g, selectedComponent, true);

    }
}
