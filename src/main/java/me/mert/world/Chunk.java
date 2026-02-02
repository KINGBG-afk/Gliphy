package me.mert.world;

import me.mert.core.Constants;
import me.mert.core.enums.LayerType;
import personthecat.fastnoise.FastNoise;
import personthecat.fastnoise.data.NoiseType;

public class Chunk {

    int x, y;
    Tile[] tiles;

    private static final float SCALE = 0.15f;
    private static final float RESOURCE_THRESHOLD = 0.6f;
    private final int CHUNK_SIZE = Constants.CHUNK_SIZE;

    private static final FastNoise noise = FastNoise.builder()
            .type(NoiseType.SIMPLEX2)
            .frequency(0.2f)
            .build();

    Chunk(int x, int y) {
        this.x = x;
        this.y = y;

        tiles = new Tile[CHUNK_SIZE * CHUNK_SIZE];
        generate();
    }

    private void generate() {
        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                int worldX = x * CHUNK_SIZE + i;
                int worldY = y * CHUNK_SIZE + j;
                tiles[i + j * CHUNK_SIZE] = createTile(worldX, worldY);
            }
        }
    }

    private Tile createTile(int worldX, int worldY) {
        float mainNoise = noise.getNoise(worldX * SCALE, worldY * SCALE);
        float deltaNoise = noise.getNoise(worldX * SCALE * 2.5f, worldY * SCALE * 2.5f);

        float combined = mainNoise * 0.7f + deltaNoise * 0.3f;
        combined = (combined + 1f) * 0.5f;

        if (combined > RESOURCE_THRESHOLD && combined < RESOURCE_THRESHOLD + 0.25f) {
            return new Tile(worldX, worldY, LayerType.CIRCLE);
        } else {
            return new Tile(worldX, worldY, null);
        }

    }

    public static void draw(Chunk chunk, int cx, int cy) {

    }

}
