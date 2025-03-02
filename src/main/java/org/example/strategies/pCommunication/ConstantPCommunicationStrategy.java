package org.example.strategies.pCommunication;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * The communication probability is constant over all iterations
 */
@ToString
@AllArgsConstructor
public class ConstantPCommunicationStrategy implements PCommunicationStrategy, Serializable {
    private final double pCommunication;

    @Override
    public double getPCommunication(int iteration) {
        return pCommunication;
    }
}
