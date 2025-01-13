package org.example.utils;

public class Timer {
    private long startTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop(String message) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(message + " took " + duration / 1000.0 + " seconds");
    }
}