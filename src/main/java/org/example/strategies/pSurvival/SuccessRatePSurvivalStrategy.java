package org.example.strategies.pSurvival;

import java.io.Serializable;
import java.util.Random;

import org.example.entities.Agent;
import org.example.entities.World;

import lombok.ToString;

/**
 * The survival probability is a function of the agent's age and success rate
 * successRate = nSuccessfulCommunications / nInitedCommunications
 * p_surv = exp(-A * age) * successRate^C
 */
@ToString
public class SuccessRatePSurvivalStrategy implements PSurvivalStrategy, Serializable {

    private final double A;
    private final double C;

    public SuccessRatePSurvivalStrategy(double A, double C) {
        this.A = A;
        this.C = C;
    }

    @Override
    public boolean survives(Agent agent, World world) {
        double pSurvival = getSurvivalProbability(agent.getAge(), agent.getSuccessRate());
        double r = new Random().nextDouble();
        return r < pSurvival;
    }

    private double getSurvivalProbability(int age, double successRate) {
        return (Math.exp(-A * age) * Math.pow(successRate, C));
    }
}
