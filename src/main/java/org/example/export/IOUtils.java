package org.example.export;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.entities.World;
import org.example.simulation.Simulation;

public class IOUtils {

    private static final String rootFolder = "out/";

    public static synchronized void exportToJson(Object object, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(fileName), object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void saveWorld(World world, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(world);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized World loadWorld(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (World) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized void createDirectory(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static synchronized void writeStringToFile(String content, String filePath) {
        try {
            Path path = Files.writeString(Paths.get(filePath), content);
            System.out.println("File written to: " + path.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void saveSimulationConfig(String folderName, Simulation simulation) {
        saveRunConfig(folderName, simulation.getVarConfig(), simulation.getStrategyConfig());
    }

    public static synchronized void saveRunConfig(String folderName, VarConfig varConfig,
            StrategyConfig strategyConfig) {
        String directory = IOUtils.rootFolder + folderName;
        createDirectory(directory);

        String output = varConfig + "\n\n" + strategyConfig;
        writeStringToFile(output, directory + "/config.txt");
    }

    public static synchronized void exportToCSV(String filePath, int nIterations, List<Object[]> headerDataPairs) {
        filePath = IOUtils.rootFolder + filePath;
        File file = new File(filePath);
    
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
    
        try (FileWriter writer = new FileWriter(file, false)) {
            List<String> headers = new ArrayList<>();
            List<List<Object>> dataLists = new ArrayList<>();
    
            for (Object[] pair : headerDataPairs) {
                headers.add((String) pair[0]);
                dataLists.add(new ArrayList<>((List<Object>) pair[1]));
            }
    
            writer.append("It");
            for (String header : headers) {
                writer.append(",").append(header);
            }
            writer.append("\n");
    
            for (List<Object> list : dataLists) {
                while (list.size() < nIterations) {
                    list.add(0, "---");
                }
            }
    
            for (int i = 0; i < nIterations; i++) {
                writer.append(String.valueOf(i));
                for (List<Object> list : dataLists) {
                    Object value = list.get(i);
                    if (value == null) {
                        writer.append(",").append("---");
                    } else {
                        writer.append(",").append(value.toString());
                    }
                }
                writer.append("\n");
            }
    
            System.out.println("CSV file created successfully: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
