package me.mert.core.enums;

public enum Primitive {
    EMPTY,
    LINE,
    CIRCLE,
    SQUARE;

    public static Primitive getPrimitive(String p) {
        return switch (p) {
            case "empty" -> EMPTY;
            case "square" -> SQUARE;
            case "circle" -> CIRCLE;
            case "line" -> LINE;
            default -> null;
        };
    }
}
