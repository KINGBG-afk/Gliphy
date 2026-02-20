package me.mert.game;

public class CurrencyManager {
    private static CurrencyManager instance;
    private long coins;

    private CurrencyManager() {
        coins = 0;
    }

    public static CurrencyManager getInstance() {
        if (instance == null) {
            instance = new CurrencyManager();
        }
        return instance;
    }

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

    public long getCoins() {
        return coins;
    }

    public void setCoins(long c) {
        this.coins = c;
    }

    public String getCoinsString() {
        if (coins < 1000)
            return String.valueOf(coins);

        String[] suffixes = { "k", "M", "B" };
        int exp = (int) (Math.log(coins) / Math.log(1000));
        return String.format("%.1f%s",
                coins / Math.pow(1000, exp),
                suffixes[exp - 1])
                .replace(".0", "");
    }

}
