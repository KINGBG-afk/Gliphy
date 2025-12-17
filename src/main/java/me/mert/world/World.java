package me.mert.world;

import java.util.ArrayList;
import java.util.List;

import me.mert.components.Collector;
import me.mert.components.Component;
import me.mert.components.Conveyor;
import me.mert.components.Hub;
import me.mert.components.Port;
import me.mert.core.ComponentType;
import me.mert.core.Constants;

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

    public boolean canPlace(int i, int j) {
        Tile tile = getTile(i, j);
        return (tile != null && tile.component != null);
    }

    private Component createComponent(ComponentType compType, int i, int j) {
        return switch (compType) {
            case CONVEYOR -> new Conveyor(i, j, me.mert.core.Direction.EAST);
            case COLLECTOR -> new Collector(i, j, me.mert.core.Direction.EAST);
            case HUB -> new Hub(i, j, me.mert.core.Direction.EAST);
            default -> null;
        };
    }

    public boolean placeObject(int i, int j, ComponentType comp) {
        Component obj = createComponent(comp, i, j);
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
        return true;
    }

    // like we said 2 phase updates bc why not :)
    public void updateComponents() {
        for (Component obj : components) {
            obj.update();
        }

        for (Component c : components) {
            for (Port p : c.getPorts()) {
                p.commitMovement();
            }
        }
    }
}
