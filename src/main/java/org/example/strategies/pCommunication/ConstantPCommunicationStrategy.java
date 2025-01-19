package org.example.strategies.pCommunication;

public class ConstantPCommunicationStrategy implements PCommunicationStrategy {
    private final double pCommunication;

    public ConstantPCommunicationStrategy(double pCommunication) {
        this.pCommunication = pCommunication;
    }

    @Override
    public double getPCommunication(int iteration) {
        return pCommunication;
    }
    
}
