package org.example.strategies.pSurvival;

import java.util.Random;

import org.example.entities.Agent;
import org.example.entities.World;

import lombok.ToString;

@ToString
public class AvgKnowledgePSurvivalStrategy implements PSurvivalStrategy {

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
    public boolean survives(Agent agent, World world) {
        double pSurvival = getSurvivalProbability(
            world.getWeighedAvgKnowledge(otherAgentsKnowledgeCoefficient, agent.getX(), agent.getY()),
            agent.getAge(),
            agent.getKnowledge());
        double r = new Random().nextDouble();
        return r < pSurvival;
    }

    private double getSurvivalProbability(double avgKnowledge, int age, double agentKnowledge) {
        return (Math.exp(-A * age) * (1 - Math.exp(-B * agentKnowledge / avgKnowledge)));
    }
}