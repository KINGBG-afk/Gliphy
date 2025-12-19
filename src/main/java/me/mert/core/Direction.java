package me.mert.core;

public enum Direction {
    NORTH(-1, 0),
    EAST(0, 1),
    SOUTH(1, 0),
    WEST(0, -1);

    // lets not forget that if i didn't think of di and dj i would've probably
    // demotivated myself from having to rewrite the whole code base
    // for future me: just thank past me
    private final int di;
    private final int dj;

    Direction(int di, int dj) {
        this.di = di;
        this.dj = dj;
    }

    // from int to direction
    public static Direction fromOrientation(int o) {
        // COUTION: DO NOT use negative values
        // just in case the number is heigher than 4 we do
        // x % 4
        return values()[o % 4];
    }

    public int getDi() {
        return di;
    }

    public int getDj() {
        return dj;
    }

    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case EAST -> WEST;
            case SOUTH -> NORTH;
            case WEST -> EAST;
        };
    }

}
