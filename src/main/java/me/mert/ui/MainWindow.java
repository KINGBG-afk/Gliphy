package me.mert.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import me.mert.components.Collector;
import me.mert.components.Conveyor;
import me.mert.components.Hub;
import me.mert.core.Direction;
import me.mert.core.GameRenderer;
import me.mert.input.KeyboardActions;
import me.mert.input.MouseInput;
import me.mert.world.World;

public class MainWindow extends JPanel implements ActionListener {
    private final Dimension screeDimension = Toolkit.getDefaultToolkit().getScreenSize();

    private final int FPS = 120;

    private final Camera camera;
    private World world;
    private final GameRenderer gameRenderer;
    private final Timer timer;
    private final Timer updateComponentsTimer;

    MainWindow() {
        setBackground(Color.BLACK);
        setPreferredSize(screeDimension);
        setFocusable(true);
        camera = new Camera(0, 0);
        world = new World();
        gameRenderer = new GameRenderer(camera, world);

        // mouse
        MouseInput mouseInput = new MouseInput(camera, this);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        addMouseWheelListener(mouseInput);

        // keyboard
        KeyboardActions kActions = new KeyboardActions(camera);
        kActions.register(this);

        Collector collector = new Collector(0, 0, Direction.SOUTH);
        Conveyor conveyor = new Conveyor(1, 0, Direction.SOUTH);
        Conveyor conveyor2 = new Conveyor(2, 0, Direction.SOUTH);
        Hub hub = new Hub(3, 0, Direction.NORTH);

        world.placeObject(2, 0, conveyor2);
        world.placeObject(3, 0, hub);
        world.placeObject(1, 0, conveyor);
        world.placeObject(0, 0, collector);

        conveyor.out.connectedTo = conveyor2.in;
        collector.out.connectedTo = conveyor.in;
        conveyor2.out.connectedTo = hub.inputs.get(0);


        timer = new Timer(1000 / FPS, this);
        timer.start();

        updateComponentsTimer = new Timer(1000, e -> {
            world.updateComponents();
        });
        updateComponentsTimer.start();

        // BUG items gets lost in the connections somewhere in the ports
        // might need to visualize it

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // camera.update();
        // zooming isn't working like its supposed to do
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int sWidth = getWidth();
        int sHeight = getHeight();
        gameRenderer.drawGrid(g, sWidth, sHeight);
        gameRenderer.drawComponents(g, sWidth, sHeight);

    }
}
