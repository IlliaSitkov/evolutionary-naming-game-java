package org.example.strategies.agentInitializer;

import java.util.Random;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.entities.Agent;
import org.example.entities.Lexicon;

import lombok.ToString;

/**
 * Initializes the world with the one language and learning ability from specific range
 */
@ToString
public class ControlledAgentInitializer implements AgentInitializer {

  private String word = null;
  private final double learningAbilityLowerBound;

  public ControlledAgentInitializer(double learningAbilityLowerBound) {
    this.learningAbilityLowerBound = learningAbilityLowerBound;
  }

  public Agent initAgent(VarConfig varConfig, StrategyConfig strategyConfig) {
    Lexicon lexicon = new Lexicon(varConfig.N());
    if (word == null) {
      word = Lexicon.generateWord(varConfig.WORD_LENGTH());
    }
    // seeting the same word (same language) for all of the agents
    lexicon.addWord(word, 1);
    // setting the desired learning ability in [learningAbilityLowerBound, 1.0) range
    double learningAbility = learningAbilityLowerBound + (new Random().nextDouble() * (1 - learningAbilityLowerBound));
    return new Agent(learningAbility, lexicon, varConfig, strategyConfig);
  }

}
