package me.mert.world;

import me.mert.core.Noise;
import me.mert.core.enums.LayerType;

public class Chunk {

    int x, y;
    Tile[] tiles;

    private static final float SCALE = 0.06f;
    private static final float RESOURCE_THRESHOLD = 0.70f;
    public static final int CHUNK_SIZE = 16;

    private final Noise noise;

    Chunk(int x, int y, Noise noise) {
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

        if (n < 0.30f) {
            return new Tile(worldX, worldY, LayerType.CIRCLE);
        }
        if (n > 0.30f && n < 0.60f) {
            return new Tile(worldX, worldY, LayerType.SQUARE);
        }
        if (n > 0.60f) {
            return new Tile(worldX, worldY, LayerType.LINE);
        }

        return null;
    }

    @Override
    public String toString() {
        return String.format("CHUNK(x=%d, y=%d)", x, y);
    }

}
