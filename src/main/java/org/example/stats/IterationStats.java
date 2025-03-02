package org.example.stats;

import lombok.Getter;

public class IterationStats {
    @Getter
    private int nCommunications = 0;
    private int nSuccessfulCommunications = 0;
    @Getter
    private int nKilledAgents = 0;
    @Getter
    private int nBornAgents = 0;
    @Getter
    private final int iteration;

    public IterationStats(int iteration) {
        this.iteration = iteration;
    }

    public void trackCommunicationResult(boolean success) {
        nCommunications++;
        if (success) {
            nSuccessfulCommunications++;
        }
    }

    public double getSuccessRate() {
        if (nCommunications == 0) {
            return 0;
        }
        return (double) nSuccessfulCommunications / nCommunications;
    }

    public void trackAgentKilled() {
        nKilledAgents++;
    }

    public void trackAgentBorn() {
        nBornAgents++;
    }
}