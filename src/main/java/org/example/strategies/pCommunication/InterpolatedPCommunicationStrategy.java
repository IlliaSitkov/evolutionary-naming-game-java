package org.example.strategies.pCommunication;

import java.io.Serializable;

import lombok.ToString;

/**
 * Communication probability continuously changes from initialProbability to
 * finalProbability over nSteps with linear interpolation
 */
@ToString
public class InterpolatedPCommunicationStrategy implements PCommunicationStrategy, Serializable {
    private final double initialProbability;
    private final double finalProbability;
    private final int nSteps;

    public InterpolatedPCommunicationStrategy(double initialProbability, double finalProbability, int nSteps) {
        this.initialProbability = initialProbability;
        this.finalProbability = finalProbability;
        this.nSteps = nSteps;
    }

    @Override
    public double getPCommunication(int iteration) {
        double t = iteration / (nSteps - 1.0);
        return initialProbability + (finalProbability - initialProbability) * t;
    }

}
