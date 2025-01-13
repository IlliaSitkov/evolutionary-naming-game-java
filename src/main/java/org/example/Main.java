package org.example;

import org.example.plotting.SimulationPlots;
import org.example.simulation.Simulation;
import org.example.strategies.pCommunication.SingleStepPCommunicationStrategy;
import org.example.utils.Timer;

public class Main {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.start();

        Simulation simulation = new Simulation(50000, 40, new SingleStepPCommunicationStrategy(0.1, 8000, 0.98), 0.001, 0.005, 5);
        simulation.start();

        timer.stop("Full simulation");

        SimulationPlots plots = new SimulationPlots(simulation.getSimulationStats());
        plots.plotSuccessRateLearningAbility();

        timer.stop("After plotting");
    }
}