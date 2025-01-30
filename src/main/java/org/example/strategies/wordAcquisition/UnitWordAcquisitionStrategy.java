package org.example.strategies.wordAcquisition;

import java.io.Serializable;

import org.example.entities.Agent;

import lombok.ToString;

@ToString
public class UnitWordAcquisitionStrategy implements WordAcquisitionStrategy, Serializable {
    @Override
    public double getInventedWordWeight(Agent agent) {
        return 1;
    }

    @Override
    public double getLearntWordWeight(Agent agent) {
        return 1;
    }

    @Override
    public double getInheritedWordWeight(double inheritedLearningAbility) {
        return 1;
    }
    
}
