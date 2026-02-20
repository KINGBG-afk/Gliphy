package me.mert.core.enums;

public enum Direction {
    NORTH(-1, 0),
    EAST(0, 1),
    SOUTH(1, 0),
    WEST(0, -1);

    // let's not forget that if i didn't think of di and dj i would've probably
    // demotivated myself from having to rewrite the whole code base
    // for future me: just thank past me
    private final int di;
    private final int dj;

    Direction(int di, int dj) {
        this.di = di;
        this.dj = dj;
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

    public Direction left() {
        return switch (this) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }

    public Direction right() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

}
