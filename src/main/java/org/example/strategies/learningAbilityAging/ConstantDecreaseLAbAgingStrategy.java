package org.example.strategies.learningAbilityAging;

import org.example.entities.Agent;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ConstantDecreaseLAbAgingStrategy  implements LAbAgingStrategy {
    private final int startAge;

    @Override
    public double ageLearningAbility(Agent agent) {
        if (agent.getAge() + 1 < startAge) {
            return agent.getLearningAbility();
        }
        return agent.getLearningAbility() - 0.01 * agent.getLearningAbilityAtBirth();
    }
    
}
