package me.mert.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.mert.components.Component;
import me.mert.components.Conveyor;
import me.mert.components.Port;
import me.mert.core.Constants;
import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.core.enums.PortType;

public class World {
    private final List<Component> components;

    // funnily enough this is the first time i ever used the long type
    private HashMap<Long, Chunk> chunks;

    private final int CHUNK_SIZE = Constants.CHUNK_SIZE;

    public World() {
        chunks = new HashMap<>();
        generateEmptyWorld();
        this.components = new ArrayList<>();
    }

    private void generateEmptyWorld() {
        for (int y = -2; y <= 2; y++) {
            for (int x = -2; x <= 2; x++) {
                Chunk c = new Chunk(x, y);
                chunks.put(chunkKey(x, y), c);
            }
        }
    }

    private long chunkKey(int x, int y) {
        return (((long) x) << 32) | (y & 0xffffffffL);
    }

    public Tile getTile(int i, int j) {
        // NOTE: use floorDiv to get the x,y of the chunk and floorDiv to convert into
        // local pos in the chunk
        int chunkX = Math.floorDiv(j, CHUNK_SIZE);
        int chunkY = Math.floorDiv(i, CHUNK_SIZE);

        long key = chunkKey(chunkX, chunkY);
        Chunk chunk = chunks.get(key);

        if (chunk == null) {
            // REVIEW: or generate a new chunk
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

    public boolean canPlace(int i, int j) {
        Tile tile = getTile(i, j);
        return (tile != null && tile.component == null);
    }

    public void setTile(int i, int j, Tile tile) {
        int chunkX = Math.floorDiv(j, CHUNK_SIZE);
        int chunkY = Math.floorDiv(i, CHUNK_SIZE);

        Chunk chunk = chunks.computeIfAbsent(
                chunkKey(chunkX, chunkY),
                k -> new Chunk(chunkX, chunkY));

        int localX = Math.floorMod(j, CHUNK_SIZE);
        int localY = Math.floorMod(i, CHUNK_SIZE);

        chunk.tiles[localY * CHUNK_SIZE + localX] = tile;
    }

    public boolean removeComponent(int i, int j) {
        Component c = getComponent(i, j);

        if (c == null)
            return false;

        components.remove(c);

        // clear ALL occupied tiles
        for (int di = 0; di < c.size[1]; di++) {
            for (int dj = 0; dj < c.size[0]; dj++) {
                setTile(c.i + di, c.j + dj, new Tile(c.i + di, c.j + dj));
            }
        }
        return true;
    }

    public boolean placeComponent(int i, int j, ComponentType comp, Direction dir) {
        int[] size;
        Component obj = ComponentType.createComponent(comp, dir, i, j);
        if (obj == null)
            return false;

        if (dir == Direction.EAST || dir == Direction.WEST) {
            size = new int[] { obj.size[1], obj.size[0] };
        } else {
            size = new int[] { obj.size[0], obj.size[1] };
        }

        int w = size[0];
        int h = size[1];

        for (int di = 0; di < h; di++) {
            for (int dj = 0; dj < w; dj++) {
                int ni = i + di;
                int nj = j + dj;

                if (!canPlace(ni, nj))
                    return false;
            }
        }

        // place is valid so proceed
        for (int di = 0; di < h; di++) {
            for (int dj = 0; dj < w; dj++) {
                int ni = i + di;
                int nj = j + dj;
                // good luck to future me :)
                // but no fr this could return null
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
