package org.example.entities;

import java.io.Serializable;
import java.util.Random;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.strategies.learningAbilityAging.LAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.LearningAbilityInheritanceStrategy;
import org.example.strategies.pSurvival.PSurvivalStrategy;
import org.example.utils.Position;

import lombok.Getter;

public class Agent implements Serializable {
    @Getter
    private double learningAbility;
    @Getter
    private final double learningAbilityAtBirth;
    @Getter
    private final Lexicon lexicon;
    @Getter
    private int x;
    @Getter
    private int y;
    @Getter
    private int age;

    private int nInitedCommunications = 0;
    private int nSuccessfulCommunications = 0;

    private final PSurvivalStrategy pSurvivalStrategy;
    private final LearningAbilityInheritanceStrategy learningAbilityInheritanceStrategy;
    private final LAbAgingStrategy learningAbilityAgingStrategy;

    private final VarConfig varConfig;
    private final StrategyConfig strategyConfig;

    public Agent(double learningAbility, Lexicon lexicon, VarConfig varConfig, StrategyConfig strategyConfig) {
        this.learningAbility = learningAbility;
        this.learningAbilityAtBirth = learningAbility;
        this.lexicon = lexicon;
        this.x = -1;
        this.y = -1;
        this.age = 1;
        this.varConfig = varConfig;
        this.pSurvivalStrategy = strategyConfig.getPSurvivalStrategy();
        this.learningAbilityAgingStrategy = strategyConfig.getLearingAbilityAgingStrategy();
        this.learningAbilityInheritanceStrategy = strategyConfig.getLearningAbilityInheritanceStrategy();
        this.strategyConfig = strategyConfig;
    }

    public String speak() {
        nInitedCommunications++;
        if (lexicon.isEmpty()) {
            lexicon.addWord(Lexicon.generateWord(varConfig.WORD_LENGTH()), 1.0);
        }
        return lexicon.getRandomWord();
    }

    public boolean knowsWord(String word) {
        return lexicon.has(word);
    }

    public void reinforceWord(String word) {
        lexicon.updateWeight(word, learningAbility);
    }

    public void diminishWord(String word) {
        lexicon.updateWeight(word, -learningAbility);
        if (lexicon.isWordObsolete(word)) {
            lexicon.removeWord(word);
        }
    }

    public void learnWord(String word) {
        lexicon.addWord(word, 1);
    }

    public boolean survives(World world) {
        return pSurvivalStrategy.survives(this, world);
    }

    public Agent reproduce(double mutationProbability) {
        Random random = new Random();

        double newLearningAbility = learningAbilityInheritanceStrategy.inheritLearningAbility(mutationProbability, this);

        double rWordMutation = random.nextDouble();
        String newWord;
        if (rWordMutation < mutationProbability || lexicon.isEmpty()) {
            newWord = Lexicon.generateWord(varConfig.WORD_LENGTH());
        } else {
            newWord = lexicon.getTopWord();
        }

        Lexicon newLexicon = new Lexicon(lexicon.getMaxSize());
        newLexicon.addWord(newWord, 1);
        return new Agent(newLearningAbility, newLexicon, this.varConfig, this.strategyConfig);
    }

    public void increaseAge() {
        learningAbility = learningAbilityAgingStrategy.ageLearningAbility(this);
        age++;
    }

    public void recordSuccessfulCommunication() {
        nSuccessfulCommunications++;
    }

    public double getKnowledge() {
        return lexicon.getWeightsSum();
    }

    public double getSuccessRate() {
        return nInitedCommunications == 0 ? 0 : (double) nSuccessfulCommunications / nInitedCommunications;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isAtPostion(int x, int y) {
        return this.x == x && this.y == y;
    }
}