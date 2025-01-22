package org.example.entities;

import java.io.Serializable;
import java.util.Random;

import org.example.VarConfig;
import org.example.strategies.pSurvival.PSurvivalStrategy;

import lombok.Getter;

public class Agent implements Serializable {
    @Getter
    private final double learningAbility;
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
    private final VarConfig varConfig;

    public Agent(double learningAbility, Lexicon lexicon, VarConfig varConfig, PSurvivalStrategy pSurvivalStrategy) {
        this.learningAbility = learningAbility;
        this.lexicon = lexicon;
        this.x = -1;
        this.y = -1;
        this.age = 1;
        this.varConfig = varConfig;
        this.pSurvivalStrategy = pSurvivalStrategy;
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

        double rLearningAbility = random.nextDouble();
        double newLearningAbility;
        if (rLearningAbility < mutationProbability) {
            newLearningAbility = random.nextDouble();
        } else {
            newLearningAbility = learningAbility;
        }

        double rWordMutation = random.nextDouble();
        String newWord;
        if (rWordMutation < mutationProbability || lexicon.isEmpty()) {
            newWord = Lexicon.generateWord(varConfig.WORD_LENGTH());
        } else {
            newWord = lexicon.getTopWord();
        }

        Lexicon newLexicon = new Lexicon(lexicon.getMaxSize());
        newLexicon.addWord(newWord, 1);
        return new Agent(newLearningAbility, newLexicon, this.varConfig, this.pSurvivalStrategy);
    }

    public void increaseAge() {
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
}