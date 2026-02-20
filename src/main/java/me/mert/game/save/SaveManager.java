package me.mert.game.save;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveManager {
    private static final String SAVE_PATH = "save.dat";

    @SuppressWarnings("CallToPrintStackTrace")
    public static void save(SaveData data) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_PATH))) {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static SaveData load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_PATH))) {
            return (SaveData) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
