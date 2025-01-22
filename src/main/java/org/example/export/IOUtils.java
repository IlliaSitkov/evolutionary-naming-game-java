package org.example.export;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.entities.World;

public class IOUtils {

    public static void exportToJson(Object object, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(fileName), object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveWorld(World world, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(world);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static World loadWorld(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (World) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createDirectory(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static void writeStringToFile(String content, String filePath) {
        try {
            Path path = Files.writeString(Paths.get(filePath), content);
            System.out.println("File written to: " + path.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveRunConfig(String folderName, VarConfig varConfig, StrategyConfig strategyConfig) {
        String directory = "out/" + folderName;
        createDirectory(directory);

        String output = varConfig + "\n\n" + strategyConfig;
        writeStringToFile(output, directory + "/config.txt");
    }
}
