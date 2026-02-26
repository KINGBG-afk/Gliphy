package me.mert.core.enums;

import me.mert.components.Collector;
import me.mert.components.Component;
import me.mert.components.Conveyor;
import me.mert.components.Cutter;
import me.mert.components.Hub;
import me.mert.components.Merger;
import me.mert.components.Rotater;
import me.mert.components.Stacker;
import me.mert.components.Trash;

public enum ComponentType {
    COLLECTOR,
    CONVEYOR,
    CUTTER,
    ROTATER,
    STACKER,
    MERGER,
    TRASH,
    HUB;

    public static Component createComponent(ComponentType compType, int i, int j, boolean variant, LayerType r) {
        return switch (compType) {
            case CONVEYOR -> new Conveyor(i, j, Direction.NORTH);
            case COLLECTOR -> new Collector(i, j, Direction.NORTH, r);
            case HUB -> new Hub(i, j, Direction.NORTH);
            case CUTTER -> new Cutter(i, j, Direction.NORTH);
            case STACKER -> new Stacker(i, j, Direction.NORTH);
            case MERGER -> new Merger(i, j, Direction.NORTH, variant);
            case ROTATER -> new Rotater(i, j, Direction.NORTH);
            case TRASH -> new Trash(i, j, Direction.NORTH);
            default -> null;
        };
    }

    public static Component createComponent(ComponentType compType, Direction d, int i, int j, boolean variant,
            LayerType r) {
        return switch (compType) {
            case CONVEYOR -> new Conveyor(i, j, d);
            case COLLECTOR -> new Collector(i, j, d, r);
            case HUB -> new Hub(i, j, d);
            case CUTTER -> new Cutter(i, j, d);
            case STACKER -> new Stacker(i, j, d);
            case MERGER -> new Merger(i, j, d, variant);
            case ROTATER -> new Rotater(i, j, d);
            case TRASH -> new Trash(i, j, d);
            default -> null;
        };
    }
}
