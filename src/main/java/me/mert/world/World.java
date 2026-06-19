package me.mert.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.mert.components.Component;
import me.mert.components.Conveyor;
import me.mert.components.Hub;
import me.mert.components.Port;
import me.mert.core.Constants;
import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.core.enums.LayerType;
import me.mert.core.enums.PortType;
import me.mert.core.logger.Logger;
import me.mert.game.SoundManager;
import personthecat.fastnoise.FastNoise;
import personthecat.fastnoise.data.NoiseType;

public class World {
    public final String worldName;

    private List<Component> components;

    // funnily enough this is the first time i ever used the long type
    private HashMap<Long, Chunk> chunks;

    private Set<Long> activeChunks = new HashSet<>();
    private final int CHUNK_SIZE = Constants.CHUNK_SIZE;

    private static final int CHUNK_LOAD_RADIUS = 1;
    private static final int CHUNK_UNLOAD_RADIUS = 1;

    private final int seed;
    private final FastNoise noise;

    private int centerChunkX = 0;
    private int centerChunkY = 0;

    public World(int seed, String name) {
        this.worldName = name;

        this.seed = seed;
        chunks = new HashMap<>();
        this.components = new ArrayList<>();
        this.noise = FastNoise.builder()
                .seed(seed)
                .type(NoiseType.SIMPLEX2)
                .frequency(0.25f)
                .build();
    }

    public World(String name) {
        this.worldName = name;

        chunks = new HashMap<>();
        this.components = new ArrayList<>();

        this.seed = World.generateSeed();
        this.noise = FastNoise.builder()
                .seed(seed)
                .type(NoiseType.SIMPLEX2)
                .frequency(0.25f)
                .build();
    }

    public static int generateSeed() {
        return (int) System.nanoTime();
    }

    public int getSeed() {
        return seed;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<Component> getComponents() {
        return components;
    }

    // --- CHUNKS ------------------------------------------------------------

    public void resetChunks() {
        chunks.clear();
        activeChunks.clear();
    }

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

    private void loadChunk(int x, int y) {
        long key = chunkKey(x, y);
        if (!chunks.containsKey(key)) {
            Chunk chunk = new Chunk(x, y, noise);
            chunks.put(key, chunk);
            // Logger.info("Loaded chunk: " + chunk);
        }
    }

    private void unloadChunk(long key) {
        Chunk chunk = chunks.get(key);
        if (chunk == null)
            return;
        activeChunks.remove(key);
        // all components are never removed unless the user does that
        // chunks stays unloaded but the components are still getting updated

        // Logger.info("Unloaded: " + chunk);
    }

    private long chunkKey(int x, int y) {
        // casting x as long and shifting left by 32 bits to the upper 32
        // then mask y to ensure it's unsigned and then combine them with bitwise OR
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
            loadChunk(chunkX, chunkY);
            chunk = chunks.get(key);
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

        if (c.type == ComponentType.HUB)
            Hub.subtractCount();

        SoundManager.play("destroy_machine");
        return true;
    }

    public boolean placeComponent(int i, int j, ComponentType comp, Direction dir, boolean variant) {
        int[] size;
        LayerType lt = getTile(i, j).recourse;
        Component obj = ComponentType.createComponent(comp, dir, i, j, variant, lt);
        int ox = 0, oy = 0;

        if (obj == null)
            return false;

        if (obj.type == ComponentType.HUB && !Hub.canPlace())
            return true;

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

        Logger.info("Placing: " + obj);
        components.add(obj);
        connectPorts(obj);

        if (obj.type == ComponentType.CONVEYOR) {
            SoundManager.play("place_belt");
        } else {
            SoundManager.play("place_machine");
        }

        // limit the world to only 5 hubs
        if (obj.type == ComponentType.HUB)
            Hub.addCount();

        SoundManager.play(worldName);

        return true;
    }

    private void connectPorts(Component placed) {
        Component other;

        for (Port p : placed.getAllPorts()) {
            int worldI = p.getWorldI();
            int worldJ = p.getWorldJ();

            other = getComponent(worldI, worldJ);
            // if it doesn't exist or its the same as "placed" we skip
            if (other == null || other == placed)
                continue;

            for (Port otherPort : other.getAllPorts()) {
                makeConnection(placed, p, worldI, worldJ, otherPort);

                Logger.info("Connected port (i=" + worldI + ", j=" + worldJ + ") with port (i=" + otherPort.getWorldI()
                        + ", j=" + otherPort.getWorldJ() + ")");

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
                            Logger.info("Conveyor changed ports");
                            makeConnection(placed, conveyor.outputs.get(0), ni, nj, p);
                            // conveyor has only 1 output at a time
                            return;
                        }
                    } // the stair case of doom
                }
            }
        }
    }

    // TODO maybe clean this up
    private void makeConnection(Component placed, Port p, int wi, int wj, Port op) {
        // if ports does not match
        // placed's ports should not be on the same pos as other's ports
        if (op.getWorldI() != wi && op.getWorldJ() != wj)
            return;

        if (p.type == PortType.OUTPUT && op.type == PortType.INPUT
                && p.getDirection().opposite() == op.getDirection()) {
            p.connectTo(op);
            Logger.info(placed + " connected to " + op.getOwner());

        } else if (p.type == PortType.INPUT && op.type == PortType.OUTPUT
                && op.getDirection().opposite() == p.getDirection()) {
            Logger.info(op.getOwner() + " connected to " + placed);
            op.connectTo(p);
        }
    }

    public void updateComponents() {
        for (Component obj : components) {
            obj.update();
        }

        // like we said 2 phase updates bc why not :)
        for (Component c : components) {
            for (Port p : c.getAllPorts()) {
                p.commitMovement();
            }
        }
    }
}
