package org.example.scenarios;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.VarConfig.ConfigKey;
import org.example.export.IOUtils;
import org.example.plotting.SimulationPlots;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.evolution.ProbabilisticEvolutionStrategy;
import org.example.strategies.learningAbilityAging.ConstantLAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.RandomLAbInheritanceStrategy;
import org.example.strategies.neighborPositions.Neighbor8PositionsStrategy;
import org.example.strategies.pCommunication.ConstantPCommunicationStrategy;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.strategies.wordAcquisition.UnitWordAcquisitionStrategy;
import org.example.utils.RunUtils;

public class PCommAndAResearch {

  public static void investigateA(int L, int T, int skipT, double minA, double maxA, double minPComm, double maxPComm,
      double pCommStep, double pCommStepSmall, int nRuns) {
    String parentFolder = "investigate_A";
    String folder = parentFolder + "/alive_depend_a_L_" + L + "/" + new Date().getTime();
    Map<Double, Object> allStats = new HashMap<>();
    for (double pComm = minPComm; pComm <= maxPComm; pComm = round(pComm + (pComm < 0.8 ? pCommStep : pCommStepSmall),
        10000)) {
      List<Double> valuesA = new ArrayList<>();
      List<Double> valuesAvgAlive = new ArrayList<>();
      List<Double> valuesActualPUpds = new ArrayList<>();
      for (double A = minA; A <= maxA; A = round(A + (A < 0.01 ? 0.002 : A < 0.1 ? 0.01 : 0.1), 100000)) {
        valuesA.add(A);
        double totalAvgAlive = 0;
        double count = 0;

        System.out.println("pComm = " + pComm + ", A = " + A);
        for (int i = 0; i < nRuns; i++) {
          VarConfig varConfig = new VarConfig(Map.of(ConfigKey.T, T, ConfigKey.L, L, ConfigKey.A, A));
          StrategyConfig strategyConfig = new StrategyConfig(
              new ConstantPCommunicationStrategy(pComm),
              new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
              new RandomLAbInheritanceStrategy(),
              new ConstantLAbAgingStrategy(),
              new Neighbor8PositionsStrategy(),
              new UnitWordAcquisitionStrategy(),
              new ProbabilisticEvolutionStrategy());
          SimulationStats stats = new SimulationStats();
          Simulation simulation = new Simulation(stats, varConfig, strategyConfig);
          simulation.start();

          System.out.println(stats.getNAgentsAlive().size());
          if (stats.getNAgentsAlive().size() < skipT) {
            continue;
          }
          double[] results = RunUtils.getSeriesStats(stats.getNAgentsAlive().subList(skipT, stats.getAvgAges().size()));
          totalAvgAlive += results[0];
          count++;
        }
        double agentsAlive = count == 0 ? 0 : totalAvgAlive / count;
        valuesAvgAlive.add(agentsAlive);
        valuesActualPUpds.add(agentsAlive == 0 ? 0 : L * L / agentsAlive * (1 - pComm));
      }

      SimulationPlots plots = new SimulationPlots(folder);
      plots.plotSeriesAsDependentOnAnother(valuesA, valuesAvgAlive, "alive_over_A_pComm=" + pComm, "A",
          "# Alive Agents", "A", 0.0, (double) L * L, true);
      plots.plotSeriesAsDependentOnAnother(valuesA, valuesActualPUpds, "pUpd_over_A_pComm=" + pComm, "A",
          "# Actual p_upd", "A", 0.0, 6.0, true);
      List<List<Double>> data = List.of(valuesA, valuesAvgAlive, valuesActualPUpds);
      IOUtils.exportToJson(data, "out/" + folder + "/data_pComm=" + pComm + ".json");
      allStats.put(pComm, data);
    }
    IOUtils.exportToJson(allStats, "out/" + folder + "/data_all.json");
  }

  public static double round(double d, double prec) {
    return Math.round(d * prec) / prec;
  }

  public static void investigatePComm(int L, int T, int skipT, double minA, double maxA, double minPComm,
      double maxPComm, double pCommStep, double pCommStepSmall, int nRuns) {
    String parentFolder = "investigate_p_comm";
    String folder = parentFolder + "/alive_depend_p_comm_L_" + L + "/" + new Date().getTime();
    Map<Double, Object> allStats = new HashMap<>();
    for (double A = minA; A < maxA; A = round(A + (A < 0.01 ? 0.002 : A < 0.1 ? 0.01 : 0.1), 100000)) {
      List<Double> valuesPComm = new ArrayList<>();
      List<Double> valuesAvgAlive = new ArrayList<>();
      List<Double> valuesActualPUpds = new ArrayList<>();
      for (double pComm = minPComm; pComm <= maxPComm; pComm = round(pComm + (pComm < 0.8 ? pCommStep : pCommStepSmall),
          10000)) {
        valuesPComm.add(pComm);
        double totalAvgAlive = 0;
        int count = 0;

        System.out.println("A = " + A + ", pComm = " + pComm);
        for (int i = 0; i < nRuns; i++) {
          VarConfig varConfig = new VarConfig(Map.of(ConfigKey.T, T, ConfigKey.L, L, ConfigKey.A, A));
          StrategyConfig strategyConfig = new StrategyConfig(
              new ConstantPCommunicationStrategy(pComm),
              new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
              new RandomLAbInheritanceStrategy(),
              new ConstantLAbAgingStrategy(),
              new Neighbor8PositionsStrategy(),
              new UnitWordAcquisitionStrategy(),
              new ProbabilisticEvolutionStrategy());
          SimulationStats stats = new SimulationStats();
          Simulation simulation = new Simulation(stats, varConfig, strategyConfig);
          simulation.start();

          System.out.println(stats.getNAgentsAlive().size());
          if (stats.getNAgentsAlive().size() < skipT) {
            continue;
          }
          double[] results = RunUtils.getSeriesStats(stats.getNAgentsAlive().subList(skipT, stats.getAvgAges().size()));
          totalAvgAlive += results[0];
          count++;
        }
        double agentsAlive = count == 0 ? 0 : totalAvgAlive / count;
        valuesAvgAlive.add(agentsAlive);
        System.out.println("agentsAlive = " + agentsAlive);
        valuesActualPUpds.add(agentsAlive == 0 ? 0 : L * L / agentsAlive * (1 - pComm));
      }
      SimulationPlots plots = new SimulationPlots(folder);
      plots.plotSeriesAsDependentOnAnother(valuesPComm, valuesAvgAlive, "alive_over_pComm_A=" + A, "p_comm",
          "# Alive Agents", "PComm", 0.0, (double) L * L);
      plots.plotSeriesAsDependentOnAnother(valuesPComm, valuesActualPUpds, "pUpd_over_pComm_A=" + A, "p_comm",
          "# Actual p_upd", "PComm", 0.0, 1.05);
      List<List<Double>> data = List.of(valuesPComm, valuesAvgAlive, valuesActualPUpds);
      IOUtils.exportToJson(data, "out/" + folder + "/data_A=" + A + ".json");
      allStats.put(A, data);
    }
    IOUtils.exportToJson(allStats, "out/" + folder + "/data_all.json");
  }
}
