package me.mert.game;

public class Upgrade {
    private final String id;
    private int level;
    private final int maxLevel;
    private final int baseCost;
    private final int costMultiplier;

    public Upgrade(String id, int maxLevel, int baseCost, int costMultiplier) {
        this.id = id;
        this.maxLevel = maxLevel;
        this.baseCost = baseCost;
        this.costMultiplier = costMultiplier;
        this.level = 1;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int l) {
        this.level = l;
    }

    public int getCost() {
        return (int) (baseCost * Math.pow(costMultiplier, level - 1));
    }

    public void levelUp() {
        if (level < maxLevel) {
            level++;
        }
    }

}
