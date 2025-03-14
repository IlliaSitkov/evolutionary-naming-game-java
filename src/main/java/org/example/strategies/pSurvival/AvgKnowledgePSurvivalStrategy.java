package org.example.strategies.pSurvival;

import java.io.Serializable;
import java.util.Random;

import org.example.entities.Agent;
import org.example.entities.World;

import lombok.ToString;

/**
 * The survival probability of an agent is determined by the average knowledge of all agents in the world
 * (other agents' knowledge is weighed by otherAgentsKnowledgeCoefficient)
 * 
 * p_surv = exp(-A * age) * (1 - exp(-B * agentKnowledge / avgKnowledge))
 */
@ToString
public class AvgKnowledgePSurvivalStrategy implements PSurvivalStrategy, Serializable {

    private final double A;
    private final double B;
    private final double otherAgentsKnowledgeCoefficient;

    public AvgKnowledgePSurvivalStrategy(double A, double B, double otherAgentsKnowledgeCoefficient) {
        this.A = A;
        this.B = B;
        this.otherAgentsKnowledgeCoefficient = otherAgentsKnowledgeCoefficient;
    }

    public AvgKnowledgePSurvivalStrategy(double A, double B) {
      this(A, B, 1);
    }

    @Override
    public double[] survives(Agent agent, World world) {
        double pSurvival = getSurvivalProbability(
            world.getWeighedAvgKnowledge(otherAgentsKnowledgeCoefficient, agent.getX(), agent.getY()),
            agent.getAge(),
            agent.getKnowledge());
        double r = new Random().nextDouble();
        return new double[]{r < pSurvival ? 1 : 0, pSurvival};
    }

    private double getSurvivalProbability(double avgKnowledge, int age, double agentKnowledge) {
        return (Math.exp(-A * age) * (1 - Math.exp(-B * agentKnowledge / avgKnowledge)));
    }
}