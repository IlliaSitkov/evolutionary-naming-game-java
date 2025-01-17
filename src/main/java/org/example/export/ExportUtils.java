package org.example.export;

import org.example.entities.Agent;
import org.example.entities.World;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ExportUtils {

    public static void exportLearningAbilities(World world, String fileName) {
        int size = world.getSize();
        double[][] learningAbilities = new double[size][size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Agent agent = world.getAgentAt(x, y);
                if (agent != null) {
                    learningAbilities[y][x] = agent.getLearningAbility();
                } else {
                    learningAbilities[y][x] = -1;
                }
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(fileName), learningAbilities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
