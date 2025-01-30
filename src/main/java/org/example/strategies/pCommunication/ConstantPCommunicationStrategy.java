package org.example.strategies.pCommunication;

import java.io.Serializable;

import lombok.ToString;

@ToString
public class ConstantPCommunicationStrategy implements PCommunicationStrategy, Serializable {
    private final double pCommunication;

    public ConstantPCommunicationStrategy(double pCommunication) {
        this.pCommunication = pCommunication;
    }

    @Override
    public double getPCommunication(int iteration) {
        return pCommunication;
    }
}
