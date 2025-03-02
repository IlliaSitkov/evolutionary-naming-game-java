package org.example.strategies.pSurvival;

import org.example.entities.Agent;
import org.example.entities.World;

/**
 * Survival strategy to determine if an agent survives
 */
public interface PSurvivalStrategy {
  boolean survives(Agent agent, World world);
}
