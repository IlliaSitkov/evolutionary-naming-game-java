package org.example.strategies.wordAcquisition;

import org.example.entities.Agent;

/**
 * Word acquisition strategy to determine the weight of invented, learnt and inherited words
 */
public interface WordAcquisitionStrategy {
    double getInventedWordWeight(Agent agent);

    double getLearntWordWeight(Agent agent);

    double getInheritedWordWeight(double inheritedLearningAbility);
}
