package org.example.strategies.pCommunication;

public class SingleStepPCommunicationStrategy implements PCommunicationStrategy {
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

    @Override
    public String toString() {
        return "SingleStepPCommunicationStrategy{" +
                "initialProbability=" + initialProbability +
                ", changeIteration=" + changeIteration +
                ", changedProbability=" + changedProbability +
                '}';
    }
}