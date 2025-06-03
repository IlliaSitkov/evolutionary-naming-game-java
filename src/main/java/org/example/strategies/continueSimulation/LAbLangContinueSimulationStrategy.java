package org.example.strategies.continueSimulation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.entities.Agent;
import org.example.entities.World;

import lombok.ToString;

@ToString
public class LAbLangContinueSimulationStrategy extends BaseContinueSimulationStrategy {

  private final double languageThreshold;
  private final double learningAbilityThreshold;

  public LAbLangContinueSimulationStrategy() {
    this(0.99, 0.95);
  }

  public LAbLangContinueSimulationStrategy(double languageThreshold, double learningAbilityThreshold) {
    this.languageThreshold = languageThreshold;
    this.learningAbilityThreshold = learningAbilityThreshold;
  }

  @Override
  public boolean shouldContinueSimulation(int iteration, int maxNIterations, World world) {
    if (isLanguageUniform(world) && world.getStats().getAvgLearningAbility() > learningAbilityThreshold) {
      return false;
    }
    return super.shouldContinueSimulation(iteration, maxNIterations, world);
  }

  private boolean isLanguageUniform(World world) {
    Map<String, Integer> languageCounts = new HashMap<>();
    List<Agent> agents = world.getAgents();

    for (Agent agent : agents) {
      String language = agent.getLexicon().isEmpty() ? "-" : agent.getLexicon().getTopWord();
      languageCounts.put(language, languageCounts.getOrDefault(language, 0) + 1);
    }

    for (int count : languageCounts.values()) {
      if ((double) count / agents.size() >= languageThreshold) {
        return true;
      }
    }

    return false;
  }

}
