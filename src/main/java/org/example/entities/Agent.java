package org.example.entities;

import java.util.Random;

import org.example.Config;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.strategies.pSurvival.PSurvivalStrategy;
import org.example.strategies.pSurvival.SuccessRatePSurvivalStrategy;

import lombok.Getter;

public class Agent {
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

    private final PSurvivalStrategy pSurvivalStrategy = new AvgKnowledgePSurvivalStrategy(Config.A, Config.B);
    // private final PSurvivalStrategy pSurvivalStrategy = new SuccessRatePSurvivalStrategy(Config.A, Config.C);

    public Agent(double learningAbility, Lexicon lexicon, int x, int y) {
        this.learningAbility = learningAbility;
        this.lexicon = lexicon;
        this.x = x;
        this.y = y;
        this.age = 1;
    }

    public Agent(double learningAbility, Lexicon lexicon) {
        this(learningAbility, lexicon, -1, -1);
    }

    public String speak() {
        nInitedCommunications++;
        if (lexicon.isEmpty()) {
            lexicon.addWord(Lexicon.generateWord(Config.WORD_LENGTH), 1.0);
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
            newWord = Lexicon.generateWord(Config.WORD_LENGTH);
        } else {
            newWord = lexicon.getTopWord();
        }

        Lexicon newLexicon = new Lexicon(lexicon.getMaxSize());
        newLexicon.addWord(newWord, 1);
        return new Agent(newLearningAbility, newLexicon);
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