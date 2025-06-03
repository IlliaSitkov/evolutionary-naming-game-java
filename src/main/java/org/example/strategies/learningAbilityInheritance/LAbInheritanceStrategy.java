package org.example.strategies.learningAbilityInheritance;

import org.example.entities.Agent;

/**
 * Learning ability inheritance strategy when a new agent is created
 */
public interface LAbInheritanceStrategy {
    double[] inheritLearningAbility(double mutationProbability, Agent parent);
}
