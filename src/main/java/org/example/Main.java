package org.example;

import org.example.export.ExportUtils;
import org.example.plotting.SimulationPlots;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.pCommunication.ContinuousIncreasePCommunicationStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.utils.Timer;

public class Main {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.start();

        int nIters = 50;
        int worldSize = 3;
        PCommunicationStrategy strategy = new ContinuousIncreasePCommunicationStrategy(0.1, 0.5, nIters);

        // Simulation simulation = new Simulation(1000, 40, new SingleStepPCommunicationStrategy(0.1, 8000, 0.98), 0.001, 0.005, 5);

        Simulation simulation = new Simulation(nIters, worldSize, strategy, 0.001, 0.005, 5);

        SimulationPlots.setFolderName("Test");
        SimulationPlots.plotWorldLanguages(simulation.getWorld(), "WorldMap_Languages_Before");
        SimulationPlots.plotWorldLearningAbilities(simulation.getWorld(), "WorldMap_LearningAbilities_Before", 0.01);

        simulation.start();

        timer.stop("Full simulation");

        SimulationPlots.plotTwoSeriesOverIterations(simulation.getSimulationStats().getAvgLearningAbilities(), simulation.getSimulationStats().getSuccessRates(), "Learning Ability and Success Rate Over Iterations", "Iteration", "Value", 0, 1, "Learning Ability", "Success Rate", 0.0, 1.2);
        SimulationPlots.plotStat(simulation.getSimulationStats().getSuccessRates(), "Success Rate Over Iterations", "Success Rate", "Success Rate", 1, 0.0, 1.2);
        SimulationPlots.plotStat(simulation.getSimulationStats().getAvgLearningAbilities(), "Average Learning Ability Over Iterations", "Average Learning Ability", "Learning Ability", 0, 0.0, 1.2);
        SimulationPlots.plotStat(simulation.getSimulationStats().getLanguagesNumber(), "Languages Number Over Iterations", "Languages Number", "Languages Number", 0);
        SimulationPlots.plotStat(simulation.getSimulationStats().getCommunicationsNumber(), "Communications Number Over Iterations", "Communications Number", "Communications Number", 1);
        SimulationPlots.plotStat(simulation.getSimulationStats().getAvgAges(), "Average Age Over Iterations", "Average Age", "Age", 0);
        SimulationPlots.plotStat(simulation.getSimulationStats().getKilledAgentsNumber(), "Killed Agents Number Over Iterations", "Killed Agents Number", "Killed Agents Number", 1);
        SimulationPlots.plotStat(simulation.getSimulationStats().getAvgKnowledge(), "Average Knowledge Over Iterations", "Avg Knowledge", "Avg Knowledge", 0);
        SimulationPlots.plotSeriesAsDependentOnAnother(SimulationStats.getPCommunicationOverIterations(strategy, nIters), simulation.getSimulationStats().getSuccessRates(), "Success rate over P_Comm", "P_Communication", "Success rate", "Success rate", 0.0, 1.2);
        SimulationPlots.plotSeriesAsDependentOnAnother(SimulationStats.getPCommunicationOverIterations(strategy, nIters), simulation.getSimulationStats().getAvgLearningAbilities(), "Learning ability over P_Comm", "P_Communication", "Learning ability", "Learning ability", 0.0, 1.2);
        
        SimulationPlots.plotSeriesAsDependentOnAnother(
            simulation.getSimulationStats().getAvgLearningAbilities(),
            simulation.getSimulationStats().getAvgKnowledge(),
            "Knowledge Over Learning ability",
            "Learning ability",
            "Knowledge",
            "Knowledge",
            null, null);

        SimulationPlots.plotSeriesAsDependentOnAnother(
            simulation.getSimulationStats().getAvgKnowledge(),
            simulation.getSimulationStats().getSuccessRates(),
            "Success Rate Over Knowledge",
            "Knowledge",
            "Success Rate",
            "Success Rate",
            null, null);

        SimulationPlots.plotWorldLanguages(simulation.getWorld(), "WorldMap_Languages_After");
        SimulationPlots.plotWorldLearningAbilities(simulation.getWorld(), "WorldMap_LearningAbilities_After", 0.01);


        timer.stop("After plotting");


        ExportUtils.exportLearningAbilities(simulation.getWorld(), "out/learningAbilities.json");
    }
}