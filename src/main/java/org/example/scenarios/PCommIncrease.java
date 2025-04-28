package org.example.scenarios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.VarConfig.ConfigKey;
import org.example.export.IOUtils;
import org.example.plotting.SimulationPlots;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.evolution.ProbabilisticEvolutionStrategy;
import org.example.strategies.learningAbilityAging.ConstantLAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.MoloneyRandomLAbInheritanceStrategy;
import org.example.strategies.learningAbilityInheritance.RandomLAbInheritanceStrategy;
import org.example.strategies.neighborPositions.Neighbor8PositionsStrategy;
import org.example.strategies.pCommunication.ConstantPCommunicationStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.strategies.wordAcquisition.UnitWordAcquisitionStrategy;
import org.example.utils.RunUtils;
import org.example.utils.Timer;

public class PCommIncrease {

    public static final String folder = "p_comm_increase";

    public static void original(int L, double A, int N, int iterationsPerStep, int nSkipIterations, double minPComm, double maxPComm) {
        runPCommIncreaseSimulations(L, A, N, false, iterationsPerStep, nSkipIterations, maxPComm, minPComm);
    }

    public static void original(int L, double A, int N, int iterationsPerStep, int nSkipIterations, double maxPComm) {
        runPCommIncreaseSimulations(L, A, N, false, iterationsPerStep, nSkipIterations, maxPComm, 0.11);
    }
    
    public static void originalMoloney(int L, double A, int N, int iterationsPerStep, int nSkipIterations, double maxPComm) {
        runPCommIncreaseSimulations(L, A, N, true, iterationsPerStep, nSkipIterations, maxPComm, 0.11);
    }

    public static void runPCommIncreaseSimulations(int L, double A, int N, boolean moloneyImpl, int iterationsPerStep, int nSkipIterations, double maxPComm, double minPComm) {
        maxPComm += 0.0001;
        VarConfig varConfig = new VarConfig(Map.of(
                ConfigKey.T, iterationsPerStep,
                ConfigKey.N, N,
                ConfigKey.A, A,
                ConfigKey.L, L,
                ConfigKey.SKIP_ITERATIONS, nSkipIterations,
                ConfigKey.REPR_LIPOWSKA, moloneyImpl ? 0 : 1));
        StrategyConfig strategyConfig = new StrategyConfig(
                null,
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                moloneyImpl ? new MoloneyRandomLAbInheritanceStrategy() : new RandomLAbInheritanceStrategy(),
                new ConstantLAbAgingStrategy(),
                new Neighbor8PositionsStrategy(),
                new UnitWordAcquisitionStrategy(),
                new ProbabilisticEvolutionStrategy());


        String subfolder = moloneyImpl ? "original_moloney" : "original";
        try {
            Thread.sleep(new Random().nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String folder = RunUtils.makePath(PCommIncrease.folder, "/", subfolder, "/", "L", L, "/", new Date().getTime());
        SimulationPlots simulationPlots = new SimulationPlots(folder);

        // List<Double> pCommunicationValues = List.of(0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.2, 0.21,
        //         0.22, 0.23, 0.24, 0.25, 0.26, 0.27);
        // List<Double> pCommunicationValues = List.of(0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19);

        List<Double> pCommunicationValues = new ArrayList<>();
        for (double i = minPComm; i <= maxPComm; i += 0.01) {
            pCommunicationValues.add(Math.round(i * 100.0) / 100.0);
        }

        List<Double> avgSuccessRates = new ArrayList<>();
        List<Double> avgLearningAbilities = new ArrayList<>();

        Simulation simulation = new Simulation(null, varConfig, strategyConfig);

        Timer timer = new Timer();
        timer.start();

        for (double pComm : pCommunicationValues) {
            System.out.println("Starting simulation for pComm = " + pComm);

            Timer simulationTimer = new Timer();
            simulationTimer.start();

            PCommunicationStrategy strategy = new ConstantPCommunicationStrategy(pComm);
            SimulationStats simulationStats = new SimulationStats(varConfig.SKIP_ITERATIONS());

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

            List<Double> pCommOverIterations = SimulationStats.getPCommunicationOverIterations(strategy, varConfig.T(), true);
            RunUtils.saveStats(folder + "/pComm_" + pComm + "_run_stats.csv", simulationStats, pCommOverIterations);
        }

        IOUtils.saveSimulationConfig(folder, simulation);

        simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgLearningAbilities, "l_ab_over_p_comm",
                "P_Communication", "Learning Ability", "Learning Ability", null, null, true);
        simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgSuccessRates, "s_rate_over_p_comm",
                "P_Communication", "Success Rate", "Success Rate", null, null, true);

        IOUtils.exportToJson(avgLearningAbilities, "out/" + folder + "/l_ab_pComm_all.json");
        IOUtils.exportToJson(avgSuccessRates, "out/" + folder + "/s_rate_pComm_all.json");

        timer.stop("All simulations done");
    }
}
