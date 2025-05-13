package org.example.strategies.evolution;

import java.util.Random;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class ConstantPUpdEvolutionStrategy implements EvolutionStrategy  {

  private final double pPopulationUpdate;

  @Override
  public boolean[] determineEvolutionSteps(double pCommunication) {
    Random random = new Random();
    double rCommunication = random.nextDouble();
    boolean shouldCommunicate = rCommunication < pCommunication;
    double rPopulationUpdate = random.nextDouble();
    boolean shouldUpdatePopulation = rPopulationUpdate < pPopulationUpdate;
    return new boolean[] {shouldCommunicate, shouldUpdatePopulation};
  }

}
