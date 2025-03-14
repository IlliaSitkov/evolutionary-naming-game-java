package org.example.strategies.pSurvival;

import org.example.entities.Agent;
import org.example.entities.World;

/**
 * Survival strategy to determine if an agent survives
 */
public interface PSurvivalStrategy {
  /**
   * res[0] - true/false
   * res[1] - p_surv
   */
  double[] survives(Agent agent, World world);
}
