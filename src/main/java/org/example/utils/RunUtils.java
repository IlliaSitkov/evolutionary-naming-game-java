package org.example.utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.example.export.IOUtils;
import org.example.plotting.SimulationPlots;
import org.example.simulation.Simulation;

public class RunUtils {
  public static void runSimulationsInParallel(List<Runnable> simulationTasks) {
    ExecutorService executorService = Executors.newFixedThreadPool(8);

    for (Runnable task : simulationTasks) {
      executorService.submit(task);
    }

    executorService.shutdown();
  }

  public static void runSimulation(Simulation simulation, String folder) {
    Timer timer = new Timer();
    timer.start();

    SimulationPlots simulationPlots = new SimulationPlots(folder);

    simulation.start();

    timer.stop("Simulation ended");

    simulationPlots.saveSimulationStats(
        simulation.getSimulationStats(),
        simulation.getStrategyConfig().getPCommunicationStrategy(),
        simulation.getVarConfig().T());
    IOUtils.saveSimulationConfig(folder, simulation);
  }
}
