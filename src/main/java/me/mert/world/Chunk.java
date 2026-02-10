package me.mert.world;

import me.mert.core.Constants;
import me.mert.core.enums.LayerType;
import personthecat.fastnoise.FastNoise;

public class Chunk {

    int x, y;
    Tile[] tiles;

    private static final float SCALE = 0.15f;
    private static final float RESOURCE_THRESHOLD = 0.8f;
    private final int CHUNK_SIZE = Constants.CHUNK_SIZE;

    private final FastNoise noise;

    Chunk(int x, int y, FastNoise noise) {
        this.x = x;
        this.y = y;
        this.noise = noise;

        tiles = new Tile[CHUNK_SIZE * CHUNK_SIZE];
        generate();
    }

    private void generate() {
        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                int worldX = x * CHUNK_SIZE + j;
                int worldY = y * CHUNK_SIZE + i;
                tiles[i * CHUNK_SIZE + j] = createTile(worldX, worldY);
            }
        }
    }

    private Tile createTile(int worldX, int worldY) {
        float mainNoise = noise.getNoise(worldX * SCALE, worldY * SCALE);
        float deltaNoise = noise.getNoise(worldX * SCALE * 2.5f, worldY * SCALE * 2.5f);

        float combined = mainNoise * 0.7f + deltaNoise * 0.3f;
        combined = (combined + 1f) * 0.5f;

        if (combined > RESOURCE_THRESHOLD) {
            return chooseRecourse(worldX, worldY);
        } else {
            return new Tile(worldX, worldY, null);
        }
    }

    private Tile chooseRecourse(int worldX, int worldY) {
        float n = (noise.getNoise(worldX * 0.08f, worldY * 0.08f) + 1f) * 0.5f;

        if (n < 0.33f) {
            return new Tile(worldX, worldY, LayerType.CIRCLE);
        } else if (n > 0.33f && n < 0.66f) {
            return new Tile(worldX, worldY, LayerType.SQUARE);
        } else {
            return new Tile(worldX, worldY, LayerType.LINE);
        }
    }

    @Override
    public String toString() {
        return String.format("CHUNK(x=%d, y=%d)", x, y);
    }

}
