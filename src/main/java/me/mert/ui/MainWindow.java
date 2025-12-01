package me.mert.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import me.mert.MouseInput;
import me.mert.core.GameRenderer;
import me.mert.world.World;
import me.mert.components.Collector;

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

        MouseInput mouseInput = new MouseInput(camera, this);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        addMouseWheelListener(mouseInput);

        world.setTitle(0, 0, new Collector(0, 0, 2));

        timer = new Timer(1000 / FPS, this);
        timer.start();

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
