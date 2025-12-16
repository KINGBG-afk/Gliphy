package me.mert.ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import me.mert.core.ComponentType;
import me.mert.core.GameRenderer;
import me.mert.input.KeyboardActions;
import me.mert.input.MouseInput;
import me.mert.world.World;

public class GamePanel extends JPanel {
    private final Camera camera;
    private final World world;
    private final GameRenderer gameRenderer;

    protected ComponentType selectedComponent = ComponentType.CONVEYOR;

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

    public void setSelectedComponent(ComponentType c) {
        this.selectedComponent = c;
        System.out.println("Selected component: " + c);
    }

    public void placeSelectedComponentAt(int i, int j) {
        world.placeObject(i, j, selectedComponent);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int sWidth = getWidth();
        int sHeight = getHeight();
        gameRenderer.drawGrid(g, sWidth, sHeight);
        gameRenderer.drawComponents(g, sWidth, sHeight);
        gameRenderer.drawHoveredCell(g);

    }
}
