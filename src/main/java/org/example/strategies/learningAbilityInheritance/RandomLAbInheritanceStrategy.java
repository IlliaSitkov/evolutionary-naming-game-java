package org.example.strategies.learningAbilityInheritance;

import java.io.Serializable;
import java.util.Random;

import org.example.entities.Agent;

import lombok.ToString;

@ToString
public class RandomLAbInheritanceStrategy implements LAbInheritanceStrategy, Serializable {
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
}
