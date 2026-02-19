package me.mert.game;

import java.util.HashMap;
import java.util.Map;

public class UpgradeManager {
    public static UpgradeManager instance;
    private Map<String, Upgrade> upgrades = new HashMap<>();

    private UpgradeManager() {
        upgrades.put("speed", new Upgrade("speed", 10, 50, 2));
    }

    public static UpgradeManager getInstance() {
        if (instance == null) {
            instance = new UpgradeManager();
        }
        return instance;
    }

    public Upgrade getUpgrade(String id) {
        return upgrades.get(id);
    }
}
