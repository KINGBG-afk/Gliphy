package me.mert.game.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    private static final String SAVE_PATH = "saves/";

    public static List<SaveData> loadAllSaves() {
        List<SaveData> saves = new ArrayList<>();

        File folder = new File("saves");
        if (!folder.exists())
            return saves;

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".dat"));

        if (files == null)
            return saves;

        for (File file : files) {
            SaveData data = load(file);
            if (data != null) {
                saves.add(data);
            }
        }
        return saves;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private static void createFolder() {
        Path path = Paths.get("saves");

        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void save(SaveData data) {
        createFolder(); // if it doesn't exist

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(SAVE_PATH + data.name + ".dat"))) {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static SaveData load(String worldName) {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(SAVE_PATH + worldName + ".dat"))) {
            return (SaveData) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static SaveData load(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {

            return (SaveData) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
