package me.mert.game;

public class CurrencyManager {
    private int coins;

    public boolean canAfford(int cost) {
        return coins >= cost;
    }

    public void add(int amount) {
        coins += amount;
    }

    public void spend(int cost) {
        if (canAfford(cost)) {
            coins -= cost;
        }
    }
}
