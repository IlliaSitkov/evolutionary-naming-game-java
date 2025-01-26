package org.example.strategies.evolution;

import lombok.ToString;

@ToString
public class FullEvolutionStrategy implements EvolutionStrategy {
    
    public boolean[] determineEvolutionSteps(double pCommunication) {
        return new boolean[] {true, true};
    }
    
}
