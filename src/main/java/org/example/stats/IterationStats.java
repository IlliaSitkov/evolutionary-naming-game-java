package org.example.stats;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class IterationStats {
    @Getter
    private int nCommunications = 0;
    @Getter
    private int nSuccessfulCommunications = 0;
    @Getter
    private int nKilledAgents = 0;
    @Getter
    private int nBornAgents = 0;
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
        if (pSurvList.isEmpty()) {
            return null;
        }
        return pSurvList.stream().mapToDouble(Double::doubleValue).sum() / pSurvList.size();
    }

    public void trackAgentKilled() {
        nKilledAgents++;
    }

    public void trackAgentBorn() {
        nBornAgents++;
    }

    public void trackPSurv(double pSurv) {
        pSurvList.add(pSurv);
    }
}