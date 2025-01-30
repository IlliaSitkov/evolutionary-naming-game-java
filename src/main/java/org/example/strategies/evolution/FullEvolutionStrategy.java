package org.example.strategies.evolution;

import java.io.Serializable;

import lombok.ToString;

@ToString
public class FullEvolutionStrategy implements EvolutionStrategy, Serializable {
    
    public boolean[] determineEvolutionSteps(double pCommunication) {
        return new boolean[] {true, true};
    }
    
}
