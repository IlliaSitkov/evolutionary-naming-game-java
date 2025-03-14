package org.example.strategies.learningAbilityInheritance;

import java.io.Serializable;
import java.util.Random;

import org.example.entities.Agent;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * Child's learning ability is derived from parent's: l_child = l_parent_at_birth + N(0, epsilon)
 * p_mutation = 1
 */
@AllArgsConstructor
@ToString
public class MutatedLAbInheritanceStrategy implements LAbInheritanceStrategy, Serializable {

    private final double stdDev;

    @Override
    public double[] inheritLearningAbility(double mutationProbability, Agent parent) {
        Random random = new Random();
        double learningAbility = parent.getLearningAbilityAtBirth() + random.nextGaussian(0, stdDev);
        return new double[]{Math.max(0, Math.min(1, learningAbility)), 0};
    }
}
