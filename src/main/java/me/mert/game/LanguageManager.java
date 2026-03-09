package me.mert.game;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;

public class LanguageManager {
    private String currentLanguage;
    private JSONObject languagePack;
    private static LanguageManager instance;

    private LanguageManager() {
        loadLanguage("en");
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public void loadLanguage(String lang) {
        try (InputStream iSteam = LanguageManager.class.getClassLoader()
                .getResourceAsStream("data/language/" + lang.toLowerCase() + ".json")) {
            if (iSteam == null) {
                return;
            }
            languagePack = new JSONObject(new String(iSteam.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8));
            currentLanguage = lang;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public String getString(String key) {
        return languagePack.optString(key, key);
    }
}
