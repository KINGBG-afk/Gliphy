package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;

public class Trash extends Component {

    public Trash(int i, int j, Direction dir) {
        super(i, j, dir, new int[] {1,1}, ComponentType.TRASH);

        addinput(Direction.EAST);
        addinput(Direction.WEST);
        addinput(Direction.NORTH);
        addinput(Direction.SOUTH);
    }

    @Override
    public void update() {
        for (Port p : inputs) {
            if (p.hasItem()) {
                p.eject();
            }
        }
    }
    
}
