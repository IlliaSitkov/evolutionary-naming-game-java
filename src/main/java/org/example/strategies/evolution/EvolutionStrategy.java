package org.example.strategies.evolution;

/**
 * Evolution Process Strategy
 */
public interface EvolutionStrategy {
    /**
     * @return [0] - should the communication happen, [1] - should the population update happen
     */
    boolean[] determineEvolutionSteps(double pCommunication);
}
