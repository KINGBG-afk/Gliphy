package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;

public class Hub extends Component {
    int stored;

    public Hub(int i, int j, Direction dir) {
        super(i, j, dir, new int[] { 1, 1 }, ComponentType.HUB);
        this.stored = 0;

        // hub is 1x1 so we make it accept from all 4 directions
        Direction north = Direction.NORTH;
        Direction west = Direction.WEST;
        Direction east = west.opposite();
        Direction south = north.opposite();

        addinput(north.getDi(), north.getDj(), north);
        addinput(west.getDi(), west.getDj(), west);
        addinput(east.getDi(), east.getDj(), east);
        addinput(south.getDi(), south.getDj(), south);

    }

    @Override
    public void update() {
        for (Port in : inputs) {
            if (in.hasItem()) {
                stored++;
                in.eject();
                System.out.println("Stored in hub: " + stored);
            }
        }
    }

    public int getStored() {
        return stored;
    }
}
