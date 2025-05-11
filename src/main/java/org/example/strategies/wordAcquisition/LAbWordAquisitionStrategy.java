package org.example.strategies.wordAcquisition;

import java.io.Serializable;
import java.util.Optional;

import org.example.entities.Agent;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * The weight of invented and learnt words is equal to the learning ability of the agent,
 * the weight of inherited words is equal to the inherited learning ability
 */
@ToString
@AllArgsConstructor
public class LAbWordAquisitionStrategy implements WordAcquisitionStrategy, Serializable {

    private final Double inventedLAb;
    private final Double learntLAb;
    private final Double inheritedLAb;


    @Override
    public double getInventedWordWeight(Agent agent) {
        return Optional.ofNullable(inventedLAb).orElse(agent.getLearningAbility());
    }

    @Override
    public double getLearntWordWeight(Agent agent) {
        return Optional.ofNullable(learntLAb).orElse(agent.getLearningAbility());
    }

    @Override
    public double getInheritedWordWeight(double inheritedLearningAbility) {
        return Optional.ofNullable(inheritedLAb).orElse(inheritedLearningAbility);
    }
    
}
