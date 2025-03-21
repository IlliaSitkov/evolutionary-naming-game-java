package org.example.utils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.example.export.IOUtils;
import org.example.plotting.SimulationPlots;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.pCommunication.PCommunicationStrategy;

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

    
    simulation.start();
    
    timer.stop("Simulation ended");

    folder = makePath(folder, "/", new Date().getTime());
    
    SimulationPlots simulationPlots = new SimulationPlots(folder);
    SimulationStats stats = simulation.getSimulationStats();
    PCommunicationStrategy pCommStrategy = simulation.getStrategyConfig().getPCommunicationStrategy();
    int nIters = simulation.getVarConfig().T();
    List<Double> pCommOverIterations = SimulationStats.getPCommunicationOverIterations(pCommStrategy, nIters, true);

    IOUtils.exportToCSV(folder + "/run_stats.csv", stats.getLanguagesNumber().size(), List.of(
        new Object[] { "ScsRate", stats.getSuccessRates() },
        new Object[] { "ScsCommN", stats.getSuccessfulCommunicationsNumber() },
        new Object[] { "CommN", stats.getCommunicationsNumber() },
        new Object[] { "PComm", pCommOverIterations },
        new Object[] { "AvgLAbBirth", stats.getAvgLearningAbilitiesAtBirth() },
        new Object[] { "AvgLAb", stats.getAvgLearningAbilities() },
        new Object[] { "AvgPSurv", stats.getAvgPSurvs() },
        new Object[] { "KilledN", stats.getKilledAgentsNumber() },
        new Object[] { "BornN", stats.getBornAgentsNumber() },
        new Object[] { "SurvN", stats.getSurvivorAgentsNumber() },
        new Object[] { "AliveN", stats.getNAgentsAlive() },
        new Object[] { "BornLAbBirth", stats.getAvgBornLAbsAtBirth() },
        new Object[] { "BornLAb", stats.getAvgBornLAbs() },
        new Object[] { "KilledLAbBirth", stats.getAvgKilledLAbsAtBirth() },
        new Object[] { "KilledLAb", stats.getAvgKilledLAbs() },
        new Object[] { "KilledKnwldg", stats.getAvgKilledKnowledge() },
        new Object[] { "KilledAge", stats.getAvgKilledAge() },
        new Object[] { "SurvLAbBirth", stats.getAvgSurvLAbsAtBirth() },
        new Object[] { "SurvLAb", stats.getAvgSurvLAbs() },
        new Object[] { "SurvKnwldg", stats.getAvgSurvKnowledge() },
        new Object[] { "SurvAge", stats.getAvgSurvAge() },
        new Object[] { "AvgKnwldg", stats.getAvgKnowledge() },
        new Object[] { "LangN", stats.getLanguagesNumber() },
        new Object[] { "AvgAge", stats.getAvgAges() },
        new Object[] { "NewWordsSpeak", stats.getNNewWordsSpeak() },
        new Object[] { "NewWordsEmptL", stats.getNNewWordsEmptyLexicon() },
        new Object[] { "NewWordsMut", stats.getNNewWordsMutation() },
        new Object[] { "WordsRmvd", stats.getNWordsRemoved() }
        ));

    simulationPlots.saveSimulationStats(
        stats,
        pCommStrategy,
        nIters);
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
