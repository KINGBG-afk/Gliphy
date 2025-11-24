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

        camera = new Camera();
        world = new World();
        gameRenderer = new GameRenderer(camera);

        MouseInput mouseInput = new MouseInput(camera, this);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);

        timer = new Timer(1000 / FPS, this);
        timer.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        camera.update();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameRenderer.drawGrid(g, getWidth(), getHeight());

    }
}
