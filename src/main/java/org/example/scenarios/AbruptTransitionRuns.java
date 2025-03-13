package org.example.scenarios;

import java.util.List;
import java.util.Map;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.evolution.ProbabilisticEvolutionStrategy;
import org.example.strategies.learningAbilityAging.ConstantLAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.MutatedLAbInheritanceStrategy;
import org.example.strategies.learningAbilityInheritance.RandomLAbInheritanceStrategy;
import org.example.strategies.neighborPositions.Neighbor8PositionsStrategy;
import org.example.strategies.neighborPositions.NeighborPositionsStrategy;
import org.example.strategies.pCommunication.SingleStepPCommunicationStrategy;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.strategies.wordAcquisition.UnitWordAcquisitionStrategy;
import org.example.utils.RunUtils;

public class AbruptTransitionRuns {

  public static final String folder = "abrupt_p_comm_transition";

  public static void mutatedLAb(int L, int N, double stdDev) {
    String folder = AbruptTransitionRuns.folder + "/mutatedLAb/L=" + L + "_N=" + N + "_stdDev=" + stdDev;
    VarConfig varConfig = new VarConfig(Map.of(
        "L", L,
        "T", 50000,
        "N", N));
    StrategyConfig strategyConfig = new StrategyConfig(
        new SingleStepPCommunicationStrategy(0.1, 8000, 0.98),
        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
        new MutatedLAbInheritanceStrategy(stdDev),
        new ConstantLAbAgingStrategy(),
        new Neighbor8PositionsStrategy(),
        new UnitWordAcquisitionStrategy(),
        new ProbabilisticEvolutionStrategy());

    SimulationStats simulationStats = new SimulationStats(
        List.of(1000, 5000, 7990, 8000, 8500, 10000, 30000, 40000, varConfig.T() - 1),
        List.of());

    Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

    RunUtils.runSimulation(simulation, folder);
  }

  public static void original(int L, int N) {
    String folder = AbruptTransitionRuns.folder + "/original/L=" + L + "_N=" + N;
    VarConfig varConfig = new VarConfig(Map.of(
        "L", L,
        "T", 50000,
        "N", N));
    StrategyConfig strategyConfig = new StrategyConfig(
        new SingleStepPCommunicationStrategy(0.1, 8000, 0.98),
        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
        new RandomLAbInheritanceStrategy(),
        new ConstantLAbAgingStrategy(),
        new Neighbor8PositionsStrategy(),
        new UnitWordAcquisitionStrategy(),
        new ProbabilisticEvolutionStrategy());

    SimulationStats simulationStats = new SimulationStats(
        List.of(1000, 5000, 7990, 8000, 8500, 10000, 30000, 40000, varConfig.T() - 1),
        List.of());

    Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

    RunUtils.runSimulation(simulation, folder);
  }

  public static void neighbors(int L, int N, NeighborPositionsStrategy neighborPositionsStrategy) {
    String folder = AbruptTransitionRuns.folder + "/neighbors/L=" + L + "_N=" + N + "_neighb="+neighborPositionsStrategy;
    VarConfig varConfig = new VarConfig(Map.of(
        "L", L,
        "T", 50000,
        "N", N));
    StrategyConfig strategyConfig = new StrategyConfig(
        new SingleStepPCommunicationStrategy(0.1, 8000, 0.98),
        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
        new RandomLAbInheritanceStrategy(),
        new ConstantLAbAgingStrategy(),
        neighborPositionsStrategy,
        new UnitWordAcquisitionStrategy(),
        new ProbabilisticEvolutionStrategy());

    SimulationStats simulationStats = new SimulationStats(
        List.of(1000, 5000, 7990, 8000, 8500, 10000, 30000, 40000, varConfig.T() - 1),
        List.of());

    Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

    RunUtils.runSimulation(simulation, folder);
  }

}
