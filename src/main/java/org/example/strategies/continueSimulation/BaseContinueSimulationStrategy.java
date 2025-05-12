package org.example.strategies.continueSimulation;

import org.example.entities.World;

import lombok.ToString;

@ToString
public class BaseContinueSimulationStrategy implements ContinueSimulationStrategy {

  @Override
  public boolean shouldContinueSimulation(int iteration, int maxNIterations, World world) {
    return iteration < maxNIterations && world.hasAgents();
  }
  
}
