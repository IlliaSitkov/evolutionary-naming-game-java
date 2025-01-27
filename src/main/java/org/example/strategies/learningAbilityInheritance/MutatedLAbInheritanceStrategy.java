package org.example.strategies.learningAbilityInheritance;

import java.util.Random;

import org.example.entities.Agent;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class MutatedLAbInheritanceStrategy implements LAbInheritanceStrategy {

    private final double stdDev;

    @Override
    public double inheritLearningAbility(double mutationProbability, Agent parent) {
        Random random = new Random();
        double learningAbility = parent.getLearningAbilityAtBirth() + random.nextGaussian(0, stdDev);
        return Math.max(0, Math.min(1, learningAbility));
    }
}
