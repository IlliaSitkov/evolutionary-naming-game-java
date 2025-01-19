package org.example.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.entities.Agent;
import org.example.entities.World;
import org.example.strategies.pCommunication.PCommunicationStrategy;

import lombok.Getter;

public class SimulationStats {
    @Getter
    private final List<Double> successRates = new ArrayList<>();
    @Getter
    private final List<Double> avgLearningAbilities = new ArrayList<>();
    @Getter
    private final List<Integer> languagesNumber = new ArrayList<>();
    @Getter
    private final List<Integer> communicationsNumber = new ArrayList<>();
    @Getter
    private final List<Double> avgAges = new ArrayList<>();
    @Getter
    private final List<Integer> killedAgentsNumber = new ArrayList<>();
    @Getter
    private final List<Double> avgKnowledge = new ArrayList<>();
    @Getter
    private final Map<String, double[][]> learningAbilityMaps = new HashMap<>();
    @Getter
    private final Map<String, String[][]> languageMaps = new HashMap<>();

    private final List<Integer> iterationsToSaveMaps;
    private final List<Double> pCommunicationsToSaveMaps;

    public SimulationStats(List<Integer> iterationsToSaveMaps, List<Double> pCommunicationsToSaveMaps) {
        this.iterationsToSaveMaps = new ArrayList<>(iterationsToSaveMaps);
        this.pCommunicationsToSaveMaps = new ArrayList<>(pCommunicationsToSaveMaps);
    }

    public void recordBeforeEvolution(World world, double pCommunication) {
        recordCommonWorldStats(world);
        recordWorldLearningAbilities(world, -1, pCommunication);
        recordWorldLanguages(world, -1, pCommunication);
    }

    public void recordIteration(IterationStats iterationStats) {
        successRates.add(iterationStats.getSuccessRate());
        communicationsNumber.add(iterationStats.getNCommunications());
        killedAgentsNumber.add(iterationStats.getNKilledAgents());
    }

    public void recordAfterIteration(World world, int iteration, double pCommunication) {
        recordCommonWorldStats(world);
        if (shouldSaveMaps(iteration, pCommunication)) {
            recordWorldLearningAbilities(world, iteration, pCommunication);
            recordWorldLanguages(world, iteration, pCommunication);
            pCommunicationsToSaveMaps.removeIf((pComm) -> pCommunication >= pComm);
        }
    }

    private boolean shouldSaveMaps(int iteration, double pCommunication) {
        return iterationsToSaveMaps.contains(iteration) ||
        pCommunicationsToSaveMaps
        .stream()
        .anyMatch((pComm) -> pCommunication >= pComm);
    }

    public void recordCommonWorldStats(World world) {
        World.Stats stats = world.getStats();
        avgLearningAbilities.add(stats.getAvgLearningAbility());
        languagesNumber.add(stats.getLanguagesNumber());
        avgAges.add(stats.getAvgAge());
        avgKnowledge.add(stats.getAvgKnowledge());
    }

    public static List<Double> getPCommunicationOverIterations(PCommunicationStrategy strategy, int nIters) {
        List<Double> pCommunications = new ArrayList<>();
        for (int i = 0; i < nIters; i++) {
            pCommunications.add(strategy.getPCommunication(i));
        }
        return pCommunications;
    }

    public void recordWorldLearningAbilities(World world, int iteration, double pCommunication) {
        int size = world.getSize();
        double[][] learningAbilities = new double[size][size];

        for (int y = 0; y < world.getSize(); y++) {
            for (int x = 0; x < world.getSize(); x++) {
                Agent agent = world.getAgentAt(x, y);
                if (agent != null) {
                    learningAbilities[y][x] = agent.getLearningAbility();
                } else {
                    learningAbilities[y][x] = -1;
                }
            }
        }

        String key = String.format("p_comm_%.4f_it_%d", pCommunication, iteration);
        learningAbilityMaps.put(key, learningAbilities);
    }

    public void recordWorldLanguages(World world, int iteration, double pCommunication) {
        int size = world.getSize();
        String[][] languages = new String[size][size];

        for (int y = 0; y < world.getSize(); y++) {
            for (int x = 0; x < world.getSize(); x++) {
                Agent agent = world.getAgentAt(x, y);
                if (agent != null) {
                    languages[y][x] = agent.getLexicon().isEmpty() ? "-" : agent.getLexicon().getTopWord();
                } else {
                    languages[y][x] = "";
                }
            }
        }

        String key = String.format("p_comm_%.4f_it_%d", pCommunication, iteration);
        languageMaps.put(key, languages);
    }
}