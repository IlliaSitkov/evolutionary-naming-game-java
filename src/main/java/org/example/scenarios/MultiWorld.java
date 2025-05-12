package org.example.scenarios;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.VarConfig.ConfigKey;
import org.example.entities.World;
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

public class MultiWorld {
  
  public static final String folder = "multi_world";


  public static World evolveWorld(int L_COLS, int L_ROWS, int N, double A, String folder, String subfolder) {
    int preSimulationStepsNumber = 20000;
    double preSimulationPComm = 0.65;
    double uniformLangThreshold = 0.99;
    double uniformLAbThreshold = 0.95;

    VarConfig varConfig = new VarConfig(Map.of(
        ConfigKey.T, preSimulationStepsNumber,
        ConfigKey.L_COLS, L_COLS,
        ConfigKey.L_ROWS, L_ROWS,
        ConfigKey.N, N,
        ConfigKey.A, A
        ));

    StrategyConfig strategyConfig = new StrategyConfig(
        new ConstantPCommunicationStrategy(preSimulationPComm),
        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
        new RandomLAbInheritanceStrategy(),
        new ConstantLAbAgingStrategy(),
        new Neighbor8PositionsStrategy(),
        new UnitWordAcquisitionStrategy(),
        new ProbabilisticEvolutionStrategy(),
        new LAbLangContinueSimulationStrategy(uniformLangThreshold, uniformLAbThreshold)
        );

    String preSimulationStatsFolder = folder + "/" + subfolder;
    SimulationStats stats = new SimulationStats(List.of(0, varConfig.T() - 1), List.of());
    
    Simulation simulation = new Simulation(stats, varConfig, strategyConfig);

    RunUtils.runSimulation(simulation, preSimulationStatsFolder, true);

    return simulation.getWorld();
  }


  public static void joinTerritory(int L, int N, double A) {
    try {
      Thread.sleep(new Random().nextInt(2000) + new Random().nextInt(2000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    String folder = RunUtils.makePath(MultiWorld.folder,"/", "join_territory", "/", "L", L, "N", N, "/", new Date().getTime());

    World world1, world2;

    ExecutorService executor = Executors.newFixedThreadPool(2);

    CompletableFuture<World> future1 = CompletableFuture.supplyAsync(() -> evolveWorld(L/2, L, N, A, folder, "world1"), executor);
    CompletableFuture<World> future2 = CompletableFuture.supplyAsync(() -> evolveWorld(L/2, L, N, A, folder, "world2"), executor);

    CompletableFuture<Void> allDone = CompletableFuture.allOf(future1, future2);

    allDone.join();

    world1 = future1.join();
    world2 = future2.join();

    double pComm = 0.65;
    int nIterations = 100000;

    VarConfig varConfig = new VarConfig(Map.of(
        ConfigKey.T, nIterations,
        ConfigKey.N, N,
        ConfigKey.A, A,
        ConfigKey.L, L
        ));

    StrategyConfig strategyConfig = new StrategyConfig(
        new ConstantPCommunicationStrategy(pComm),
        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
        new RandomLAbInheritanceStrategy(),
        new ConstantLAbAgingStrategy(),
        new Neighbor8PositionsStrategy(),
        new UnitWordAcquisitionStrategy(),
        new ProbabilisticEvolutionStrategy());

    World world3 = World.mergeWorlds(world1, world2, varConfig, strategyConfig);

    SimulationStats stats = new SimulationStats(
      List.of(0, 1000, 2000, 5000, 20000, 50000, 60000, 90000, varConfig.T() - 1),
      List.of()
      );

    Simulation simulation = new Simulation(world3, stats, varConfig, strategyConfig);

    RunUtils.runSimulation(simulation, folder, true);
  }

  public static void relocate(int L, int N, double A, int relocatedN) {
    try {
      Thread.sleep(new Random().nextInt(2000) + new Random().nextInt(2000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    String folder = RunUtils.makePath(MultiWorld.folder,"/", "relocated", "/", "L", L, "N", N, "/", new Date().getTime());

    World world1, world2;

    ExecutorService executor = Executors.newFixedThreadPool(2);

    CompletableFuture<World> future1 = CompletableFuture.supplyAsync(() -> evolveWorld(L, L, N, A, folder, "world1"), executor);
    CompletableFuture<World> future2 = CompletableFuture.supplyAsync(() -> evolveWorld(L, L, N, A, folder, "world2"), executor);

    CompletableFuture<Void> allDone = CompletableFuture.allOf(future1, future2);

    allDone.join();

    world1 = future1.join();
    world2 = future2.join();

    double pComm = 0.65;
    int nIterations = 100000;

    VarConfig varConfig = new VarConfig(Map.of(
        ConfigKey.T, nIterations,
        ConfigKey.N, N,
        ConfigKey.A, A,
        ConfigKey.L, L
        ));

    StrategyConfig strategyConfig = new StrategyConfig(
        new ConstantPCommunicationStrategy(pComm),
        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
        new RandomLAbInheritanceStrategy(),
        new ConstantLAbAgingStrategy(),
        new Neighbor8PositionsStrategy(),
        new UnitWordAcquisitionStrategy(),
        new ProbabilisticEvolutionStrategy());

    world1.takeAgentsFrom(world2, relocatedN);

    SimulationStats stats = new SimulationStats(
      List.of(0, 1000, 2000, 5000, 20000, 50000, 60000, 90000, varConfig.T() - 1),
      List.of()
      );

    Simulation simulation = new Simulation(world1, stats, varConfig, strategyConfig);

    RunUtils.runSimulation(simulation, folder, true);
  }


}
