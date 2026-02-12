package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;

public class Stacker extends Component {

    public Stacker(int i, int j, Direction direction) {
        super(i, j, direction, new int[] { 1, 2 }, ComponentType.STACKER);
    }

    @Override
    public void update() {
    }

}
