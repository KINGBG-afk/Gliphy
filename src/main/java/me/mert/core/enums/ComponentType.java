package me.mert.core.enums;

import me.mert.components.Collector;
import me.mert.components.Component;
import me.mert.components.Conveyor;
import me.mert.components.Cutter;
import me.mert.components.Hub;

public enum ComponentType {
    COLLECTOR,
    CONVEYOR,
    CUTTER,
    HUB;

    public static Component createComponent(ComponentType compType, int i, int j) {
        return switch (compType) {
            case CONVEYOR -> new Conveyor(i, j, Direction.NORTH);
            case COLLECTOR -> new Collector(i, j, Direction.NORTH);
            case HUB -> new Hub(i, j, Direction.NORTH);
            case CUTTER -> new Cutter(i, j, Direction.NORTH);
            default -> null;
        };
    }

    public static Component createComponent(ComponentType compType, Direction d, int i, int j) {
        return switch (compType) {
            case CONVEYOR -> new Conveyor(i, j, d);
            case COLLECTOR -> new Collector(i, j, d);
            case HUB -> new Hub(i, j, d);
            case CUTTER -> new Cutter(i, j, Direction.NORTH);
            default -> null;
        };
    }
}
