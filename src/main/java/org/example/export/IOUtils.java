package org.example.export;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
}
