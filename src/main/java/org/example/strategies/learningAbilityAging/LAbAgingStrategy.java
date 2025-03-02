package org.example.strategies.learningAbilityAging;

import org.example.entities.Agent;

/**
 * Learning Ability Aging Strategy (learning ability changing over agent's age)
 */
public interface LAbAgingStrategy {
    double ageLearningAbility(Agent agent);
}
