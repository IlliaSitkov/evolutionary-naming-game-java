package org.example.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
        if (totalWeight == 0) {
            List<String> keys = new ArrayList<>(words.keySet());
            return keys.get(new Random().nextInt(keys.size()));
        }
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
        shouldNotBeEmpty();
        String topWord = null;
        double maxWeight = Double.NEGATIVE_INFINITY;

        for (Map.Entry<String, Double> entry : words.entrySet()) {
            if (entry.getValue() > maxWeight) {
                maxWeight = entry.getValue();
                topWord = entry.getKey();
            }
        }

        return topWord;
    }

    public double getWeightsSum() {
        double res = 0.0;
        for (double d : words.values()) {
            res += d;
        }
        return res;
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

        String leastWeightedWord = null;
        double minWeight = Double.POSITIVE_INFINITY;

        for (Map.Entry<String, Double> entry : words.entrySet()) {
            if (entry.getValue() < minWeight) {
                minWeight = entry.getValue();
                leastWeightedWord = entry.getKey();
            }
        }

        if (leastWeightedWord != null) {
            words.remove(leastWeightedWord);
        }
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