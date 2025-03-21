package org.example.scenarios;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.VarConfig.ConfigKey;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.evolution.ProbabilisticEvolutionStrategy;
import org.example.strategies.learningAbilityAging.ConstantLAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.MoloneyRandomLAbInheritanceStrategy;
import org.example.strategies.learningAbilityInheritance.MutatedLAbInheritanceStrategy;
import org.example.strategies.learningAbilityInheritance.RandomLAbInheritanceStrategy;
import org.example.strategies.neighborPositions.Neighbor8PositionsStrategy;
import org.example.strategies.pCommunication.ContinuousIncreasePCommunicationStrategy;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.strategies.wordAcquisition.UnitWordAcquisitionStrategy;
import org.example.utils.RunUtils;

public class ContinuousPCommTransition {

  public static final String folder = "continuous_p_comm_transition";
  
  public static void original(int L, int N, double finalPComm, double A) {
        String folder = RunUtils.makePath(ContinuousPCommTransition.folder, "/original/L", L, "N", N);
        VarConfig varConfig = new VarConfig(Map.of(
                ConfigKey.L, L,
                ConfigKey.A, A,
                ConfigKey.T, 80000,
                ConfigKey.N, N
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                new ContinuousIncreasePCommunicationStrategy(0.1, finalPComm, varConfig.T()),
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new RandomLAbInheritanceStrategy(),
                new ConstantLAbAgingStrategy(),
                new Neighbor8PositionsStrategy(),
                new UnitWordAcquisitionStrategy(),
                new ProbabilisticEvolutionStrategy()
        );

        SimulationStats simulationStats = new SimulationStats(
                List.of(varConfig.T() - 1),
                List.of(0.1, 0.12, 0.13, 0.15,0.16,0.17, 0.18,0.2, 0.22, 0.25,0.26, 0.28,0.3,0.35, 0.4, 0.49, 0.5)
        );

        Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

        RunUtils.runSimulation(simulation, folder);
    }

    public static void originalMoloney(int L, int N, double finalPComm, double A) {
      String folder = RunUtils.makePath(ContinuousPCommTransition.folder, "/original_moloney/L", L, "N", N, '/', new Date().getTime());
      VarConfig varConfig = new VarConfig(Map.of(
              ConfigKey.L, L,
              ConfigKey.A, A,
              ConfigKey.T, 80000,
              ConfigKey.N, N,
              ConfigKey.REPR_LIPOWSKA, 0
      ));
      StrategyConfig strategyConfig = new StrategyConfig(
              new ContinuousIncreasePCommunicationStrategy(0.1, finalPComm, varConfig.T()),
              new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
              new MoloneyRandomLAbInheritanceStrategy(),
              new ConstantLAbAgingStrategy(),
              new Neighbor8PositionsStrategy(),
              new UnitWordAcquisitionStrategy(),
              new ProbabilisticEvolutionStrategy()
      );

      SimulationStats simulationStats = new SimulationStats(
              List.of(varConfig.T() - 1),
              List.of(0.1, 0.12, 0.13, 0.15, 0.18, 0.22, 0.25, 0.28, 0.4, 0.49, 0.5)
      );

      Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

      RunUtils.runSimulation(simulation, folder);
  }






}
