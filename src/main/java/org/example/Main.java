package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.example.VarConfig.ConfigKey;
import org.example.export.IOUtils;
import org.example.plotting.SimulationPlots;
import org.example.scenarios.AbruptTransitionRuns;
import org.example.scenarios.ContinuousPCommTransition;
import org.example.scenarios.PCommDecrease;
import org.example.scenarios.PCommIncrease;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.evolution.FullEvolutionStrategy;
import org.example.strategies.evolution.ProbabilisticEvolutionStrategy;
import org.example.strategies.learningAbilityAging.ConstantLAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.MutatedLAbInheritanceStrategy;
import org.example.strategies.learningAbilityInheritance.RandomLAbInheritanceStrategy;
import org.example.strategies.neighborPositions.Neighbor4PositionsStrategy;
import org.example.strategies.neighborPositions.Neighbor8PositionsStrategy;
import org.example.strategies.pCommunication.ConstantPCommunicationStrategy;
import org.example.strategies.pCommunication.ContinuousIncreasePCommunicationStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.strategies.pCommunication.SingleStepPCommunicationStrategy;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.strategies.wordAcquisition.UnitWordAcquisitionStrategy;
import org.example.utils.Timer;

public class Main {
    public static void main(String[] args) {
        runSimulationsInParallel();
    }

    public static void runSimulationsInParallel() {
        ExecutorService executorService = Executors.newFixedThreadPool(16);

        List<Runnable> simulationTasks = List.of(
            // () -> ContinuousPCommTransition.original(40, 1, 0.5, 0.05, 100000, 100000),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.05, 100000, 100000, 0.01),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.05, 100000, 100000, 0.05),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.05, 100000, 100000, 0.075),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.05, 100000, 100000, 0.1),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.05, 100000, 100000, 5),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.05, 200000, 200000, 0.05),
            // () -> ContinuousPCommTransition.original(40, 1, 0.98, 0.05, 200000, 200000)

            // () -> ContinuousPCommTransition.mutatedLAb(60, 1, 0.5, 0.05, 100000, 100000, 0.1),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.05, 200000, 200000, 0.05),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.05, 100000, 100000, 0.025)


            () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, 1),
            () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, 2)
            // () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, 3),
            // () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, 4),
            // () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, 5)

            // () -> PCommIncrease.mutatedLAb(40, 0.05, 1, 10000, 3000,  0.14, 0.01, 0.05),
            // () -> PCommIncrease.mutatedLAb(40, 0.05, 1, 10000, 3000,  0.14, 0.01, 5),
            // () -> PCommIncrease.mutatedLAb(40, 0.05, 1, 10000, 3000,  0.18, 0.08, 0.01)
        );

        for (Runnable task : simulationTasks) {
            executorService.submit(task);
        }

        executorService.shutdown();
    }

    public static void abruptPCommIncrease(int L, int N, double stdDev) {
        String folder = "abrupt_p_comm_increase/L=" + L + "_N=" + N+"_stdDev="+stdDev;
        VarConfig varConfig = new VarConfig(Map.of(
                ConfigKey.L, L,
                ConfigKey.T, 50000,
                ConfigKey.N, N
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                new SingleStepPCommunicationStrategy(0.1, 8000, 0.98),
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new MutatedLAbInheritanceStrategy(stdDev),
                new ConstantLAbAgingStrategy(),
                new Neighbor8PositionsStrategy(),
                new UnitWordAcquisitionStrategy(),
                new ProbabilisticEvolutionStrategy()
        );

        SimulationStats simulationStats = new SimulationStats(
                List.of(1000, 5000, 7990, 8000, 8500, 10000, 30000, 40000, varConfig.T() - 1),
                List.of()
        );

        Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

        runSimulation(simulation, folder);
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


    public static void continuousPCommIncrease(int L, int N, double stdDev) {
        String folder = "continuous_p_comm_increase0.1-0.5/L=" + L+"_N="+N+"_stdDev="+stdDev;
        VarConfig varConfig = new VarConfig(Map.of(
                ConfigKey.L, L,
                ConfigKey.T, 80000,
                ConfigKey.N, N
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                new ContinuousIncreasePCommunicationStrategy(0.1, 0.5, varConfig.T()),
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new MutatedLAbInheritanceStrategy(stdDev),
                new ConstantLAbAgingStrategy(),
                new Neighbor8PositionsStrategy(),
                new UnitWordAcquisitionStrategy(),
                new ProbabilisticEvolutionStrategy()
        );

        SimulationStats simulationStats = new SimulationStats(
                List.of(varConfig.T() - 1),
                List.of(0.1, 0.12, 0.13, 0.15, 0.18, 0.22, 0.25, 0.28, 0.4, 0.49)
        );

        Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

        runSimulation(simulation, folder);
    }
    
    public static void runOrdinarySimulation() {
        String folder = "TEST";
        VarConfig varConfig = new VarConfig(Map.of(
                ConfigKey.L, 20,
                ConfigKey.T, 10000
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                new SingleStepPCommunicationStrategy(0.1, 5000, 0.98),
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new MutatedLAbInheritanceStrategy(0.1),
                new ConstantLAbAgingStrategy(),
                new Neighbor8PositionsStrategy(),
                new UnitWordAcquisitionStrategy(),
                new ProbabilisticEvolutionStrategy()
        );

        SimulationStats simulationStats = new SimulationStats();

        Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

        runSimulation(simulation, folder);
    }

    public static void runPCommIncreaseSimulations() {

        VarConfig varConfig = new VarConfig(Map.of(
                ConfigKey.T, 3000
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                null,
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new RandomLAbInheritanceStrategy(),
                new ConstantLAbAgingStrategy(),
                new Neighbor8PositionsStrategy(),
                new UnitWordAcquisitionStrategy(),
                new ProbabilisticEvolutionStrategy()
        );

        int nSkipIterations = 1000;

        String folder = "p_comm_new_0.11-0.27_3k_iters";
        SimulationPlots simulationPlots = new SimulationPlots(folder);

        List<Double> pCommunicationValues = List.of(0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.2, 0.21, 0.22, 0.23, 0.24, 0.25, 0.26, 0.27);
        List<Double> avgSuccessRates = new ArrayList<>();
        List<Double> avgLearningAbilities = new ArrayList<>();

        Simulation simulation = new Simulation(null, varConfig, strategyConfig);

        IOUtils.saveSimulationConfig(folder, simulation);

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

            IOUtils.exportToJson(avgLearningAbility, "out/" + folder + "/l_ab_pComm_"+pComm+".json");
            IOUtils.exportToJson(avgSuccessRate, "out/" + folder + "/s_rate_pComm_"+pComm+".json");
            IOUtils.saveWorld(simulation.getWorld(), "out/" + folder + "/world_after_pComm_"+pComm+".ser");
        }

        simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgLearningAbilities, "l_ab_over_p_comm", "P_Communication", "Learning Ability", "Learning Ability", null, null, true);
        simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgSuccessRates, "s_rate_over_p_comm", "P_Communication", "Success Rate", "Success Rate", null, null, true);
        
        IOUtils.exportToJson(avgLearningAbilities, "out/" + folder + "/l_ab_pComm_all.json");
        IOUtils.exportToJson(avgSuccessRates, "out/" + folder + "/s_rate_pComm_all.json");

        timer.stop("All simulations done");
    }

    public static void runPCommDecreaseSimulations() {

        VarConfig varConfig = new VarConfig(Map.of(
                ConfigKey.T, 100
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                new ConstantPCommunicationStrategy(0.99),
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new RandomLAbInheritanceStrategy(),
                new ConstantLAbAgingStrategy(),
                new Neighbor8PositionsStrategy(),
                new UnitWordAcquisitionStrategy(),
                new FullEvolutionStrategy()
        );
        String folder = "p_comm_decrease/full_pre-sim";
        String preSimulationStatsFolder = folder + "/pre-simulation";
        SimulationPlots preSimulationPlots = new SimulationPlots(preSimulationStatsFolder);
        SimulationPlots simulationPlots = new SimulationPlots(folder);

        Simulation simulation = new Simulation(new SimulationStats(List.of(4000, 4500, 4999), List.of()), varConfig, strategyConfig);

        IOUtils.saveSimulationConfig(preSimulationStatsFolder, simulation);
        simulation.start();

        preSimulationPlots.saveSimulationStats(simulation.getSimulationStats(), strategyConfig.getPCommunicationStrategy(), varConfig.T());
        varConfig.setT(3000);

        int nSkipIterations = 1000;

        List<Double> pCommunicationValues = List.of(0.27, 0.26, 0.25, 0.24, 0.23, 0.22, 0.21, 0.2, 0.19, 0.18, 0.17, 0.16, 0.15, 0.14, 0.13, 0.12, 0.11);
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

            IOUtils.exportToJson(avgLearningAbility, "out/" + folder + "/l_ab_pComm_"+pComm+".json");
            IOUtils.exportToJson(avgSuccessRate, "out/" + folder + "/s_rate_pComm_"+pComm+".json");
            IOUtils.saveWorld(simulation.getWorld(), "out/" + folder + "/world_after_pComm_"+pComm+".ser");
        }

        simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgLearningAbilities, "l_ab_over_p_comm_avg", "P_Communication", "Learning Ability", "Learning Ability", null, null, true);
        simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgSuccessRates, "s_rate_over_p_comm_avg", "P_Communication", "Success Rate", "Success Rate", null, null, true);
        
        IOUtils.exportToJson(avgLearningAbilities, "out/" + folder + "/l_ab_pComm_all.json");
        IOUtils.exportToJson(avgSuccessRates, "out/" + folder + "/s_rate_pComm_all.json");

        timer.stop("All simulations done");
    }
}