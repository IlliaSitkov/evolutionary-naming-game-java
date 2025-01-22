package org.example.strategies.pSurvival;

import java.util.Random;

import org.example.entities.Agent;
import org.example.entities.World;

public class AvgKnowledgePSurvivalStrategy implements PSurvivalStrategy {

    private final double A;
    private final double B;

    public AvgKnowledgePSurvivalStrategy(double A, double B) {
        this.A = A;
        this.B = B;
    }

    @Override
    public boolean survives(Agent agent, World world) {
        double pSurvival = getSurvivalProbability(world.getAvgKnowledge(), agent.getAge(), agent.getKnowledge());
        double r = new Random().nextDouble();
        return r < pSurvival;
    }

    private double getSurvivalProbability(double avgKnowledge, int age, double agentKnowledge) {
        return (Math.exp(-A * age) * (1 - Math.exp(-B * agentKnowledge / avgKnowledge)));
    }

    @Override
    public String toString() {
        return "AvgKnowledgePSurvivalStrategy";
    }
}