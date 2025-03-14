package org.example.utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.example.export.IOUtils;
import org.example.plotting.SimulationPlots;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;

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

    SimulationStats stats = simulation.getSimulationStats();

    IOUtils.exportToCSV(folder + "/run_stats.csv", stats.getLanguagesNumber().size(), List.of(
        new Object[] { "ScsRate", stats.getSuccessRates() },
        new Object[] { "ScsCommN", stats.getSuccessfulCommunicationsNumber() },
        new Object[] { "CommN", stats.getCommunicationsNumber() },
        new Object[] { "AvgPSurv", stats.getAvgPSurvs() },
        new Object[] { "AvgLAb", stats.getAvgLearningAbilities() },
        new Object[] { "AvgKnldg", stats.getAvgKnowledge() },
        new Object[] { "LangN", stats.getLanguagesNumber() },
        new Object[] { "AvgAge", stats.getAvgAges() },
        new Object[] { "KilledN", stats.getKilledAgentsNumber() },
        new Object[] { "BornN", stats.getBornAgentsNumber() },
        new Object[] { "AliveN", stats.getNAgentsAlive() }));

    simulationPlots.saveSimulationStats(
        stats,
        simulation.getStrategyConfig().getPCommunicationStrategy(),
        simulation.getVarConfig().T());
    IOUtils.saveSimulationConfig(folder, simulation);
  }

  public static String makePath(Object... args) {
    StringBuilder path = new StringBuilder();

    for (int i = 0; i < args.length; i++) {
      Object arg = args[i];
      if (arg.toString().equals("/")) {
        path.append(arg);
      } else {
        boolean nextExists = i + 1 < args.length;
        boolean hasNextSlash = nextExists && args[i + 1].toString().startsWith("/");
        path.append(arg.toString());
        if (!hasNextSlash && nextExists) {
          path.append("_");
        }
      }
    }

    return path.toString();
  }
}
