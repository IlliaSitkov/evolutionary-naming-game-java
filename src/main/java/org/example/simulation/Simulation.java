package org.example.simulation;

import java.util.Random;

import org.example.entities.Agent;
import org.example.entities.World;
import org.example.stats.IterationStats;
import org.example.stats.SimulationStats;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.utils.Position;
import org.example.utils.Timer;

import lombok.Getter;

public class Simulation {
    @Getter
    private final World world;
    private final int nIters;
    private final PCommunicationStrategy pCommunicationStrategy;
    private final double pMutation;
    private final double A;
    private final double B;
    @Getter
    private final SimulationStats simulationStats;
    private IterationStats iterationStats;

    public Simulation(int nIters, int worldSize, PCommunicationStrategy pCommunicationStrategy, double pMutation, double A, double B) {
        this.world = new World(worldSize);
        this.nIters = nIters;
        this.pCommunicationStrategy = pCommunicationStrategy;
        this.pMutation = pMutation;
        this.A = A;
        this.B = B;
        this.simulationStats = new SimulationStats();
    }

    public void start() {
        simulationStats.recordBeforeEvolution(world);
        for (int iteration = 0; iteration < nIters && world.hasAgents(); iteration++) {
            iterationStats = new IterationStats();

            Timer timer = new Timer();
            timer.start();

            evolveWorld(iteration);

            timer.stop("Iteration " + iteration);

            simulationStats.recordIteration(iterationStats);
            simulationStats.recordAfterIteration(world);
        }
    }

    private void evolveWorld(int iteration) {
        for (int i = 0; i < world.getSize() * world.getSize() && world.hasAgents(); i++) {
            updateAgent(iteration);
        }
        world.increaseAgentsAge();
    }

    private void updateAgent(int iteration) {
        Agent speaker = world.getRandomAgent();
        double rCommunication = new Random().nextDouble();
        if (rCommunication < pCommunicationStrategy.getPCommunication(iteration)) {
            Agent listener = world.getRandomNeighbour(speaker.getX(), speaker.getY());
            if (listener == null) {
                populationUpdate(speaker);
            } else {
                boolean isCommunicationSuccessful = communicationUpdate(speaker, listener);
                iterationStats.trackSuccessRate(isCommunicationSuccessful);
            }
        } else {
            populationUpdate(speaker);
        }
    }

    private boolean communicationUpdate(Agent speaker, Agent listener) {
        String word = speaker.speak();
        if (listener.knowsWord(word)) {
            speaker.reinforceWord(word);
            listener.reinforceWord(word);
            return true;
        }

        speaker.diminishWord(word);
        listener.learnWord(word);
        return false;
    }

    private void populationUpdate(Agent agent) {
        if (agent.survives(world.getAvgKnowledge(), A, B)) {
            Position emptyNeighbourPosition = world.getRandomEmptyNeighbourPosition(agent.getX(), agent.getY());
            if (emptyNeighbourPosition != null) {
                reproduce(agent, emptyNeighbourPosition);
            }
        } else {
            world.killAgent(agent.getX(), agent.getY());
            iterationStats.trackKilledAgent();
        }
    }

    private void reproduce(Agent agent, Position position) {
        Agent childAgent = agent.reproduce(pMutation);
        world.addAgent(position.x(), position.y(), childAgent);
    }
}