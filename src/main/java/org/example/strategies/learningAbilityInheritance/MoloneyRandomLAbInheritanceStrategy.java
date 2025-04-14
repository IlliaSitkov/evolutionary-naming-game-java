package org.example.strategies.learningAbilityInheritance;

import java.io.Serializable;
import java.util.Random;

import org.example.entities.Agent;

import lombok.ToString;

/**
 * Child inherits parent's learning ability at birth with p_mutation probability, otherwise acquires random learning ability
 */
@ToString
public class MoloneyRandomLAbInheritanceStrategy implements LAbInheritanceStrategy, Serializable {
    @Override
    public double[] inheritLearningAbility(double mutationProbability, Agent parent) {
        Random random = new Random();
        double rLearningAbility = random.nextDouble();
        double newLearningAbility;
        if (rLearningAbility >= mutationProbability) {
            newLearningAbility = parent.getLearningAbilityAtBirth();
        } else {
            newLearningAbility = random.nextDouble();
        }
        return new double[]{newLearningAbility, rLearningAbility};
    }
}
