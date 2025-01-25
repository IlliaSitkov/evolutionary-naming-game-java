package org.example.strategies.learningAbilityAging;

import org.example.entities.Agent;

import lombok.ToString;

@ToString
public class ConstantLAbAgingStrategy implements LAbAgingStrategy {
    @Override
    public double ageLearningAbility(Agent agent) {
        return agent.getLearningAbility();
    }
    
}
