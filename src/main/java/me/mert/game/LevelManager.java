package me.mert.game;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import me.mert.core.enums.Primitive;
import me.mert.glyph.Glyph;
import me.mert.glyph.GlyphLayer;

public class LevelManager {
    private int stored;
    private int level;
    private Glyph levelGoal;
    private JSONObject levelObj;

    private boolean checkLevelCompleted() {
        return stored <= levelObj.getInt("amount");
    }

    // there will only be one layer even tho i added the option for multiple layers
    private void getLevelGoal() {
        JSONArray arr = levelObj.getJSONArray("glyph");
        Primitive[] ps = new Primitive[4];
        for (int i = 0; i < arr.length(); i++) {
            ps[i] = Primitive.getPrimitive(arr.getString(i));
        }
        levelGoal = new Glyph(GlyphLayer.createLayer(ps));
    }

    public void addToStored(Glyph g) {
        if (g.equals(levelGoal)) {
            stored++;
        }
    }

    public boolean goNextLevel() {
        if (level == 3) {
            return false;
        }

        if (checkLevelCompleted()) {
            level++;
            loadNextLevel(level);
            return true;
        }

        return false;
    }

    private void loadNextLevel(int lvl) {
        try (InputStream iStream = LevelManager.class.getClassLoader().getResourceAsStream("data/level-data.json")) {
            if (iStream == null) {
                throw new RuntimeException("level-data.json is not found");
            }

            JSONObject obj = new JSONObject(new JSONTokener(iStream));
            this.levelObj = obj.getJSONObject(String.valueOf(lvl));
            this.stored = 0;
            getLevelGoal();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load leve " + lvl);
        }
    }

}
