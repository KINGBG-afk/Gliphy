package me.mert.world;

import java.util.ArrayList;
import java.util.List;

import me.mert.components.Component;
import me.mert.components.Port;
import me.mert.core.ComponentType;
import me.mert.core.Constants;
import me.mert.core.Direction;

public class World {
    private final Tile[][] tiles;
    private List<Component> components;
    private final int width = Constants.GRID_CELL_WIDTH;
    private final int height = Constants.GRID_CELL_HEIGHT;

    public World() {
        tiles = new Tile[height][width];
        generateEmptyWorld();
        this.components = new ArrayList<>();
    }

    private void generateEmptyWorld() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = new Tile(i, j);
            }
        }
    }

    private boolean inBounds(int i, int j) {
        return i >= 0 && i < height && j >= 0 && j < width;
    }

    public Tile getTile(int i, int j) {
        if (inBounds(i, j))
            return tiles[i][j];
        return null;
    }

    public Component getComponent(int i, int j) {
        Tile tile = getTile(i, j);
        if (tile.component != null) {
            return tile.component;
        } else
            return null;
    }

    public boolean canPlace(int i, int j) {
        Tile tile = getTile(i, j);
        return (tile != null && tile.component == null);
    }

    public boolean placeObject(int i, int j, ComponentType comp, Direction dir) {
        Component obj = ComponentType.createComponent(comp, dir, i, j);
        System.out.println("placing: " + obj);
        if (obj == null)
            return false;
        int w = obj.size[0];
        int h = obj.size[1];

        for (int di = 0; di < h; di++) {
            for (int dj = 0; dj < w; dj++) {
                int ni = i + di;
                int nj = j + dj;

                if (!inBounds(ni, nj) || !canPlace(ni, nj))
                    return false;
            }
        }

        // place is valid so proceed
        for (int di = 0; di < h; di++) {
            for (int dj = 0; dj < w; dj++) {
                int ni = i + di;
                int nj = j + dj;
                tiles[ni][nj].component = obj;
            }
        }

        components.add(obj);
        connectPorts(obj);
        return true;
    }

    private void connectPorts(Component placed) {
        for (Port p : placed.getAllPorts()) {
            int wi = p.getWorldI();
            int wj = p.getWorldJ();

            if (!inBounds(wi, wj))
                continue;

            Component other = getComponent(wi, wj);
            // if it exist or its the same as "placed" we skip
            if (other == null || other == placed)
                continue;

            for (Port op : other.getAllPorts()) {
                // if ports does not match
                System.out.println("::other");
                System.out.println("world I;J:" + op.getWorldI() + ";" + op.getWorldJ());

                System.out.println("::port of placed");
                System.out.println(wi + ", " + wj);
                if (op.getWorldI() != wi || op.getWorldJ() != placed.j)
                    continue;

                if (!p.isInput && op.isInput && p.getDirection().opposite() == op.getDirection()) {
                    p.connectTo(op);
                    System.out.println(placed + " connected to " + op.getOwner());
                } else if (p.isInput && !op.isInput && op.getDirection().opposite() == p.getDirection()) {
                    System.out.println(op.getOwner() + " connected to" + placed);
                    op.connectTo(p);
                }
            }

        }
    }

    // like we said 2 phase updates bc why not :)
    public void updateComponents() {
        for (Component obj : components) {
            obj.update();
        }

        for (Component c : components) {
            for (Port p : c.getAllPorts()) {
                p.commitMovement();
            }
        }
    }
}
