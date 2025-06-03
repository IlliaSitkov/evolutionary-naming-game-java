package org.example.stats;

import java.util.ArrayList;
import java.util.List;

import org.example.entities.Agent;

import lombok.Getter;

public class IterationStats {
    @Getter
    private int nCommunications;
    @Getter
    private int nSuccessfulCommunications;
    @Getter
    private int nNewWordSpeak;
    @Getter
    private int nNewWordEmptyLexicon;
    @Getter
    private int nNewWordMutation;
    @Getter
    private int nWordRemoved;
    @Getter
    private List<Double> killedLAbsAtBirth = new ArrayList<>();
    @Getter
    private List<Double> killedLAbs = new ArrayList<>();
    @Getter
    private List<Double> killedKnowledge = new ArrayList<>();
    @Getter
    private List<Integer> killedAges = new ArrayList<>();
    @Getter
    private List<Double> survivorLAbsAtBirth = new ArrayList<>();
    @Getter
    private List<Double> survivorLAbs = new ArrayList<>();
    @Getter
    private List<Double> survivorKnowledge = new ArrayList<>();
    @Getter
    private List<Integer> survivorAges = new ArrayList<>();
    @Getter
    private List<Double> bornLAbsAtBirth = new ArrayList<>();
    @Getter
    private List<Double> bornLAbs = new ArrayList<>();
    @Getter
    private final int iteration;
    @Getter
    private List<Double> pSurvList = new ArrayList<>();

    public IterationStats(int iteration) {
        this.iteration = iteration;
    }

    public void trackCommunicationResult(boolean success) {
        nCommunications++;
        if (success) {
            nSuccessfulCommunications++;
        }
    }

    public Double getSuccessRate() {
        if (nCommunications == 0) {
            return null;
        }
        return (double) nSuccessfulCommunications / nCommunications;
    }

    public Double getAvgPSurv() {
        return SimulationStats.getDoubleListAvg(pSurvList);
    }

    public Double getAvgKilledLAbAtBirth() {
        return SimulationStats.getDoubleListAvg(killedLAbsAtBirth);
    }

    public Double getAvgKilledLAb() {
        return SimulationStats.getDoubleListAvg(killedLAbs);
    }

    public Double getAvgKilledKnowledge() {
        return SimulationStats.getDoubleListAvg(killedKnowledge);
    }

    public Double getAvgKilledAge() {
        return SimulationStats.getDoubleListAvg(killedAges);
    }

    public Double getAvgBornLAbAtBirth() {
        return SimulationStats.getDoubleListAvg(bornLAbsAtBirth);
    }

    public Double getAvgBornLAb() {
        return SimulationStats.getDoubleListAvg(bornLAbs);
    }

    public Double getAvgSurvivorLAbAtBirth() {
        return SimulationStats.getDoubleListAvg(survivorLAbsAtBirth);
    }

    public Double getAvgSurvivorLAb() {
        return SimulationStats.getDoubleListAvg(survivorLAbs);
    }

    public Double getAvgSurvivorKnowledge() {
        return SimulationStats.getDoubleListAvg(survivorKnowledge);
    }

    public Double getAvgSurvivorAge() {
        return SimulationStats.getDoubleListAvg(survivorAges);
    }

    public void trackAgentKilled(Agent agent) {
        killedLAbsAtBirth.add(agent.getLearningAbilityAtBirth());
        killedLAbs.add(agent.getLearningAbility());
        killedKnowledge.add(agent.getKnowledge());
        killedAges.add(agent.getAge());
    }

    public void trackAgentBorn(Agent agent) {
        bornLAbsAtBirth.add(agent.getLearningAbilityAtBirth());
        bornLAbs.add(agent.getLearningAbility());
    }

    public void trackSurvivor(Agent agent) {
        survivorLAbsAtBirth.add(agent.getLearningAbilityAtBirth());
        survivorLAbs.add(agent.getLearningAbility());
        survivorKnowledge.add(agent.getKnowledge());
        survivorAges.add(agent.getAge());
    }

    public void trackNewWordSpeak() {
        nNewWordSpeak++;
    }

    public void trackNewWordEmptyLexicon() {
        nNewWordEmptyLexicon++;
    }

    public void trackNewWordMutation() {
        nNewWordMutation++;
    }

    public void trackWordRemoved() {
        nWordRemoved++;
    }

    public void trackPSurv(double pSurv) {
        pSurvList.add(pSurv);
    }

    public Integer getNKilledAgents() {
        return killedLAbs.size();
    }

    public Integer getNBornAgents() {
        return bornLAbs.size();
    }

    public Integer getNSurvivorAgents() {
        return survivorLAbs.size();
    }
}