package me.mert.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import me.mert.core.GameRenderer;
import me.mert.input.KeyboardActions;
import me.mert.input.MouseInput;
import me.mert.world.World;
import me.mert.components.Collector;
import me.mert.components.Conveyor;
import me.mert.components.Hub;

public class MainWindow extends JPanel implements ActionListener {
    private Dimension screeDimension = Toolkit.getDefaultToolkit().getScreenSize();

    private final int FPS = 120;

    private Camera camera;
    private World world;
    private GameRenderer gameRenderer;
    private final Timer timer;

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

        Collector collector = new  Collector(0, 0, 1);
        Conveyor conveyor = new     Conveyor(1, 0, 1);
        Conveyor conveyor2 = new    Conveyor(2, 0, 1);
        Hub hub = new                    Hub(3, 0, 0);

        world.setTile(0, 0, collector);
        world.setTile(1, 0, conveyor);
        world.setTile(2, 0, conveyor2);
        world.setTile(3, 0, hub);

        System.out.println(world.getTile(0, 0).getComponent());
        System.out.println(world.getTile(1, 0).getComponent());
        System.out.println(world.getTile(2, 0).getComponent());
        System.out.println(world.getTile(3, 0).getComponent());

        timer = new Timer(1000 / FPS, this);
        timer.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // camera.update();
        // zooming isn't working like its supposed to do
        world.updateComponents();
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
