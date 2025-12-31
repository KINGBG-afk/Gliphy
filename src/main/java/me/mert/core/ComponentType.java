package me.mert.core;

import me.mert.components.Collector;
import me.mert.components.Component;
import me.mert.components.Conveyor;
import me.mert.components.Hub;

public enum ComponentType {
    COLLECTOR,
    CONVEYOR,
    HUB;

    public static Component createComponent(ComponentType compType, int i, int j) {
        return switch (compType) {
            case CONVEYOR -> new Conveyor(i, j, Direction.EAST);
            case COLLECTOR -> new Collector(i, j, Direction.EAST);
            case HUB -> new Hub(i, j, Direction.EAST);
            default -> null;
        };
    }

    public static Component createComponent(ComponentType compType, Direction d, int i, int j) {
        return switch (compType) {
            case CONVEYOR -> new Conveyor(i, j, d);
            case COLLECTOR -> new Collector(i, j, d);
            case HUB -> new Hub(i, j, d);
            default -> null;
        };
    }
}
