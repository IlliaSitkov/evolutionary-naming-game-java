package org.example.stats;

import java.util.ArrayList;
import java.util.List;

import org.example.entities.World;

import lombok.Getter;

public class SimulationStats {
    @Getter
    private final List<Double> successRates = new ArrayList<>();
    @Getter
    private final List<Double> avgLearningAbilities = new ArrayList<>();

    public void recordBeforeEvolution(World world) {
        avgLearningAbilities.add(world.getAvgLearningAbility());
    }

    public void recordIteration(IterationStats iterationStats) {
        successRates.add(iterationStats.getSuccessRate());
    }

    public void recordAfterIteration(World world) {
        avgLearningAbilities.add(world.getAvgLearningAbility());
    }
}