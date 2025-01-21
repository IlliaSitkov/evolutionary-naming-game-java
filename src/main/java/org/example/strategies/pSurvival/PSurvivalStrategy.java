package org.example.strategies.pSurvival;

import org.example.entities.Agent;
import org.example.entities.World;

public interface PSurvivalStrategy {
  boolean survives(Agent agent, World world);
}
