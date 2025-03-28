package org.example.strategies.pCommunication;

import java.io.Serializable;

import lombok.ToString;

/**
 * Communication probability continuously changes from initialProbability to finalProbability over nSteps
 */
@ToString
public class ContinuousIncreasePCommunicationStrategy implements PCommunicationStrategy, Serializable {
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
