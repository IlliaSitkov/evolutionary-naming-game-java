package org.example.strategies.pCommunication;

/**
 * Communication probability change over iterations strategy
 */
public interface PCommunicationStrategy {
    double getPCommunication(int iteration);
}