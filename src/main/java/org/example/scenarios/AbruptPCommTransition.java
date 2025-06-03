package org.example.scenarios;

import java.util.List;
import java.util.Map;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.VarConfig.ConfigKey;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.evolution.ProbabilisticEvolutionStrategy;
import org.example.strategies.learningAbilityAging.ConstantLAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.RandomLAbInheritanceStrategy;
import org.example.strategies.neighborPositions.Neighbor8PositionsStrategy;
import org.example.strategies.neighborPositions.NeighborPositionsStrategy;
import org.example.strategies.pCommunication.SingleStepPCommunicationStrategy;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.strategies.wordAcquisition.UnitWordAcquisitionStrategy;
import org.example.utils.RunUtils;

public class AbruptPCommTransition {

  public static final String folder = "abrupt_p_comm_transition";

  public static void original(String tag, int L, int N, double A) {
    original(tag, L, N, A, 0);
  }

  public static void original(String tag, int L, int N, double A, int initAge) {
    original(tag, L, N, A, initAge, new Neighbor8PositionsStrategy());
  }

  public static void original(String tag, int L, int N, double A, int initAge,
      NeighborPositionsStrategy neighborPositionsStrategy) {
    String folder = RunUtils.makePath(AbruptPCommTransition.folder, "/original/L", L, "N", N)
        + (tag == null ? "" : "/" + tag);

    VarConfig varConfig = new VarConfig(Map.of(
        ConfigKey.L, L,
        ConfigKey.A, A,
        ConfigKey.T, 50000,
        ConfigKey.N, N,
        ConfigKey.INIT_AGE, initAge));
    StrategyConfig strategyConfig = new StrategyConfig(
        new SingleStepPCommunicationStrategy(0.1, 8000, 0.98),
        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
        new RandomLAbInheritanceStrategy(),
        new ConstantLAbAgingStrategy(),
        neighborPositionsStrategy,
        new UnitWordAcquisitionStrategy(),
        new ProbabilisticEvolutionStrategy());

    SimulationStats simulationStats = new SimulationStats(
        List.of(1000, 5000, 7990, 8000, 8050, 8100, 8200, 8500, 10000, 15000, 20000, 30000, 35000, 37000, 40000,
            varConfig.T() - 1),
        List.of(), true, 0.99);

    Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

    RunUtils.runSimulation(simulation, folder);
  }

  public static void originalMoloney(int L, int N, double A) {
    originalMoloney(L, N, A, 0);
  }

  public static void originalMoloney(int L, int N, double A, int initAge) {
    originalMoloney(L, N, A, initAge, new Neighbor8PositionsStrategy());
  }

  public static void originalMoloney(int L, int N, double A, int initAge,
      NeighborPositionsStrategy neighborPositionsStrategy) {
    String folder = RunUtils.makePath(AbruptPCommTransition.folder, "/original_moloney/L", L, "N", N);
    VarConfig varConfig = new VarConfig(Map.of(
        ConfigKey.L, L,
        ConfigKey.A, A,
        ConfigKey.T, 50000,
        ConfigKey.N, N,
        ConfigKey.REPR_LIPOWSKA, 0,
        ConfigKey.INIT_AGE, initAge));
    StrategyConfig strategyConfig = new StrategyConfig(
        new SingleStepPCommunicationStrategy(0.1, 8000, 0.98),
        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
        new RandomLAbInheritanceStrategy(),
        new ConstantLAbAgingStrategy(),
        neighborPositionsStrategy,
        new UnitWordAcquisitionStrategy(),
        new ProbabilisticEvolutionStrategy());

    SimulationStats simulationStats = new SimulationStats(
        List.of(1000, 5000, 7990, 8000, 8050, 8100, 8200, 8500, 10000, 15000, 20000, 30000, 35000, 37000, 40000,
            varConfig.T() - 1),
        List.of());

    Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

    RunUtils.runSimulation(simulation, folder);
  }

}
