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
import org.example.strategies.learningAbilityInheritance.RandomLAbInheritanceStrategy;
import org.example.strategies.neighborPositions.Neighbor8PositionsStrategy;
import org.example.strategies.neighborPositions.NeighborPositionsStrategy;
import org.example.strategies.pCommunication.ConstantPCommunicationStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.strategies.wordAcquisition.UnitWordAcquisitionStrategy;
import org.example.utils.RunUtils;
import org.example.utils.Timer;

public class PCommDecrease {

    public static final String folder = "p_comm_decrease";

    public static void original(String tag, int L, int N, double A, boolean moloneyImpl, int nSkipIterations,
            int nIterationsPerStep, double minPComm, double maxPComm) {
        original(tag, L, N, A, moloneyImpl, nSkipIterations, nIterationsPerStep, minPComm, maxPComm,
                new Neighbor8PositionsStrategy());
    }

    public static void original(String tag, int L, int N, double A, boolean moloneyImpl, int nSkipIterations,
            int nIterationsPerStep, double minPComm, double maxPComm,
            NeighborPositionsStrategy neighborPositionsStrategy) {

        int preSimulationStepsNumber = 10000;
        double preSimulationPComm = 0.65;

        VarConfig varConfig = new VarConfig(Map.of(
                ConfigKey.T, preSimulationStepsNumber,
                ConfigKey.L, L,
                ConfigKey.N, N,
                ConfigKey.SKIP_ITERATIONS, nSkipIterations,
                ConfigKey.A, A,
                ConfigKey.REPR_LIPOWSKA, moloneyImpl ? 0 : 1));

        StrategyConfig strategyConfig = new StrategyConfig(
                new ConstantPCommunicationStrategy(preSimulationPComm),
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new RandomLAbInheritanceStrategy(),
                new ConstantLAbAgingStrategy(),
                neighborPositionsStrategy,
                new UnitWordAcquisitionStrategy(),
                new ProbabilisticEvolutionStrategy());

        try {
            Thread.sleep(new Random().nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String subfolder = moloneyImpl ? "original_moloney" : "original";
        String baseFolder = RunUtils.makePath(PCommDecrease.folder, "/", "natural_world", "/", subfolder, "/", "L", L)
                + (tag == null ? "" : "/" + tag);
        String folder = RunUtils.makePath(baseFolder, "/", new Date().getTime());
        String preSimulationStatsFolder = folder + "/pre-simulation";
        SimulationPlots preSimulationPlots = new SimulationPlots(preSimulationStatsFolder);
        SimulationPlots simulationPlots = new SimulationPlots(folder);

        Simulation simulation = new Simulation(
                new SimulationStats(List.of(4000, 4500, 4999, 10000, 13000, 20000, 29000, 29999), List.of()), varConfig,
                strategyConfig);

        simulation.start();
        IOUtils.saveSimulationConfig(preSimulationStatsFolder, simulation);

        List<Double> pCommOverIterations = SimulationStats
                .getPCommunicationOverIterations(simulation.getPCommunicationStrategy(), varConfig.T(), true);
        RunUtils.saveStats(preSimulationStatsFolder + "/run_stats.csv", simulation.getSimulationStats(),
                pCommOverIterations);

        preSimulationPlots.saveSimulationStats(simulation.getSimulationStats(),
                strategyConfig.getPCommunicationStrategy(),
                varConfig.T());
        varConfig.setT(nIterationsPerStep);

        maxPComm += 0.0001;
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

            List<Double> pCommOverIterationsSingleStep = SimulationStats.getPCommunicationOverIterations(strategy,
                    varConfig.T(), true);
            RunUtils.saveStats(folder + "/pComm_" + pComm + "_run_stats.csv", simulationStats,
                    pCommOverIterationsSingleStep);
        }

        IOUtils.saveSimulationConfig(folder, simulation);

        simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgLearningAbilities,
                "l_ab_over_p_comm_avg",
                "P_Communication", "Learning Ability", "Learning Ability", null, null, true);
        simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgSuccessRates, "s_rate_over_p_comm_avg",
                "P_Communication", "Success Rate", "Success Rate", null, null, true);

        IOUtils.exportToJson(avgLearningAbilities, "out/" + folder + "/l_ab_pComm_all.json");
        IOUtils.exportToJson(avgSuccessRates, "out/" + folder + "/s_rate_pComm_all.json");

        timer.stop("All simulations done");
    }
}
