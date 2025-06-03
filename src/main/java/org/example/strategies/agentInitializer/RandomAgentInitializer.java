package org.example.strategies.agentInitializer;

import java.util.Random;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.entities.Agent;
import org.example.entities.Lexicon;

import lombok.ToString;

/**
 * Original random initialization of the world
 */
@ToString
public class RandomAgentInitializer implements AgentInitializer {

  public Agent initAgent(VarConfig varConfig, StrategyConfig strategyConfig) {
    Lexicon lexicon = new Lexicon(varConfig.N());
    lexicon.addWord(Lexicon.generateWord(varConfig.WORD_LENGTH()), 1);
    return new Agent(new Random().nextDouble(), lexicon, varConfig, strategyConfig);
  }
  
}
