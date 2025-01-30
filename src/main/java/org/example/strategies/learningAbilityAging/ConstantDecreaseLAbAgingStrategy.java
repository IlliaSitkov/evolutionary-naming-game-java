package org.example.strategies.learningAbilityAging;

import java.io.Serializable;

import org.example.entities.Agent;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ConstantDecreaseLAbAgingStrategy  implements LAbAgingStrategy, Serializable {
    private final int startAge;

    @Override
    public double ageLearningAbility(Agent agent) {
        if (agent.getAge() + 1 < startAge) {
            return agent.getLearningAbility();
        }
        return agent.getLearningAbility() - 0.01 * agent.getLearningAbilityAtBirth();
    }
    
}
