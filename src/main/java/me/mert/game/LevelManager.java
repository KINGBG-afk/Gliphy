package me.mert.game;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import me.mert.core.enums.Primitive;
import me.mert.glyph.Glyph;
import me.mert.glyph.GlyphLayer;

public class LevelManager {
    private static LevelManager instance;

    private int stored;
    private int level;
    private Glyph levelGoal;
    private int goalAmount;
    private JSONObject levelObj;

    private LevelManager() {
        level = 1;
        loadNextLevel(level);
    }

    public static LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    private boolean checkLevelCompleted() {
        return stored >= goalAmount;
    }

    public void addToStored(Glyph g) {
        if (g.equals(levelGoal)) {
            stored++;

            if (checkLevelCompleted()) {
                goNextLevel();
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int l) {
        this.level = l;
        loadNextLevel(l);
        loadGoalGlyph();
        getGoalAmount();
    }

    // it's public only so it can load the goal glyph
    private void loadGoalGlyph() {
        JSONArray arr = levelObj.getJSONArray("glyph");
        Primitive[] ps = new Primitive[4];
        // there will only be one layer even tho i added the option for multiple layers
        for (int i = 0; i < arr.length(); i++) {
            ps[i] = Primitive.getPrimitive(arr.getString(i));
        }
        levelGoal = new Glyph(GlyphLayer.createLayer(ps));
    }

    private void goNextLevel() {
        if (level == 3) {
            return;
        }

        level++;
        loadNextLevel(level);
        CurrencyManager.getInstance().add(50);
        System.out.println("Leve up");
        System.out.println("Going to level: " + level);
    }

    public int getGoalAmount() {
        return goalAmount;
    }

    public int getStored() {
        return stored;
    }

    public Glyph getLevelGoal() {
        return levelGoal;
    }

    @SuppressWarnings("CallToPrintStackTrace") // the warning in my editor bothers me a lot
    private void loadNextLevel(int lvl) {
        try (InputStream iStream = LevelManager.class
                .getClassLoader()
                .getResourceAsStream("data/level-data.json")) {
            if (iStream == null) {
                throw new RuntimeException("level-data.json not found");
            }

            JSONObject obj = new JSONObject(new JSONTokener(iStream));

            this.levelObj = obj.getJSONObject(String.valueOf(lvl));
            this.goalAmount = levelObj.getInt("amount");
            this.stored = 0;
            loadGoalGlyph();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load leve " + lvl);
        }
    }

}
