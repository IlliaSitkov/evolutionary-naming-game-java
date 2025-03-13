package org.example.strategies.agentInitializer;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.entities.Agent;

public interface AgentInitializer {
  Agent initAgent(VarConfig varConfig, StrategyConfig strategyConfig);
}
