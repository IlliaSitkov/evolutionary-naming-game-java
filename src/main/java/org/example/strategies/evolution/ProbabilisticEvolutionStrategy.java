package org.example.strategies.evolution;

import java.io.Serializable;
import java.util.Random;

import lombok.ToString;

@ToString
public class ProbabilisticEvolutionStrategy implements EvolutionStrategy, Serializable {
    
    public boolean[] determineEvolutionSteps(double pCommunication) {
        double rCommunication = new Random().nextDouble();
        boolean shouldCommunicate = rCommunication < pCommunication;
        return new boolean[] {shouldCommunicate, !shouldCommunicate};
    }

}
