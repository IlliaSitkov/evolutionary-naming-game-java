package org.example.strategies.wordAcquisition;

import org.example.entities.Agent;

public interface WordAcquisitionStrategy {
    double getInventedWordWeight(Agent agent);

    double getLearntWordWeight(Agent agent);

    double getInheritedWordWeight(double inheritedLearningAbility);
}
