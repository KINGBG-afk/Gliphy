package me.mert.world;

import me.mert.core.Constants;
import personthecat.fastnoise.FastNoise;

public class Chunk {

    int x, y;
    Tile[] tiles;
    FastNoise noise;
    private final int CHUNK_SIZE = Constants.CHUNK_SIZE;

    public Chunk(int x, int y) {
        this.x = x;
        this.y = y;

        tiles = new Tile[CHUNK_SIZE * CHUNK_SIZE];
        
        // TODO: research this shit from com.'personthecat'

    }

    private void generate() {
        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                int worldX = x * CHUNK_SIZE + i;
                int worldY = y * CHUNK_SIZE + j;
                // TODO: generate the cells
            }
        }
    }

    private Tile createTile(int worldX, int worldY) {
        return new Tile(worldX, worldY);
    }

    public static void draw(Chunk chunk, int cx, int cy) {

    }

}
