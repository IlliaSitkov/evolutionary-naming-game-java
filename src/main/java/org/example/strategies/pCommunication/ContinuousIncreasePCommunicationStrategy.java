package org.example.strategies.pCommunication;

import lombok.ToString;

@ToString
public class ContinuousIncreasePCommunicationStrategy implements PCommunicationStrategy {
    private final double initialProbability;
    private final double finalProbability;
    private final int nSteps;

    public ContinuousIncreasePCommunicationStrategy(double initialProbability, double finalProbability, int nSteps) {
        this.initialProbability = initialProbability;
        this.finalProbability = finalProbability;
        this.nSteps = nSteps <= 1 ? 2 : nSteps;
    }

    @Override
    public double getPCommunication(int iteration) {
        return initialProbability + (finalProbability - initialProbability) * iteration / (nSteps - 1);
    }

}
