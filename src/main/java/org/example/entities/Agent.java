package org.example.entities;

import java.io.Serializable;
import java.util.Random;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.stats.IterationStats;
import org.example.strategies.learningAbilityAging.LAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.LAbInheritanceStrategy;
import org.example.strategies.pSurvival.PSurvivalStrategy;
import org.example.strategies.wordAcquisition.WordAcquisitionStrategy;

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

    /**
     * Number of communications where the agent was a speaker
     */
    private int nInitedCommunications = 0;
    /**
     * Number of communications where the agent was a speaker and the communication
     * was successful
     */
    private int nSuccessfulCommunications = 0;

    private final PSurvivalStrategy pSurvivalStrategy;
    private final LAbInheritanceStrategy learningAbilityInheritanceStrategy;
    private final LAbAgingStrategy learningAbilityAgingStrategy;
    private final WordAcquisitionStrategy wordAcquisitionStrategy;

    private final VarConfig varConfig;
    private final StrategyConfig strategyConfig;

    public Agent(double learningAbility, Lexicon lexicon, VarConfig varConfig, StrategyConfig strategyConfig) {
        this.learningAbility = learningAbility;
        this.learningAbilityAtBirth = learningAbility;
        this.lexicon = lexicon;
        this.x = -1;
        this.y = -1;
        this.age = varConfig.INIT_AGE();
        this.varConfig = varConfig;
        this.strategyConfig = strategyConfig;
        this.pSurvivalStrategy = strategyConfig.getPSurvivalStrategy();
        this.learningAbilityAgingStrategy = strategyConfig.getLearingAbilityAgingStrategy();
        this.learningAbilityInheritanceStrategy = strategyConfig.getLearningAbilityInheritanceStrategy();
        this.wordAcquisitionStrategy = strategyConfig.getWordAcquisitionStrategy();
    }

    public String speak(IterationStats iterationStats) {
        nInitedCommunications++;
        if (lexicon.isEmpty()) {
            iterationStats.trackNewWordSpeak();
            inventWord();
        }
        return lexicon.getRandomWord();
    }

    private void inventWord() {
        lexicon.addWord(Lexicon.generateWord(varConfig.WORD_LENGTH()), wordAcquisitionStrategy.getInventedWordWeight(this));
    }

    public boolean knowsWord(String word) {
        return lexicon.has(word);
    }

    public void reinforceWord(String word) {
        lexicon.updateWeight(word, learningAbility);
    }

    public void diminishWord(String word, IterationStats iterationStats) {
        lexicon.updateWeight(word, -learningAbility);
        if (lexicon.isWordObsolete(word)) {
            iterationStats.trackWordRemoved();
            lexicon.removeWord(word);
        }
    }

    public void learnWord(String word) {
        lexicon.addWord(word, wordAcquisitionStrategy.getLearntWordWeight(this));
    }

    public double[] survives(World world) {
        return pSurvivalStrategy.survives(this, world);
    }

    public Agent reproduce(double mutationProbability, IterationStats iterationStats) {
        return varConfig.REPR_LIPOWSKA() == 0 ? reproduceMoloney(mutationProbability, iterationStats)
                : reproduceLipowska(mutationProbability, iterationStats);
    }

    private Agent reproduceLipowska(double mutationProbability, IterationStats iterationStats) {
        Random random = new Random();

        double newLearningAbility = learningAbilityInheritanceStrategy.inheritLearningAbility(mutationProbability,
                this)[0];

        double rWordMutation = random.nextDouble();
        String newWord;
        if (rWordMutation < mutationProbability) {
            iterationStats.trackNewWordMutation();
            newWord = Lexicon.generateWord(varConfig.WORD_LENGTH());
        } else {
            if (lexicon.isEmpty()) {
                iterationStats.trackNewWordEmptyLexicon();
                inventWord();
            }
            newWord = lexicon.getTopWord();
        }

        Lexicon newLexicon = new Lexicon(lexicon.getMaxSize());
        newLexicon.addWord(newWord, wordAcquisitionStrategy.getInheritedWordWeight(newLearningAbility));
        return new Agent(newLearningAbility, newLexicon, this.varConfig, this.strategyConfig);
    }

    private Agent reproduceMoloney(double mutationProbability, IterationStats iterationStats) {
        double[] newLearningAbilityResult = learningAbilityInheritanceStrategy.inheritLearningAbility(mutationProbability,
                this);

        double newLearningAbility = newLearningAbilityResult[0];
        double rWordMutation = newLearningAbilityResult[1];

        String newWord;
        if (rWordMutation >= mutationProbability) {
            if (lexicon.isEmpty()) {
                iterationStats.trackNewWordEmptyLexicon();
                inventWord();
            }
            newWord = lexicon.getTopWord();
        } else {
            iterationStats.trackNewWordMutation();
            newWord = Lexicon.generateWord(varConfig.WORD_LENGTH());
        }

        Lexicon newLexicon = new Lexicon(lexicon.getMaxSize());
        newLexicon.addWord(newWord, wordAcquisitionStrategy.getInheritedWordWeight(newLearningAbility));
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