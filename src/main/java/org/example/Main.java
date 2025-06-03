package org.example;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.example.scenarios.AbruptPCommTransition;
import org.example.scenarios.ConstantPCommTransition;
import org.example.scenarios.ContinuousPCommTransition;
import org.example.scenarios.PCommAndAResearch;
import org.example.scenarios.PCommDecrease;
import org.example.scenarios.PCommIncrease;
import org.example.strategies.neighborPositions.Neighbor4PositionsStrategy;

public class Main {
    public static void main(String[] args) {
        runSimulationsInParallel();
    }

    public static void runSimulationsInParallel() {
        ExecutorService executorService = Executors.newFixedThreadPool(16);

        List<Runnable> simulationTasks = List.of(
                () -> ContinuousPCommTransition.original("example", 40, 1, 0.5, 0.05, 100000, 100000),
                () -> AbruptPCommTransition.original("example", 20, 1, 0.01),
                () -> PCommIncrease.original(40, 0.05, 1, false, 8000, 3000, 0.19, 0.11,
                        new Neighbor4PositionsStrategy(false)),
                () -> PCommDecrease.original("example", 40, 1, 0.05, false, 3000, 8000, 0.01, 0.15,
                        new Neighbor4PositionsStrategy(false)),
                () -> ConstantPCommTransition.run("example", 30, 1, 0.0, 0.05, 30000, 0.001),
                () -> PCommAndAResearch.investigateA(40, 2000, 1000, 0, 1, 0.5, 0.99, 0.05, 0.02, 2),
                () -> PCommAndAResearch.investigatePComm(40, 2000, 1000, 0, 1, 0.5, 0.99, 0.05, 0.02, 2));

        for (Runnable task : simulationTasks) {
            executorService.submit(task);
        }

        executorService.shutdown();
    }
}