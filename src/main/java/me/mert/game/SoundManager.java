package me.mert.game;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
    private static final Map<String, Clip> sounds = new HashMap<>();
    // TEMP it was getting annooying; change it to true when needed
    private static boolean canPlay = false;

    public static void init() {
        loadSound("destroy_machine");
        loadSound("place_belt");
        loadSound("place_machine");
    }

    private static void loadSound(String name) {
        try {
            String path = "/sounds/" + name + ".wav";

            InputStream is = SoundManager.class.getResourceAsStream(path);
            AudioInputStream audio = AudioSystem.getAudioInputStream(is);

            Clip clip = AudioSystem.getClip();

            clip.open(audio);
            sounds.put(name, clip);

        } catch (Exception e) {
            System.err.println("Failed to load sound: " + name);
            e.printStackTrace();
        }
    }

    public static void setCanPlay(boolean p) {
        canPlay = p;
    }

    public static void play(String name) {
        if (!canPlay)
            return;

        Clip clip = sounds.get(name);
        if (clip == null)
            return;

        clip.setFramePosition(0);
        clip.start();
    }
}
