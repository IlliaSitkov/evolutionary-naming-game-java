package org.example.strategies.pCommunication;

import java.io.Serializable;

import lombok.ToString;

/**
 * Communication probability changes once from initialProbability to changedProbability at changeIteration
 */
@ToString
public class SingleStepPCommunicationStrategy implements PCommunicationStrategy, Serializable {
    private final double initialProbability;
    private final int changeIteration;
    private final double changedProbability;

    public SingleStepPCommunicationStrategy(double initialProbability, int changeIteration, double changedProbability) {
        this.initialProbability = initialProbability;
        this.changeIteration = changeIteration;
        this.changedProbability = changedProbability;
    }

    @Override
    public double getPCommunication(int iteration) {
        if (iteration >= changeIteration) {
            return changedProbability;
        }
        return initialProbability;
    }
}