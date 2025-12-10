package me.mert.world;

import java.util.ArrayList;
import java.util.List;

import me.mert.components.Component;
import me.mert.components.Port;
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

    public boolean isComponent(int i, int j) {
        Tile tile = getTile(i, j);
        return (tile != null && tile.component != null);
    }

    public boolean placeObject(int i, int j, Component obj) {
        int w = obj.size[0];
        int h = obj.size[1];

        for (int di = 0; di < h; di++) {
            for (int dj = 0; dj < w; dj++) {
                int ni = i + di;
                int nj = j + dj;

                if (!inBounds(nj, ni) || isComponent(nj, ni))
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
