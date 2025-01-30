package org.example.strategies.learningAbilityAging;

import java.io.Serializable;

import org.example.entities.Agent;

import lombok.ToString;

@ToString
public class ConstantLAbAgingStrategy implements LAbAgingStrategy, Serializable {
    @Override
    public double ageLearningAbility(Agent agent) {
        return agent.getLearningAbility();
    }
    
}
