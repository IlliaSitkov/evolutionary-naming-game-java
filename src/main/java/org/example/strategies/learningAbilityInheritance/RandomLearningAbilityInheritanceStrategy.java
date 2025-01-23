package org.example.strategies.learningAbilityInheritance;

import java.util.Random;

import org.example.entities.Agent;

public class RandomLearningAbilityInheritanceStrategy implements LearningAbilityInheritanceStrategy {
    @Override
    public double inheritLearningAbility(double mutationProbability, Agent parent) {
        Random random = new Random();
        double rLearningAbility = random.nextDouble();
        double newLearningAbility;
        if (rLearningAbility < mutationProbability) {
            newLearningAbility = random.nextDouble();
        } else {
            newLearningAbility = parent.getLearningAbility();
        }
        return newLearningAbility;
    }

    @Override
    public String toString() {
        return "RandomLearningAbilityInheritanceStrategy";
    }
}
