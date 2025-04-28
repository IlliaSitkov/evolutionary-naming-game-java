package org.example.scenarios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.VarConfig.ConfigKey;
import org.example.export.IOUtils;
import org.example.plotting.SimulationPlots;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.agentInitializer.ControlledAgentInitializer;
import org.example.strategies.evolution.ProbabilisticEvolutionStrategy;
import org.example.strategies.learningAbilityAging.ConstantLAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.RandomLAbInheritanceStrategy;
import org.example.strategies.neighborPositions.Neighbor8PositionsStrategy;
import org.example.strategies.pCommunication.ConstantPCommunicationStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.strategies.wordAcquisition.UnitWordAcquisitionStrategy;
import org.example.utils.RunUtils;
import org.example.utils.Timer;

public class PCommDecrease {

  public static final String folder = "p_comm_decrease";

  public static void controlledWorld(int L) {
    String folder = PCommDecrease.folder + "/controlled_world/L=" + L + "/" + new Date().getTime();
    int nSkipIterations = 1000;
    
    VarConfig varConfig = new VarConfig(Map.of(
        ConfigKey.T, 3000, ConfigKey.L, L));

    StrategyConfig strategyConfig = new StrategyConfig(
        null,
        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
        new RandomLAbInheritanceStrategy(),
        new ConstantLAbAgingStrategy(),
        new Neighbor8PositionsStrategy(),
        new UnitWordAcquisitionStrategy(),
        new ProbabilisticEvolutionStrategy(),
        new ControlledAgentInitializer(0.95)
        );
    
    SimulationPlots simulationPlots = new SimulationPlots(folder);

    Simulation simulation = new Simulation(null, varConfig, strategyConfig);

    IOUtils.saveSimulationConfig(folder, simulation);

    List<Double> pCommunicationValues = List.of(0.27, 0.26, 0.25, 0.24, 0.23, 0.22, 0.21, 0.2, 0.19, 0.18, 0.17, 0.16,
        0.15, 0.14, 0.13, 0.12, 0.11);
    List<Double> avgSuccessRates = new ArrayList<>();
    List<Double> avgLearningAbilities = new ArrayList<>();

    Timer timer = new Timer();
    timer.start();

    for (double pComm : pCommunicationValues) {
      System.out.println("Starting simulation for pComm = " + pComm);

      PCommunicationStrategy strategy = new ConstantPCommunicationStrategy(pComm);
      SimulationStats simulationStats = new SimulationStats(nSkipIterations);

      simulation.setPCommunicationStrategy(strategy);
      simulation.setSimulationStats(simulationStats);
      simulation.start();

      Double avgLearningAbility = simulationStats.getAvgLearningAbility();
      avgLearningAbilities.add(avgLearningAbility);

      Double avgSuccessRate = simulationStats.getAvgSuccessRate();
      avgSuccessRates.add(avgSuccessRate);

      simulationPlots.plotTwoSeriesOverIterations(simulationStats.getAvgLearningAbilities(),
          simulationStats.getSuccessRates(), "l_ab_&_s_rate, pComm = " + pComm, "Iteration",
          "Value", 0, 1, "Learning Ability", "Success Rate", 0.0, 1.2);

      IOUtils.exportToJson(avgLearningAbility, "out/" + folder + "/l_ab_pComm_" + pComm + ".json");
      IOUtils.exportToJson(avgSuccessRate, "out/" + folder + "/s_rate_pComm_" + pComm + ".json");
    }

    simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgLearningAbilities, "l_ab_over_p_comm_avg",
        "P_Communication", "Learning Ability", "Learning Ability", 0.0, 1.2, true);
    simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgSuccessRates, "s_rate_over_p_comm_avg",
        "P_Communication", "Success Rate", "Success Rate", 0.0, 1.2, true);

    IOUtils.exportToJson(avgLearningAbilities, "out/" + folder + "/l_ab_pComm_all.json");
    IOUtils.exportToJson(avgSuccessRates, "out/" + folder + "/s_rate_pComm_all.json");

    timer.stop("All simulations done");
  }

  public static void original(int L, int N, int nSkipIterations, int nIterationsPerStep, double minPComm, double maxPComm) {

    VarConfig varConfig = new VarConfig(Map.of(
        ConfigKey.T, 30000,
        ConfigKey.L, L,
        ConfigKey.N, N
        ));

    StrategyConfig strategyConfig = new StrategyConfig(
        new ConstantPCommunicationStrategy(0.5),
        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
        new RandomLAbInheritanceStrategy(),
        new ConstantLAbAgingStrategy(),
        new Neighbor8PositionsStrategy(),
        new UnitWordAcquisitionStrategy(),
        new ProbabilisticEvolutionStrategy());

    String folder = RunUtils.makePath("p_comm_decrease", "/", "natural_world", "/", "L", L, "/", new Date().getTime());
    String preSimulationStatsFolder = folder + "/pre-simulation";
    SimulationPlots preSimulationPlots = new SimulationPlots(preSimulationStatsFolder);
    SimulationPlots simulationPlots = new SimulationPlots(folder);

    Simulation simulation = new Simulation(new SimulationStats(List.of(4000, 4500, 4999, 10000, 13000, 20000, 29000, 29999), List.of()), varConfig,
        strategyConfig);

    IOUtils.saveSimulationConfig(preSimulationStatsFolder, simulation);
    simulation.start();

    preSimulationPlots.saveSimulationStats(simulation.getSimulationStats(), strategyConfig.getPCommunicationStrategy(),
        varConfig.T());
    varConfig.setT(nIterationsPerStep);

    // List<Double> pCommunicationValues = List.of(0.27, 0.26, 0.25, 0.24, 0.23, 0.22, 0.21, 0.2, 0.19, 0.18, 0.17, 0.16,
    //     0.15, 0.14, 0.13, 0.12, 0.11);

    List<Double> pCommunicationValues = new ArrayList<>();
    for (double i = minPComm; i <= maxPComm; i += 0.01) {
        pCommunicationValues.add(Math.round(i * 100.0) / 100.0);
    }
    pCommunicationValues = pCommunicationValues.reversed();

    List<Double> avgSuccessRates = new ArrayList<>();
    List<Double> avgLearningAbilities = new ArrayList<>();

    Timer timer = new Timer();
    timer.start();

    for (double pComm : pCommunicationValues) {
      System.out.println("Starting simulation for pComm = " + pComm);

      Timer simulationTimer = new Timer();
      simulationTimer.start();

      PCommunicationStrategy strategy = new ConstantPCommunicationStrategy(pComm);
      SimulationStats simulationStats = new SimulationStats(nSkipIterations);

      simulation.setPCommunicationStrategy(strategy);
      simulation.setSimulationStats(simulationStats);
      simulation.start();

      simulationTimer.stop("Simulation for pComm = " + pComm + " done");

      Double avgLearningAbility = simulationStats.getAvgLearningAbility();
      avgLearningAbilities.add(avgLearningAbility);

      Double avgSuccessRate = simulationStats.getAvgSuccessRate();
      avgSuccessRates.add(avgSuccessRate);

      simulationPlots.plotTwoSeriesOverIterations(simulationStats.getAvgLearningAbilities(),
          simulationStats.getSuccessRates(), "l_ab_&_s_rate, pComm = " + pComm, "Iteration",
          "Value", 0, 1, "Learning Ability", "Success Rate", 0.0, 1.2);

      IOUtils.exportToJson(avgLearningAbility, "out/" + folder + "/l_ab_pComm_" + pComm + ".json");
      IOUtils.exportToJson(avgSuccessRate, "out/" + folder + "/s_rate_pComm_" + pComm + ".json");
    }

    simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgLearningAbilities, "l_ab_over_p_comm_avg",
        "P_Communication", "Learning Ability", "Learning Ability", null, null, true);
    simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgSuccessRates, "s_rate_over_p_comm_avg",
        "P_Communication", "Success Rate", "Success Rate", null, null, true);

    IOUtils.exportToJson(avgLearningAbilities, "out/" + folder + "/l_ab_pComm_all.json");
    IOUtils.exportToJson(avgSuccessRates, "out/" + folder + "/s_rate_pComm_all.json");

    timer.stop("All simulations done");
  }


  public static void runPCommDecreaseSimulations(int L) {

    VarConfig varConfig = new VarConfig(Map.of(
        ConfigKey.T, 30000, ConfigKey.L, L));

    StrategyConfig strategyConfig = new StrategyConfig(
        new ConstantPCommunicationStrategy(0.5),
        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
        new RandomLAbInheritanceStrategy(),
        new ConstantLAbAgingStrategy(),
        new Neighbor8PositionsStrategy(),
        new UnitWordAcquisitionStrategy(),
        new ProbabilisticEvolutionStrategy());

    String folder = RunUtils.makePath("p_comm_decrease", "/", "natural_world", "/", "L", L, "/", new Date().getTime());
    String preSimulationStatsFolder = folder + "/pre-simulation";
    SimulationPlots preSimulationPlots = new SimulationPlots(preSimulationStatsFolder);
    SimulationPlots simulationPlots = new SimulationPlots(folder);

    Simulation simulation = new Simulation(new SimulationStats(List.of(4000, 4500, 4999, 10000, 13000, 14999), List.of()), varConfig,
        strategyConfig);

    IOUtils.saveSimulationConfig(preSimulationStatsFolder, simulation);
    simulation.start();

    preSimulationPlots.saveSimulationStats(simulation.getSimulationStats(), strategyConfig.getPCommunicationStrategy(),
        varConfig.T());
    varConfig.setT(3000);

    int nSkipIterations = 1000;

    List<Double> pCommunicationValues = List.of(0.27, 0.26, 0.25, 0.24, 0.23, 0.22, 0.21, 0.2, 0.19, 0.18, 0.17, 0.16,
        0.15, 0.14, 0.13, 0.12, 0.11);
    List<Double> avgSuccessRates = new ArrayList<>();
    List<Double> avgLearningAbilities = new ArrayList<>();

    Timer timer = new Timer();
    timer.start();

    for (double pComm : pCommunicationValues) {
      System.out.println("Starting simulation for pComm = " + pComm);

      Timer simulationTimer = new Timer();
      simulationTimer.start();

      PCommunicationStrategy strategy = new ConstantPCommunicationStrategy(pComm);
      SimulationStats simulationStats = new SimulationStats(nSkipIterations);

      simulation.setPCommunicationStrategy(strategy);
      simulation.setSimulationStats(simulationStats);
      simulation.start();

      simulationTimer.stop("Simulation for pComm = " + pComm + " done");

      Double avgLearningAbility = simulationStats.getAvgLearningAbility();
      avgLearningAbilities.add(avgLearningAbility);

      Double avgSuccessRate = simulationStats.getAvgSuccessRate();
      avgSuccessRates.add(avgSuccessRate);

      simulationPlots.plotTwoSeriesOverIterations(simulationStats.getAvgLearningAbilities(),
          simulationStats.getSuccessRates(), "l_ab_&_s_rate, pComm = " + pComm, "Iteration",
          "Value", 0, 1, "Learning Ability", "Success Rate", 0.0, 1.2);

      IOUtils.exportToJson(avgLearningAbility, "out/" + folder + "/l_ab_pComm_" + pComm + ".json");
      IOUtils.exportToJson(avgSuccessRate, "out/" + folder + "/s_rate_pComm_" + pComm + ".json");
    }

    simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgLearningAbilities, "l_ab_over_p_comm_avg",
        "P_Communication", "Learning Ability", "Learning Ability", null, null, true);
    simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgSuccessRates, "s_rate_over_p_comm_avg",
        "P_Communication", "Success Rate", "Success Rate", null, null, true);

    IOUtils.exportToJson(avgLearningAbilities, "out/" + folder + "/l_ab_pComm_all.json");
    IOUtils.exportToJson(avgSuccessRates, "out/" + folder + "/s_rate_pComm_all.json");

    timer.stop("All simulations done");
  }
}
