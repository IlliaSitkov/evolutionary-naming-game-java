package org.example.strategies.wordAcquisition;

import java.io.Serializable;

import org.example.entities.Agent;

import lombok.ToString;

@ToString
public class LAbWordAquisitionStrategy implements WordAcquisitionStrategy, Serializable {
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
