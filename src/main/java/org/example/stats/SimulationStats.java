package org.example.stats;

import java.util.ArrayList;
import java.util.List;

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

    public void recordBeforeEvolution(World world) {
        recordCommonWorldStats(world);
    }

    public void recordIteration(IterationStats iterationStats) {
        successRates.add(iterationStats.getSuccessRate());
        communicationsNumber.add(iterationStats.getNCommunications());
        killedAgentsNumber.add(iterationStats.getNKilledAgents());
    }

    public void recordAfterIteration(World world) {
        recordCommonWorldStats(world);
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
}