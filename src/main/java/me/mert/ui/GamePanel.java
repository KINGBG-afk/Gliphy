package me.mert.ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import me.mert.components.Component;
import me.mert.core.ComponentType;
import me.mert.core.Direction;
import me.mert.core.GameRenderer;
import me.mert.input.KeyboardActions;
import me.mert.input.MouseInput;
import me.mert.world.World;

public class GamePanel extends JPanel {
    private final Camera camera;
    private final World world;
    private final GameRenderer gameRenderer;

    protected ComponentType selectedType = ComponentType.CONVEYOR;
    protected Direction selectedDirection = Direction.NORTH;
    protected Component selectedComponent = ComponentType.createComponent(selectedType, 0, 0);

    public GamePanel(Camera camera, World world, GameRenderer gameRenderer) {
        this.camera = camera;
        this.world = world;
        this.gameRenderer = gameRenderer;

        setBackground(Color.BLACK);
        setFocusable(true);

        // mouse
        MouseInput mouseInput = new MouseInput(camera, this, gameRenderer);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        addMouseWheelListener(mouseInput);

        // keyboard
        KeyboardActions kActions = new KeyboardActions(camera);
        kActions.register(this);

    }

    public void setSelectedType(ComponentType c) {
        selectedType = c;
        selectedComponent = ComponentType.createComponent(selectedType, selectedDirection, 0, 0);
        System.out.println("Selected component: " + c);
    }

    public void rotateDirection() {
        selectedDirection = selectedDirection.rotate90();
        selectedComponent.direction = selectedDirection;
        System.out.println("Direction is " + selectedDirection);
    }

    public void placeSelectedComponentAt(int i, int j) {
        world.placeObject(i, j, selectedType, selectedDirection);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int sWidth = getWidth();
        int sHeight = getHeight();
        gameRenderer.drawGrid(g, sWidth, sHeight);
        gameRenderer.drawComponents(g, sWidth, sHeight);
        gameRenderer.drawPreviewComponent(g, selectedComponent, true);

    }
}
