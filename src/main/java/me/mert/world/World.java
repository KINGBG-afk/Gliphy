package me.mert.world;

import me.mert.components.GameObject;
import me.mert.core.Constants;

public class World {
    private Tile[][] tiles;

    public World() {
        tiles = new Tile[Constants.GRID_CELL_HEIGHT][Constants.GRID_CELL_WIDTH];
        generateEmptyWorld();
        
    }

    private void generateEmptyWorld(){
        for (int i = 0; i < Constants.GRID_CELL_HEIGHT; i++) {
            for (int j = 0; j < Constants.GRID_CELL_WIDTH; j++) {
                tiles[i][j] = new Tile(i, j);
            }
        }
    }

    private boolean inBounds(int i, int j) {
        return i >= 0 && i < Constants.GRID_CELL_HEIGHT && j >= 0 && j < Constants.GRID_CELL_WIDTH;
    }

    public Tile getTile(int i, int j) {
        if (inBounds(i, j)) return tiles[i][j];
        return null;
    }

    public void setTitle(int i, int j, GameObject obj) {
        tiles[i][j].component = obj;
    }


}
