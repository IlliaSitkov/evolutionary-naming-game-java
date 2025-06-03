package org.example.scenarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.VarConfig.ConfigKey;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.continueSimulation.LAbLangContinueSimulationStrategy;
import org.example.strategies.evolution.ProbabilisticEvolutionStrategy;
import org.example.strategies.learningAbilityAging.ConstantLAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.RandomLAbInheritanceStrategy;
import org.example.strategies.neighborPositions.Neighbor8PositionsStrategy;
import org.example.strategies.pCommunication.ConstantPCommunicationStrategy;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.strategies.wordAcquisition.UnitWordAcquisitionStrategy;
import org.example.utils.RunUtils;

public class ConstantPCommTransition {

  public static final String folder = "constant_p_comm_transition";

  public static void run(String tag, int L, int N, double pComm, double A, int nSteps, double pMut) {

    String folder = RunUtils.makePath(ConstantPCommTransition.folder, "/original/L", L, "N", N)
        + (tag == null ? "" : "/" + tag);

    VarConfig varConfig = new VarConfig(Map.of(
        ConfigKey.L, L,
        ConfigKey.P_MUT, pMut,
        ConfigKey.A, A,
        ConfigKey.T, nSteps,
        ConfigKey.N, N
        ));

    StrategyConfig strategyConfig = new StrategyConfig(
        new ConstantPCommunicationStrategy(pComm),
        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
        new RandomLAbInheritanceStrategy(),
        new ConstantLAbAgingStrategy(),
        new Neighbor8PositionsStrategy(),
        new UnitWordAcquisitionStrategy(),
        new ProbabilisticEvolutionStrategy(),
        new LAbLangContinueSimulationStrategy(0.95, 0)
    );

    List<Integer> iterations = new ArrayList<>();
      for (int i = 0; i < nSteps; i += 200) {
        iterations.add(i);
      }

    SimulationStats simulationStats = new SimulationStats(
        iterations,
        List.of(0.1, 0.11, 0.115, 0.12, 0.125, 0.13, 0.135, 0.14, 0.15, 0.16, 0.17, 0.18, 0.2, 0.22, 0.23, 0.24, 0.25,
            0.26, 0.28, 0.29, 0.3, 0.31, 0.32, 0.33, 0.34, 0.35,
            0.4, 0.41, 0.42, 0.43, 0.44, 0.45, 0.46, 0.47, 0.49, 0.5));

    Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

    RunUtils.runSimulation(simulation, folder);
  }

}
