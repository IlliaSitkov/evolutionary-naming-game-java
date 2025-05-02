package org.example.stats;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.entities.Agent;
import org.example.entities.World;
import org.example.strategies.pCommunication.PCommunicationStrategy;

import lombok.Getter;
import smile.validation.metric.AdjustedRandIndex;
import smile.validation.metric.RandIndex;

public class SimulationStats {
    @Getter
    private final List<Double> successRates = new ArrayList<>();
    @Getter
    private final List<Double> avgLearningAbilities = new ArrayList<>();
    @Getter
    private final List<Double> avgLearningAbilitiesAtBirth = new ArrayList<>();
    @Getter
    private final List<Integer> languagesNumber = new ArrayList<>();
    @Getter
    private final List<Integer> communicationsNumber = new ArrayList<>();
    @Getter
    private final List<Integer> successfulCommunicationsNumber = new ArrayList<>();
    @Getter
    private final List<Double> avgAges = new ArrayList<>();
    @Getter
    private final List<Integer> killedAgentsNumber = new ArrayList<>();
    @Getter
    private final List<Integer> bornAgentsNumber = new ArrayList<>();
    @Getter
    private final List<Integer> survivorAgentsNumber = new ArrayList<>();
    @Getter
    private final List<Double> avgKnowledge = new ArrayList<>();
    @Getter
    private final List<Double> avgPSurvs = new ArrayList<>();
    @Getter
    private final List<Double> avgKilledLAbsAtBirth = new ArrayList<>();
    @Getter
    private final List<Double> avgKilledLAbs = new ArrayList<>();
    @Getter
    private final List<Double> avgKilledKnowledge = new ArrayList<>();
    @Getter
    private final List<Double> avgKilledAge = new ArrayList<>();
    @Getter
    private final List<Double> avgBornLAbsAtBirth = new ArrayList<>();
    @Getter
    private final List<Double> avgBornLAbs = new ArrayList<>();
    @Getter
    private final List<Double> avgSurvLAbsAtBirth = new ArrayList<>();
    @Getter
    private final List<Double> avgSurvLAbs = new ArrayList<>();
    @Getter
    private final List<Double> avgSurvKnowledge = new ArrayList<>();
    @Getter
    private final List<Double> avgSurvAge = new ArrayList<>();
    @Getter
    private final List<Integer> nNewWordsSpeak = new ArrayList<>();
    @Getter
    private final List<Integer> nNewWordsEmptyLexicon = new ArrayList<>();
    @Getter
    private final List<Integer> nNewWordsMutation = new ArrayList<>();
    @Getter
    private final List<Integer> nWordsRemoved = new ArrayList<>();
    @Getter
    private final List<Double> learningAbilityLanguageARI = new ArrayList<>();
    @Getter
    private final List<Double> learningAbilityLanguageRI = new ArrayList<>();
    @Getter
    private final List<Integer> nLangClusters = new ArrayList<>();
    @Getter
    private final List<Integer> nLAbClusters = new ArrayList<>();

    @Getter
    private final Map<String, double[][]> learningAbilityMaps = new HashMap<>();
    @Getter
    private final Map<String, String[][]> languageMaps = new HashMap<>();
    @Getter
    private final List<Integer> nAgentsAlive = new ArrayList<>();

    private final List<Integer> iterationsToSaveMaps;
    private final List<Double> pCommunicationsToSaveMaps;
    private final int nSkipIterations;

    public SimulationStats(List<Integer> iterationsToSaveMaps, List<Double> pCommunicationsToSaveMaps) {
        this.iterationsToSaveMaps = new ArrayList<>(iterationsToSaveMaps);
        this.pCommunicationsToSaveMaps = new ArrayList<>(pCommunicationsToSaveMaps);
        this.nSkipIterations = 0;
    }

    public SimulationStats(int nSkipIterations) {
        this.iterationsToSaveMaps = new ArrayList<>();
        this.pCommunicationsToSaveMaps = new ArrayList<>();
        this.nSkipIterations = nSkipIterations;
    }

    public SimulationStats() {
        this(0);
    }

    public void recordBeforeEvolution(World world, double pCommunication) {
        if (nSkipIterations > 0) {
            return;
        }
        recordCommonWorldStats(world);
        recordWorldLearningAbilities(world, -1, pCommunication);
        recordWorldLanguages(world, -1, pCommunication);
    }

    public void recordIteration(IterationStats iterationStats) {
        if (iterationStats.getIteration() < nSkipIterations) {
            return;
        }
        successRates.add(iterationStats.getSuccessRate());
        communicationsNumber.add(iterationStats.getNCommunications());
        successfulCommunicationsNumber.add(iterationStats.getNSuccessfulCommunications());
        killedAgentsNumber.add(iterationStats.getNKilledAgents());
        bornAgentsNumber.add(iterationStats.getNBornAgents());
        survivorAgentsNumber.add(iterationStats.getNSurvivorAgents());
        avgPSurvs.add(iterationStats.getAvgPSurv());
        avgKilledLAbsAtBirth.add(iterationStats.getAvgKilledLAbAtBirth());
        avgKilledLAbs.add(iterationStats.getAvgKilledLAb());
        avgKilledKnowledge.add(iterationStats.getAvgKilledKnowledge());
        avgKilledAge.add(iterationStats.getAvgKilledAge());
        avgBornLAbsAtBirth.add(iterationStats.getAvgBornLAbAtBirth());
        avgBornLAbs.add(iterationStats.getAvgBornLAb());
        avgSurvLAbsAtBirth.add(iterationStats.getAvgSurvivorLAbAtBirth());
        avgSurvLAbs.add(iterationStats.getAvgSurvivorLAb());
        avgSurvKnowledge.add(iterationStats.getAvgSurvivorKnowledge());
        avgSurvAge.add(iterationStats.getAvgSurvivorAge());
        nNewWordsSpeak.add(iterationStats.getNNewWordSpeak());
        nNewWordsEmptyLexicon.add(iterationStats.getNNewWordEmptyLexicon());
        nNewWordsMutation.add(iterationStats.getNNewWordMutation());
        nWordsRemoved.add(iterationStats.getNWordRemoved());
    }

    public void recordAfterIteration(World world, int iteration, double pCommunication) {
        if (iteration < nSkipIterations) {
            return;
        }
        recordCommonWorldStats(world);
        if (shouldSaveMaps(iteration, pCommunication)) {
            recordWorldLearningAbilities(world, iteration, pCommunication);
            recordWorldLanguages(world, iteration, pCommunication);
            pCommunicationsToSaveMaps.removeIf((pComm) -> pCommunication >= pComm);
        }
    }

    private boolean shouldSaveMaps(int iteration, double pCommunication) {
        boolean isScheduledSave = iterationsToSaveMaps.contains(iteration) ||
            pCommunicationsToSaveMaps.stream().anyMatch((pComm) -> pCommunication >= pComm);
    
        boolean isExtremeDrop = false;
        if (!learningAbilityLanguageRI.isEmpty()) {
            double currentRiScore = learningAbilityLanguageRI.get(learningAbilityLanguageRI.size() - 1);
            double previousRiScore = learningAbilityLanguageRI.size() > 1
                ? learningAbilityLanguageRI.get(learningAbilityLanguageRI.size() - 2)
                : currentRiScore;
    
            double dropPercentage = ((previousRiScore - currentRiScore) / previousRiScore) * 100;
            isExtremeDrop = dropPercentage > 20;
        }
    
        return isScheduledSave || isExtremeDrop; // || iteration % 50 == 0;
    }

    // private boolean shouldSaveMaps(int iteration, double pCommunication) {
    //     return iterationsToSaveMaps.contains(iteration) ||
    //     pCommunicationsToSaveMaps
    //     .stream()
    //     .anyMatch((pComm) -> pCommunication >= pComm);
    // }

    private void recordCommonWorldStats(World world) {
        World.Stats stats = world.getStats();
        avgLearningAbilities.add(stats.getAvgLearningAbility());
        avgLearningAbilitiesAtBirth.add(stats.getAvgLearningAbilityAtBirth());
        languagesNumber.add(stats.getLanguagesNumber());
        avgAges.add(stats.getAvgAge());
        avgKnowledge.add(stats.getAvgKnowledge());
        nAgentsAlive.add(world.getAgents().size());
        calculateLearningAbilityLanguageCorrelation(world);
    }

    private void calculateLearningAbilityLanguageCorrelation(World world) {
        List<Agent> agents = world.getAgents();
        agents.sort(Comparator.comparingInt(Agent::getX).thenComparingInt(Agent::getY));

        Map<Double, Integer> learningAbilityClusterMap = new HashMap<>();
        Map<String, Integer> languageClusterMap = new HashMap<>();

        List<Integer> learningAbilityLabelsList = new ArrayList<>();
        List<Integer> spokenLanguageLabelsList = new ArrayList<>();

        int nextLearningAbilityId = 0;
        int nextLanguageId = 0;

        String emptyLexiconLanguage = "empty_lexicon";

        for (Agent agent : agents) {
            double ability = agent.getLearningAbilityAtBirth();
            if (!learningAbilityClusterMap.containsKey(ability)) {
                learningAbilityClusterMap.put(ability, nextLearningAbilityId++);
            }
            int abilityId = learningAbilityClusterMap.get(ability);
            learningAbilityLabelsList.add(abilityId);

            String language = agent.getLexicon().isEmpty() ? emptyLexiconLanguage : agent.getLexicon().getTopWord();
            if (!languageClusterMap.containsKey(language)) {
                languageClusterMap.put(language, nextLanguageId++);
            }
            int languageId = languageClusterMap.get(language);
            spokenLanguageLabelsList.add(languageId);
        }

        int[] learningAbilityLabels = learningAbilityLabelsList.stream().mapToInt(i -> i).toArray();
        int[] spokenLanguageLabels = spokenLanguageLabelsList.stream().mapToInt(i -> i).toArray();

        AdjustedRandIndex ari = new AdjustedRandIndex();
        RandIndex ri = new RandIndex();

        double ariScore = ari.score(learningAbilityLabels, spokenLanguageLabels);
        double riScore = ri.score(learningAbilityLabels, spokenLanguageLabels);

        learningAbilityLanguageARI.add(ariScore);
        learningAbilityLanguageRI.add(riScore);

        nLAbClusters.add(nextLearningAbilityId);
        nLangClusters.add(nextLanguageId);
    }

    public static List<Double> getPCommunicationOverIterations(PCommunicationStrategy strategy, int nIters) {
        return getPCommunicationOverIterations(strategy, nIters, false);
    }

    public static List<Double> getPCommunicationOverIterations(PCommunicationStrategy strategy, int nIters, boolean padInitialValue) {
        List<Double> pCommunications = new ArrayList<>();
        if (padInitialValue) {
            // used for aligning with "zero" (initial setup) iteration stats
            pCommunications.add(strategy.getPCommunication(0));
        }
        for (int i = 0; i < nIters; i++) {
            pCommunications.add(strategy.getPCommunication(i));
        }
        return pCommunications;
    }

    public static Double getDoubleListAvg(List<? extends Number> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.stream().mapToDouble(Number::doubleValue).sum() / list.size();
    }

    private void recordWorldLearningAbilities(World world, int iteration, double pCommunication) {
        double[][] learningAbilities = new double[world.getRows()][world.getCols()];

        for (int y = 0; y < world.getRows(); y++) {
            for (int x = 0; x < world.getCols(); x++) {
                Agent agent = world.getAgentAt(x, y);
                if (agent != null) {
                    learningAbilities[y][x] = agent.getLearningAbility();
                } else {
                    learningAbilities[y][x] = -1;
                }
            }
        }

        String key = String.format("p_comm_%.4f_it_%d", pCommunication, iteration);
        learningAbilityMaps.put(key, learningAbilities);
    }

    private void recordWorldLanguages(World world, int iteration, double pCommunication) {
        String[][] languages = new String[world.getRows()][world.getCols()];

        for (int y = 0; y < world.getRows(); y++) {
            for (int x = 0; x < world.getCols(); x++) {
                Agent agent = world.getAgentAt(x, y);
                if (agent != null) {
                    languages[y][x] = agent.getLexicon().isEmpty() ? "-" : agent.getLexicon().getTopWord();
                } else {
                    languages[y][x] = "";
                }
            }
        }

        String key = String.format("p_comm_%.4f_it_%d", pCommunication, iteration);
        languageMaps.put(key, languages);
    }

    public double getAvgLearningAbility() {
        return avgLearningAbilities.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    public double getAvgSuccessRate() {
        return successRates.stream().filter((d) -> d != null).mapToDouble(Double::doubleValue).average().orElse(0);
    }

    
}