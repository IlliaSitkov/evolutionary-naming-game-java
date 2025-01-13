package org.example.stats;

public class IterationStats {
    private int nCommunications = 0;
    private int nSuccessfulCommunications = 0;

    public void trackSuccessRate(boolean success) {
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
}