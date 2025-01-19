package org.example.entities;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import lombok.Getter;

public class Lexicon implements Serializable {
    @Getter
    private final int maxSize;
    private final Map<String, Double> words = new LinkedHashMap<>();

    public Lexicon(int maxSize) {
        this.maxSize = maxSize;
    }

    public void updateWeight(String word, double weight) {
        shouldContainWord(word);
        words.put(word, words.get(word) + weight);
    }

    public void addWord(String word, double weight) {
        shouldNotContainWord(word);
        if (words.size() >= maxSize) {
            removeLeastWeightedWord();
        }
        words.put(word, weight);
    }

    public void removeWord(String word) {
        shouldContainWord(word);
        words.remove(word);
    }

    public String getRandomWord() {
        shouldNotBeEmpty();
        double totalWeight = getWeightsSum();
        double randVal = new Random().nextDouble(totalWeight);
        double cumulativeWeight = 0;
        for (Map.Entry<String, Double> entry : words.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (cumulativeWeight >= randVal) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getTopWord() {
        return words.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    public double getWeightsSum() {
        return words.values().stream().reduce(0.0, Double::sum);
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }

    public boolean isWordObsolete(String word) {
        return words.get(word) < 0;
    }

    public boolean has(String word) {
        return words.containsKey(word);
    }

    private void shouldContainWord(String word) {
        if (!has(word)) {
            throw new IllegalArgumentException("Lexicon does not contain the word '" + word + "'");
        }
    }

    private void shouldNotContainWord(String word) {
        if (has(word)) {
            throw new IllegalArgumentException("Lexicon already contains the word '" + word + "'");
        }
    }

    private void shouldNotBeEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException("Lexicon is empty");
        }
    }

    private void removeLeastWeightedWord() {
        shouldNotBeEmpty();
        String leastWeightedWord = words.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey();
        words.remove(leastWeightedWord);
    }

    public static String generateWord(int size) {
        Random random = new Random();
        StringBuilder word = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            word.append((char) ('a' + random.nextInt(26)));
        }
        return word.toString();
    }
}