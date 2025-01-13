package org.example.entities;

import java.util.Random;

import org.example.Config;

import lombok.Getter;

public class Agent {
    @Getter
    private final double learningAbility;
    private final Lexicon lexicon;
    @Getter
    private int x;
    @Getter
    private int y;
    private int age;

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

    public double getSurvivalProbability(double avgKnowledge, double A, double B) {
        return (Math.exp(-A * age) * (1 - Math.exp(-B * lexicon.getWeightsSum() / avgKnowledge)));
    }

    public boolean survives(double avgKnowledge, double A, double B) {
        double pSurvival = getSurvivalProbability(avgKnowledge, A, B);
        double r = new Random().nextDouble();
        return r < pSurvival;
    }

    public Agent reproduce(double mutationProbability) {
        Random random = new Random();
        double rLearningAbility = random.nextDouble();
        double rWordMutation = random.nextDouble();

        double newLearningAbility = learningAbility;
        String newWord = lexicon.getTopWord();

        if (rLearningAbility < mutationProbability) {
            newLearningAbility = random.nextDouble();
        }

        if (rWordMutation < mutationProbability) {
            newWord = Lexicon.generateWord(Config.WORD_LENGTH);
        }

        Lexicon newLexicon = new Lexicon(lexicon.getMaxSize());
        newLexicon.addWord(newWord, 1);
        return new Agent(newLearningAbility, newLexicon);
    }

    public void increaseAge() {
        age++;
    }

    public double getKnowledge() {
        return lexicon.getWeightsSum();
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}