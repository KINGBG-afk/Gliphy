package me.mert.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpgradeManager {
    public static UpgradeManager instance;
    private Map<String, Upgrade> upgrades = new HashMap<>();
    private Set<String> unlockedComponents = new HashSet<>();

    private UpgradeManager() {
        upgrades.put("speed", new Upgrade("speed", 10, 50, 2));
        unlockedComponents.add("collector");
        unlockedComponents.add("conveyor");
        unlockedComponents.add("hub");
        unlockedComponents.add("trash");

    }

    public static UpgradeManager getInstance() {
        if (instance == null) {
            instance = new UpgradeManager();
        }
        return instance;
    }

    public Collection<Upgrade> getAllUpgrades() {
        return upgrades.values();
    }

    public Set<String> getUnlockedComponents() {
        return unlockedComponents;
    }

    public Upgrade getUpgrade(String id) {
        return upgrades.get(id);
    }

    public boolean isComponentUnlocked(String id) {
        return unlockedComponents.contains(id);
    }

    public void unlockComponent(String id) {
        unlockedComponents.add(id);
    }

    public void setUnlockedComponents(Set<String> unlockedComponents) {
        this.unlockedComponents = unlockedComponents;
    }

    public void setUpgrades(Map<String, Upgrade> upgrades) {
        this.upgrades = upgrades;
    }

}
