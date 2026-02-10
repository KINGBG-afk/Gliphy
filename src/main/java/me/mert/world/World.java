package me.mert.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.mert.components.Component;
import me.mert.components.Conveyor;
import me.mert.components.Port;
import me.mert.core.Constants;
import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.core.enums.PortType;
import personthecat.fastnoise.FastNoise;
import personthecat.fastnoise.data.NoiseType;

public class World {
    private final List<Component> components;

    // funnily enough this is the first time i ever used the long type
    private final HashMap<Long, Chunk> chunks;

    private Set<Long> activeChunks = new HashSet<>();
    private final int CHUNK_SIZE = Constants.CHUNK_SIZE;

    private static final int CHUNK_LOAD_RADIUS = 1;
    private static final int CHUNK_UNLOAD_RADIUS = 1;

    private final int seed;
    private final FastNoise noise;

    private int centerChunkX = 0;
    private int centerChunkY = 0;

    public World(int seed) {
        this.seed = seed;
        chunks = new HashMap<>();
        this.components = new ArrayList<>();
        this.noise = FastNoise.builder()
                .seed(seed)
                .type(NoiseType.SIMPLEX2)
                .frequency(0.25f)
                .build();
        loadChunksAroundCenter();
    }

    public World() {
        chunks = new HashMap<>();
        this.components = new ArrayList<>();

        this.seed = World.generateSeed();
        this.noise = FastNoise.builder()
                .seed(seed)
                .type(NoiseType.SIMPLEX2)
                .frequency(0.25f)
                .build();
        loadChunksAroundCenter();
    }

    public static int generateSeed() {
        return (int) System.nanoTime();
    }

    // --- CHUNKS ------------------------------------------------------------
    public Set<Long> getChunks() {
        return activeChunks;
    }

    /**
     * This method accepts positions in TILE coordinates
     * 
     * @param x x position in tile coordinates
     * @param y y position in tile coordinates
     */
    public void updateCenter(int x, int y) {
        int ncX = Math.floorDiv(x, CHUNK_SIZE);
        int ncY = Math.floorDiv(y, CHUNK_SIZE);

        if (ncX != centerChunkX || ncY != centerChunkY) {
            centerChunkX = ncX;
            centerChunkY = ncY;
            manageChunks();
        }
    }

    private void manageChunks() {
        int chunkX, chunkY;
        Set<Long> nActiveChunks = new HashSet<>();

        for (int y = -CHUNK_LOAD_RADIUS; y <= CHUNK_LOAD_RADIUS; y++) {
            for (int x = -CHUNK_LOAD_RADIUS; x <= CHUNK_LOAD_RADIUS; x++) {
                chunkX = centerChunkX + x;
                chunkY = centerChunkY + y;
                long key = chunkKey(chunkX, chunkY);

                if (!chunks.containsKey(key)) {
                    loadChunk(chunkX, chunkY);
                }
                nActiveChunks.add(key);
            }
        }

        List<Long> chunksToUnload = new ArrayList<>();
        for (Long key : chunks.keySet()) {
            chunkX = (int) (key >> 32);
            chunkY = (int) (key & 0xFFFFFFFFL);

            int cx = Math.abs(chunkX - centerChunkX);
            int cy = Math.abs(chunkY - centerChunkY);

            if (cx > CHUNK_UNLOAD_RADIUS || cy > CHUNK_UNLOAD_RADIUS) {
                chunksToUnload.add(key);
            }
        }

        for (Long key : chunksToUnload) {
            unloadChunk(key);
        }

        activeChunks = nActiveChunks;
    }

    // NOTE: called only to generate chunks in the constructor
    private void loadChunksAroundCenter() {
        for (int y = -CHUNK_LOAD_RADIUS; y <= CHUNK_LOAD_RADIUS; y++) {
            for (int x = -CHUNK_LOAD_RADIUS; x <= CHUNK_LOAD_RADIUS; x++) {
                loadChunk(x, y);
                activeChunks.add(chunkKey(x, y));
            }
        }
    }

    private void loadChunk(int x, int y) {
        long key = chunkKey(x, y);
        if (!chunks.containsKey(key)) {
            Chunk chunk = new Chunk(x, y, noise);
            chunks.put(key, chunk);
            // System.out.println("Loaded chunk: " + chunk);
        }
    }

    private void unloadChunk(long key) {
        Chunk chunk = chunks.get(key);
        if (chunk == null)
            return;
        activeChunks.remove(key);
        // all components are never removed unless the user does that
        // chunks stays unloaded but the components are still getting updated

        // System.out.println("Unloaded: " + chunk);
    }

    private long chunkKey(int x, int y) {
        // casting x as long and shifting left by32 bits to the upper 32
        // then mask y to ensure it's unsigned and them combine them with "|"
        return (((long) x) << 32) | (y & 0xffffffffL);
    }

    // --- COMPONENTS ------------------------------------------------------------
    public Tile getTile(int i, int j) {
        // NOTE: use floorDiv to get the x,y of the chunk and floorMod to convert into
        // local pos in the chunk
        int chunkX = Math.floorDiv(j, CHUNK_SIZE);
        int chunkY = Math.floorDiv(i, CHUNK_SIZE);

        long key = chunkKey(chunkX, chunkY);
        Chunk chunk = chunks.get(key);

        if (chunk == null) {
            // REVIEW: or generate a new chunk
            loadChunk(chunkX, chunkY);
            return null;
        }

        int localX = Math.floorMod(j, CHUNK_SIZE);
        int localY = Math.floorMod(i, CHUNK_SIZE);
        return chunk.tiles[localY * CHUNK_SIZE + localX];
    }

    public Component getComponent(int i, int j) {
        Tile tile = getTile(i, j);
        return tile != null ? tile.component : null;
    }

    private boolean canPlace(int i, int j) {
        Tile tile = getTile(i, j);
        return (tile != null && tile.component == null);
    }

    private void clearComponent(int i, int j) {
        int chunkX = Math.floorDiv(j, CHUNK_SIZE);
        int chunkY = Math.floorDiv(i, CHUNK_SIZE);

        Chunk chunk = chunks.computeIfAbsent(
                chunkKey(chunkX, chunkY),
                k -> new Chunk(chunkX, chunkY, noise));

        int localX = Math.floorMod(j, CHUNK_SIZE);
        int localY = Math.floorMod(i, CHUNK_SIZE);

        chunk.tiles[localY * CHUNK_SIZE + localX].component = null;
    }

    public boolean removeComponent(int i, int j) {
        Component c = getComponent(i, j);

        if (c == null)
            return false;

        components.remove(c);

        // clear ALL occupied tiles
        for (int di = 0; di < c.size[1]; di++) {
            for (int dj = 0; dj < c.size[0]; dj++) {
                clearComponent(i, j);
            }
        }
        return true;
    }

    public boolean placeComponent(int i, int j, ComponentType comp, Direction dir) {
        int[] size;
        Component obj = ComponentType.createComponent(comp, dir, i, j);
        int ox = 0, oy = 0;
        if (obj == null)
            return false;

        if (dir == Direction.EAST || dir == Direction.WEST) {
            size = new int[] { obj.size[1], obj.size[0] };
        } else {
            size = new int[] { obj.size[0], obj.size[1] };
        }

        int w = size[0];
        int h = size[1];
        switch (dir) {
            case NORTH -> oy = -(h - 1);
            case WEST -> ox = -(w - 1);
        }

        for (int di = 0; di < h; di++) {
            for (int dj = 0; dj < w; dj++) {
                int ni = i + ox + di;
                int nj = j + oy + dj;

                if (!canPlace(ni, nj))
                    return false;
            }
        }

        // place is valid so proceed
        for (int di = 0; di < h; di++) {
            for (int dj = 0; dj < w; dj++) {
                int ni = i + di;
                int nj = j + dj;
                getTile(ni, nj).component = obj;

            }
        }

        System.out.println("Placing: " + obj);
        components.add(obj);
        connectPorts(obj);

        return true;
    }

    private void connectPorts(Component placed) {
        Component other;

        for (Port p : placed.getAllPorts()) {
            int wi = p.getWorldI();
            int wj = p.getWorldJ();

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

                other = getComponent(ni, nj);
                if (other == null || other == conveyor)
                    continue;

                for (Port p : other.getAllPorts()) {
                    // rule all cases wehere we do not need to change ports
                    int wi = p.getWorldI();
                    int wj = p.getWorldJ();
                    Direction pDir = p.getDirection();

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
