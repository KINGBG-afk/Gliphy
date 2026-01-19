package me.mert.world;

import java.util.ArrayList;
import java.util.List;

import me.mert.components.Component;
import me.mert.components.Conveyor;
import me.mert.components.Port;
import me.mert.core.Constants;
import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.core.enums.PortType;

public class World {
    private final Tile[][] tiles;
    private final List<Component> components;
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

    public boolean removeComponent(int i, int j) {
        if (!inBounds(i, j))
            return false;

        Component c = getComponent(i, j);

        if (c == null)
            return false;

        components.remove(c);
        tiles[i][j] = new Tile(i, j);
        return true;
    }

    public boolean placeComponent(int i, int j, ComponentType comp, Direction dir) {
        Component obj = ComponentType.createComponent(comp, dir, i, j);
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

        System.out.println("Placing: " + obj);
        components.add(obj);
        connectPorts(obj);

        return true;
    }

    // not the most efficient thing but im proud of it
    private void connectPorts(Component placed) {
        Component other;

        for (Port p : placed.getAllPorts()) {
            int wi = p.getWorldI();
            int wj = p.getWorldJ();

            if (!inBounds(wi, wj))
                continue;

            other = getComponent(wi, wj);
            // if it doesn't exist or its the same as "placed" we skip
            if (other == null || other == placed)
                continue;

            for (Port op : other.getAllPorts()) {
                System.out.println("Other port: i=" + op.getWorldI() + ", j=" + op.getWorldJ());
                System.out.println("Port of placed: i=" + wi + ", j=" + wj);

                makeConnection(placed, p, wi, wj, op);
            }
        }

        if (placed instanceof Conveyor conveyor) {
            Direction[] dirs = new Direction[] {
                    Direction.NORTH,
                    Direction.EAST,
                    Direction.SOUTH,
                    Direction.WEST
            };

            for (Direction d : dirs) {
                if (conveyor.direction == d || conveyor.direction.opposite() == d)
                    continue;

                int ni = conveyor.i + d.getDi();
                int nj = conveyor.j + d.getDj();

                if (!inBounds(ni, nj))
                    continue;
                other = getComponent(ni, nj);
                if (other == null || other == conveyor)
                    continue;

                for (Port p : other.getAllPorts()) {
                    // rule all cases wehere we do not need to change ports
                    int wi = p.getWorldI();
                    int wj = p.getWorldJ();
                    Direction pDir = p.getDirection();

                    if (!inBounds(wi, wj))
                        continue;

                    if (p.type == PortType.OUTPUT || pDir == conveyor.direction)
                        continue;

                    if (conveyor.i == wi && conveyor.j == wj) {

                        if (conveyor.changeOutputPort(ni, nj, pDir.opposite())) {
                            System.out.println(":: Conveyor changed ports");
                            makeConnection(placed, conveyor.out, ni, nj, p);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void makeConnection(Component placed, Port p, int wi, int wj, Port op) {
        // if ports does not match
        // placed's ports should not be on the same pos as other's ports
        if (op.getWorldI() != wi && op.getWorldJ() != wj)
            return;

        if (p.type == PortType.OUTPUT && op.type == PortType.INPUT
                && p.getDirection().opposite() == op.getDirection()) {
            p.connectTo(op);
            System.out.println(placed + " connected to " + op.getOwner());

        } else if (p.type == PortType.INPUT && op.type == PortType.OUTPUT
                && op.getDirection().opposite() == p.getDirection()) {
            System.out.println(op.getOwner() + " connected to " + placed);
            op.connectTo(p);
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
