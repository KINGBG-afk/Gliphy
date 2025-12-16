package me.mert.components;

import me.mert.core.ComponentType;
import me.mert.core.Direction;

public class Hub extends Component {
    int stored;

    public Hub(int i, int j, Direction dir) {
        super(i, j, dir, new int[] { 3, 3 }, ComponentType.HUB);
        this.stored = 0;
        loadImage("hub");

        // hub is 3x3 so we make it accept from all 12 directions
        // (i, j) is top left tile
        // and it will be in the middle of the grid so we don't have to check for bounds
        // TOP
        addinput(i - 1, j, dir); // LEFT
        addinput(i - 1, j + 1, dir); // MIDDLE
        addinput(i - 1, j + 2, dir); // RIGHT

        // EAST
        addinput(i, j + 3, dir); // TOP
        addinput(i + 1, j + 3, dir); // MIDDLE
        addinput(i + 2, j + 3, dir); // BOTTOM

        // SOUTH
        addinput(i + 3, j, dir); // LEFT
        addinput(i + 3, j + 1, dir); // MIDDLE
        addinput(i + 3, j + 2, dir); // RIGHT

        // WEST
        addinput(i + 1, j - 1, dir); // TOP
        addinput(i + 2, j - 1, dir); // MIDDLE
        addinput(i + 3, j - 1, dir); // DOWN

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
