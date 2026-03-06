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
    private static final Path SAVE_PATH = getSavePath();

    // chooses the save path depending on the OS
    private static Path getSavePath() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            String home = System.getProperty("user.home");
            return Paths.get(home, "AppData", "Local", "Gliphy", "saves");
        } else {
            return Paths.get("saves");
        }
    }

    public static List<SaveData> loadAllSaves() {
        createFolder();
        List<SaveData> saves = new ArrayList<>();

        File[] files = SAVE_PATH.toFile().listFiles((dir, name) -> name.toLowerCase().endsWith(".dat"));

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
        try {
            Files.createDirectories(SAVE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void save(SaveData data) {
        createFolder(); // if it doesn't exist

        Path file = SAVE_PATH.resolve(data.name + ".dat");

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file.toFile()))) {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static SaveData load(String worldName) {
        createFolder();
        Path file = SAVE_PATH.resolve(worldName + ".dat");

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.toFile()))) {
            return (SaveData) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static SaveData load(File file) {
        createFolder();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (SaveData) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void delete(String worldName) {
        Path file = SAVE_PATH.resolve(worldName + ".dat");

        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
