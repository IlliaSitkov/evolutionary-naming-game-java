package org.example.strategies.wordAcquisition;

import org.example.entities.Agent;

import lombok.ToString;

@ToString
public class LAbWordAquisitionStrategy implements WordAcquisitionStrategy {
    @Override
    public double getInventedWordWeight(Agent agent) {
        return agent.getLearningAbility();
    }

    @Override
    public double getLearntWordWeight(Agent agent) {
        return agent.getLearningAbility();
    }

    @Override
    public double getInheritedWordWeight(double inheritedLearningAbility) {
        return inheritedLearningAbility;
    }
    
}
