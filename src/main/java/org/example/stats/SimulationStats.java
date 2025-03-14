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
    private final List<Integer> successfulCommunicationsNumber = new ArrayList<>();
    @Getter
    private final List<Double> avgAges = new ArrayList<>();
    @Getter
    private final List<Integer> killedAgentsNumber = new ArrayList<>();
    @Getter
    private final List<Integer> bornAgentsNumber = new ArrayList<>();
    @Getter
    private final List<Double> avgKnowledge = new ArrayList<>();
    @Getter
    private final List<Double> avgPSurvs = new ArrayList<>();
    @Getter
    private final Map<String, double[][]> learningAbilityMaps = new HashMap<>();
    @Getter
    private final Map<String, String[][]> languageMaps = new HashMap<>();
    @Getter
    private final List<Integer> nAgentsAlive = new ArrayList<>();

    private final List<Integer> iterationsToSaveMaps;
    private final List<Double> pCommunicationsToSaveMaps;
    private final int nSkipIterations;

    public SimulationStats(List<Integer> iterationsToSaveMaps, List<Double> pCommunicationsToSaveMaps) {
        this.iterationsToSaveMaps = new ArrayList<>(iterationsToSaveMaps);
        this.pCommunicationsToSaveMaps = new ArrayList<>(pCommunicationsToSaveMaps);
        this.nSkipIterations = 0;
    }

    public SimulationStats(int nSkipIterations) {
        this.iterationsToSaveMaps = new ArrayList<>();
        this.pCommunicationsToSaveMaps = new ArrayList<>();
        this.nSkipIterations = nSkipIterations;
    }

    public SimulationStats() {
        this(0);
    }

    public void recordBeforeEvolution(World world, double pCommunication) {
        if (nSkipIterations > 0) {
            return;
        }
        recordCommonWorldStats(world);
        recordWorldLearningAbilities(world, -1, pCommunication);
        recordWorldLanguages(world, -1, pCommunication);
    }

    public void recordIteration(IterationStats iterationStats) {
        if (iterationStats.getIteration() < nSkipIterations) {
            return;
        }
        successRates.add(iterationStats.getSuccessRate());
        communicationsNumber.add(iterationStats.getNCommunications());
        successfulCommunicationsNumber.add(iterationStats.getNSuccessfulCommunications());
        killedAgentsNumber.add(iterationStats.getNKilledAgents());
        bornAgentsNumber.add(iterationStats.getNBornAgents());
        avgPSurvs.add(iterationStats.getAvgPSurv());
    }

    public void recordAfterIteration(World world, int iteration, double pCommunication) {
        if (iteration < nSkipIterations) {
            return;
        }
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

    private void recordCommonWorldStats(World world) {
        World.Stats stats = world.getStats();
        avgLearningAbilities.add(stats.getAvgLearningAbility());
        languagesNumber.add(stats.getLanguagesNumber());
        avgAges.add(stats.getAvgAge());
        avgKnowledge.add(stats.getAvgKnowledge());
        nAgentsAlive.add(world.getAgents().size());
    }

    public static List<Double> getPCommunicationOverIterations(PCommunicationStrategy strategy, int nIters) {
        List<Double> pCommunications = new ArrayList<>();
        for (int i = 0; i < nIters; i++) {
            pCommunications.add(strategy.getPCommunication(i));
        }
        return pCommunications;
    }

    private void recordWorldLearningAbilities(World world, int iteration, double pCommunication) {
        double[][] learningAbilities = new double[world.getRows()][world.getCols()];

        for (int y = 0; y < world.getRows(); y++) {
            for (int x = 0; x < world.getCols(); x++) {
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

    private void recordWorldLanguages(World world, int iteration, double pCommunication) {
        String[][] languages = new String[world.getRows()][world.getCols()];

        for (int y = 0; y < world.getRows(); y++) {
            for (int x = 0; x < world.getCols(); x++) {
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

    public double getAvgLearningAbility() {
        return avgLearningAbilities.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    public double getAvgSuccessRate() {
        return successRates.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    
}