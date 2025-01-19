package org.example;

import java.util.ArrayList;
import java.util.List;

import org.example.entities.World;
import org.example.export.IOUtils;
import org.example.plotting.SimulationPlots;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.pCommunication.ConstantPCommunicationStrategy;
import org.example.strategies.pCommunication.ContinuousIncreasePCommunicationStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.strategies.pCommunication.SingleStepPCommunicationStrategy;
import org.example.utils.Timer;

public class Main {
    public static void main(String[] args) {
        runPCommSimulations();
    }

    public static void runOrdinarySimulation() {
        Timer timer = new Timer();
        timer.start();

        int nIters = 50000;
        int worldSize = 20;
        double A = 0.05;
        double B = 5;
        double pMutation = 0.001;
        SimulationStats simulationStats = new SimulationStats(List.of(1000, 7000, 7999, 8000, 8050, 9000, nIters - 1),
                List.of(0.11, 0.12, 0.14, 0.22, 0.24, 0.25, 0.3));
        // PCommunicationStrategy strategy = new SingleStepPCommunicationStrategy(0.1,
        // 8000, 0.98);
        PCommunicationStrategy strategy = new ContinuousIncreasePCommunicationStrategy(0.1, 0.5, nIters);

        Simulation simulation = new Simulation(nIters, worldSize, strategy, simulationStats, pMutation, A, B);

        String folder = "Test1";
        SimulationPlots.setFolderName(folder);

        simulation.start();

        timer.stop("Simulation ended");

        SimulationPlots.plotTwoSeriesOverIterations(simulationStats.getAvgLearningAbilities(),
                simulationStats.getSuccessRates(), "Learning Ability and Success Rate Over Iterations", "Iteration",
                "Value", 0, 1, "Learning Ability", "Success Rate", 0.0, 1.2);
        SimulationPlots.plotStat(simulationStats.getSuccessRates(), "Success Rate Over Iterations", "Success Rate",
                "Success Rate", 1, 0.0, 1.2);
        SimulationPlots.plotStat(simulationStats.getAvgLearningAbilities(), "Average Learning Ability Over Iterations",
                "Average Learning Ability", "Learning Ability", 0, 0.0, 1.2);
        SimulationPlots.plotStat(simulationStats.getLanguagesNumber(), "Languages Number Over Iterations",
                "Languages Number", "Languages Number", 0);
        SimulationPlots.plotStat(simulationStats.getCommunicationsNumber(), "Communications Number Over Iterations",
                "Communications Number", "Communications Number", 1);
        SimulationPlots.plotStat(simulationStats.getAvgAges(), "Average Age Over Iterations", "Average Age", "Age", 0);
        SimulationPlots.plotStat(simulationStats.getKilledAgentsNumber(), "Killed Agents Number Over Iterations",
                "Killed Agents Number", "Killed Agents Number", 1);
        SimulationPlots.plotStat(simulationStats.getAvgKnowledge(), "Average Knowledge Over Iterations",
                "Avg Knowledge", "Avg Knowledge", 0);
        SimulationPlots.plotStat(simulationStats.getBornAgentsNumber(), "Born Agents Over Iterations",
                "Number Of Born Agents", "Number Of Born Agents", 1);
        SimulationPlots.plotStat(simulationStats.getNAgentsAlive(), "Alive Over Iterations", "Number Of Alive Agents",
                "Number Of Alive Agents", 0);

        SimulationPlots.plotSeriesAsDependentOnAnother(
                SimulationStats.getPCommunicationOverIterations(strategy, nIters),
                simulationStats.getSuccessRates(),
                "Success rate over P_Comm",
                "P_Communication",
                "Success rate",
                "Success rate",
                0.0, 1.2);

        SimulationPlots.plotSeriesAsDependentOnAnother(
                SimulationStats.getPCommunicationOverIterations(strategy, nIters),
                simulationStats.getAvgLearningAbilities(),
                "Learning ability over P_Comm",
                "P_Communication",
                "Learning ability",
                "Learning ability",
                0.0, 1.2);

        SimulationPlots.plotSeriesAsDependentOnAnother(
                simulationStats.getAvgLearningAbilities(),
                simulationStats.getAvgKnowledge(),
                "Knowledge Over Learning ability",
                "Learning ability",
                "Knowledge",
                "Knowledge",
                null, null);

        SimulationPlots.plotSeriesAsDependentOnAnother(
                simulationStats.getAvgKnowledge(),
                simulationStats.getSuccessRates(),
                "Success Rate Over Knowledge",
                "Knowledge",
                "Success Rate",
                "Success Rate",
                null, null);

        IOUtils.exportToJson(simulationStats.getLanguageMaps(), "out/" + folder + "/language_maps.json");
        IOUtils.exportToJson(simulationStats.getLearningAbilityMaps(),
                "out/" + folder + "/learning_ability_maps.json");
    }

    public static void runPCommSimulations() {

        int nIters = 50000;
        int nSkipIterations = 15000;
        int worldSize = 20;
        double A = 0.05;
        double B = 5;
        double pMutation = 0.001;

        String folder = "Test1_pComm";
        SimulationPlots.setFolderName(folder);

        List<Double> pCommunicationValues = List.of(0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17);
        List<Double> avgSuccessRates = new ArrayList<>();
        List<Double> avgLearningAbilities = new ArrayList<>();

        Simulation simulation = new Simulation(nIters, worldSize, null, null, pMutation, A, B);

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

            SimulationPlots.plotTwoSeriesOverIterations(simulationStats.getAvgLearningAbilities(),
                simulationStats.getSuccessRates(), "Learning Ability and Success Rate Over Iterations, pComm = " + pComm, "Iteration",
                "Value", 0, 1, "Learning Ability", "Success Rate", 0.0, 1.2);

            IOUtils.exportToJson(avgLearningAbility, "out/" + folder + "/l_ab_pComm_"+pComm+".json");
            IOUtils.exportToJson(avgSuccessRate, "out/" + folder + "/s_rate_pComm_"+pComm+".json");
            IOUtils.saveWorld(simulation.getWorld(), "out/" + folder + "/world_after_pComm_"+pComm+".ser");
        }

        SimulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgLearningAbilities, "Learning Ability Over P_Comm", "P_Communication", "Learning Ability", "Learning Ability", null, null, true);
        SimulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgSuccessRates, "Success Rate Over P_Comm", "P_Communication", "Success Rate", "Success Rate", null, null, true);

        timer.stop("All simulations done");
    }
}