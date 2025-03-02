package org.example.strategies.evolution;

import java.io.Serializable;

import lombok.ToString;

/**
 * Evolution Process as in Lipowska ENG: p_pop_upd = 1 - p_comm
 */
@ToString
public class FullEvolutionStrategy implements EvolutionStrategy, Serializable {
    
    public boolean[] determineEvolutionSteps(double pCommunication) {
        return new boolean[] {true, true};
    }
    
}
