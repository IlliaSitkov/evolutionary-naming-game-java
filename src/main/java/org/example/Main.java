package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.example.export.IOUtils;
import org.example.plotting.SimulationPlots;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.learningAbilityInheritance.MutatedLearningAbilityInheritanceStrategy;
import org.example.strategies.learningAbilityInheritance.RandomLearningAbilityInheritanceStrategy;
import org.example.strategies.pCommunication.ConstantPCommunicationStrategy;
import org.example.strategies.pCommunication.ContinuousIncreasePCommunicationStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.strategies.pCommunication.SingleStepPCommunicationStrategy;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.utils.Timer;

public class Main {
    public static void main(String[] args) {
        runSimulationsInParallel();
        // runOrdinarySimulation();
    }

    public static void runSimulationsInParallel() {
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        List<Runnable> simulationTasks = List.of(
            // () -> abruptPCommIncrease(10, 1),
            // () -> abruptPCommIncrease(20, 1),
            // () -> abruptPCommIncrease(30, 1),
            // () -> abruptPCommIncrease(40, 1),
            // () -> abruptPCommIncrease(40, 3),
            // () -> abruptPCommIncrease(40, 5),
            // () -> abruptPCommIncrease(40, 10),
            // () -> abruptPCommIncrease(40, Integer.MAX_VALUE),

            // () -> continuousPCommIncrease(10, 1),
            // () -> continuousPCommIncrease(20, 1),
            // () -> continuousPCommIncrease(30, 1),
            // () -> continuousPCommIncrease(40, 1),
            // () -> continuousPCommIncrease(40, 5),
            // () -> continuousPCommIncrease(40, Integer.MAX_VALUE)

            // () -> abruptPCommIncrease(40, 1, 0.01),
            // // () -> abruptPCommIncrease(40, 1, 0.03),
            // () -> abruptPCommIncrease(40, 1, 0.05),
            // // () -> abruptPCommIncrease(40, 1, 0.075),
            // () -> abruptPCommIncrease(40, 1, 0.1),
            // // () -> abruptPCommIncrease(40, 1, 0.15),
            // () -> abruptPCommIncrease(40, 1, 0.2),

            () -> continuousPCommIncrease(40, 1, 0.01),
            // () -> continuousPCommIncrease(40, 1, 0.03),
            () -> continuousPCommIncrease(40, 1, 0.05),
            // () -> continuousPCommIncrease(40, 1, 0.075),
            () -> continuousPCommIncrease(40, 1, 0.1),
            // () -> continuousPCommIncrease(40, 1, 0.15),
            () -> continuousPCommIncrease(40, 1, 0.2)
        );

        for (Runnable task : simulationTasks) {
            executorService.submit(task);
        }

        executorService.shutdown();
    }

    public static void abruptPCommIncrease(int L, int N, double stdDev) {
        Timer timer = new Timer();
        timer.start();

        String folder = "abrupt_p_comm_increase/L=" + L + "_N=" + N+"_stdDev="+stdDev;
        VarConfig varConfig = new VarConfig(Map.of(
                "L", L,
                "T", 50000,
                "N", N
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                new SingleStepPCommunicationStrategy(0.1, 8000, 0.98),
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new MutatedLearningAbilityInheritanceStrategy(stdDev)
        );

        SimulationStats simulationStats = new SimulationStats(
                List.of(1000, 5000, 7990, 8000, 8500, 10000, 30000, 40000, varConfig.T() - 1),
                List.of()
        );

        Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

        SimulationPlots simulationPlots = new SimulationPlots(folder);

        simulation.start();

        timer.stop("Simulation ended");

        simulationPlots.saveSimulationStats(simulationStats, strategyConfig.getPCommunicationStrategy(), varConfig.T());
        IOUtils.saveRunConfig(folder, varConfig, strategyConfig);
    }

    public static void continuousPCommIncrease(int L, int N, double stdDev) {
        Timer timer = new Timer();
        timer.start();

        String folder = "continuous_p_comm_increase0.1-0.5/L=" + L+"_N="+N+"_stdDev="+stdDev;
        VarConfig varConfig = new VarConfig(Map.of(
                "L", L,
                "T", 80000,
                "N", N
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                new ContinuousIncreasePCommunicationStrategy(0.1, 0.5, varConfig.T()),
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new MutatedLearningAbilityInheritanceStrategy(stdDev)
        );

        SimulationStats simulationStats = new SimulationStats(
                List.of(varConfig.T() - 1),
                List.of(0.1, 0.12, 0.13, 0.15, 0.18, 0.22, 0.25, 0.28, 0.4, 0.49)
        );

        Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

        SimulationPlots simulationPlots = new SimulationPlots(folder);

        simulation.start();

        timer.stop("Simulation ended");

        simulationPlots.saveSimulationStats(simulationStats, strategyConfig.getPCommunicationStrategy(), varConfig.T());
        IOUtils.saveRunConfig(folder, varConfig, strategyConfig);
    }
    
    public static void runOrdinarySimulation() {
        Timer timer = new Timer();
        timer.start();

        String folder = "TEST";
        VarConfig varConfig = new VarConfig(Map.of(
                "L", 20,
                "T", 10000
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                new SingleStepPCommunicationStrategy(0.1, 5000, 0.98),
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new MutatedLearningAbilityInheritanceStrategy(0.1)
        );

        SimulationStats simulationStats = new SimulationStats();

        Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

        SimulationPlots simulationPlots = new SimulationPlots(folder);

        simulation.start();

        timer.stop("Simulation ended");

        simulationPlots.saveSimulationStats(simulationStats, strategyConfig.getPCommunicationStrategy(), varConfig.T());
        IOUtils.saveRunConfig(folder, varConfig, strategyConfig);
    }

    public static void runPCommSimulations() {

        VarConfig varConfig = new VarConfig(Map.of(
                "L", 20,
                "T", 5000
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                null,
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new RandomLearningAbilityInheritanceStrategy()
        );

        int nSkipIterations = 1500;

        String folder = "p_comm_test";
        SimulationPlots simulationPlots = new SimulationPlots(folder);

        List<Double> pCommunicationValues = List.of(0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17);
        List<Double> avgSuccessRates = new ArrayList<>();
        List<Double> avgLearningAbilities = new ArrayList<>();

        Simulation simulation = new Simulation(null, varConfig, strategyConfig);

        IOUtils.saveRunConfig(folder, varConfig, strategyConfig);

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
}