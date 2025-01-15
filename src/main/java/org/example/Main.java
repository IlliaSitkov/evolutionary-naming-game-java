package org.example;

import org.example.plotting.SimulationPlots;
import org.example.simulation.Simulation;
import org.example.strategies.pCommunication.ContinuousIncreasePCommunicationStrategy;
import org.example.utils.Timer;

public class Main {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.start();

        // Simulation simulation = new Simulation(1000, 40, new SingleStepPCommunicationStrategy(0.1, 8000, 0.98), 0.001, 0.005, 5);
        Simulation simulation = new Simulation(50000, 40, new ContinuousIncreasePCommunicationStrategy(0.1, 0.5, 50000), 0.001, 0.005, 5);
        SimulationPlots.plotWorld(simulation.getWorld(), "WorldMapBefore");
        simulation.start();

        timer.stop("Full simulation");

        SimulationPlots plots = new SimulationPlots(simulation.getSimulationStats());
        plots.plotSuccessRateLearningAbility();
        SimulationPlots.plotStat(simulation.getSimulationStats().getSuccessRates(), "Success Rate Over Iterations", "Success Rate", "Success Rate", 1, 0.0, 1.2);
        SimulationPlots.plotStat(simulation.getSimulationStats().getAvgLearningAbilities(), "Average Learning Ability Over Iterations", "Average Learning Ability", "Learning Ability", 0, 0.0, 1.2);
        SimulationPlots.plotStat(simulation.getSimulationStats().getLanguagesNumber(), "Languages Number Over Iterations", "Languages Number", "Languages Number", 0);
        SimulationPlots.plotStat(simulation.getSimulationStats().getCommunicationsNumber(), "Communications Number Over Iterations", "Communications Number", "Communications Number", 1);
        SimulationPlots.plotStat(simulation.getSimulationStats().getAvgAges(), "Average Age Over Iterations", "Average Age", "Age", 0);
        SimulationPlots.plotStat(simulation.getSimulationStats().getKilledAgentsNumber(), "Killed Agents Number Over Iterations", "Killed Agents Number", "Killed Agents Number", 1);
        SimulationPlots.plotStat(simulation.getSimulationStats().getAvgKnowledge(), "Average Knowledge Over Iterations", "Avg Knowledge", "Avg Knowledge", 0);
        SimulationPlots.plotWorld(simulation.getWorld(), "WorldMapAfter");


        timer.stop("After plotting");
    }
}