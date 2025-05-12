package org.example.strategies.continueSimulation;

import org.example.entities.World;

public interface ContinueSimulationStrategy {
  
  boolean shouldContinueSimulation(int iteration, int maxNIterations, World world);

}
