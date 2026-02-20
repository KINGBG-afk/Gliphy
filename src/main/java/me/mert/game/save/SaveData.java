package me.mert.game.save;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.mert.components.Component;

public class SaveData implements Serializable {
    public static final long serialVersionUID = 1l;

    public int level;
    public int seed;
    public long coins;
    public double cameraX;
    public double cameraY;
    public double cameraZoom;
    public List<Component> components;
    public Map<String, Integer> upgradeLevels;
    public Set<String> unlockedComponents;

}
