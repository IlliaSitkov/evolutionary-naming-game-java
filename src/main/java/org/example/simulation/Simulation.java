package org.example.simulation;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.entities.Agent;
import org.example.entities.World;
import org.example.stats.IterationStats;
import org.example.stats.SimulationStats;
import org.example.strategies.evolution.EvolutionStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.utils.Position;
import org.example.utils.Timer;

import lombok.Getter;
import lombok.Setter;

public class Simulation {
    @Getter
    private final World world;
    @Setter
    private PCommunicationStrategy pCommunicationStrategy;
    @Setter
    @Getter
    private SimulationStats simulationStats;
    private IterationStats iterationStats;
    @Getter
    private final VarConfig varConfig;
    @Getter
    private final StrategyConfig strategyConfig;

    private final EvolutionStrategy evolutionStrategy;

    public Simulation(SimulationStats simulationStats, VarConfig varConfig, StrategyConfig strategyConfig) {
        this(new World(varConfig, strategyConfig), simulationStats, varConfig, strategyConfig);
    }

    public Simulation(World world, SimulationStats simulationStats, VarConfig varConfig, StrategyConfig strategyConfig) {
        this.world = world;
        this.pCommunicationStrategy = strategyConfig.getPCommunicationStrategy();
        this.simulationStats = simulationStats;
        this.evolutionStrategy = strategyConfig.getEvolutionStrategy();
        this.varConfig = varConfig;
        this.strategyConfig = strategyConfig;
    }

    public void start() {
        simulationStats.recordBeforeEvolution(world, pCommunicationStrategy.getPCommunication(0));
        for (int iteration = 0; iteration < varConfig.T() && world.hasAgents(); iteration++) {
            iterationStats = new IterationStats(iteration);

            Timer timer = new Timer();
            timer.start();

            evolveWorld(iteration);

            timer.stop("Iteration " + iteration);

            simulationStats.recordIteration(iterationStats);
            simulationStats.recordAfterIteration(world, iteration, pCommunicationStrategy.getPCommunication(iteration));
        }
    }

    private void evolveWorld(int iteration) {
        for (int i = 0; i < world.getCellsNumber() && world.hasAgents(); i++) {
            updateAgent(iteration);
        }
        world.increaseAgentsAge();
    }

    private void updateAgent(int iteration) {
        Agent speaker = world.getRandomAgent();
        boolean[] steps = evolutionStrategy.determineEvolutionSteps(pCommunicationStrategy.getPCommunication(iteration));
        boolean shouldCommunicate = steps[0];
        boolean shouldUpdatePopulation = steps[1];
        if (shouldCommunicate) {
            Agent listener = world.getRandomNeighbour(speaker.getX(), speaker.getY());
            if (listener == null) {
                populationUpdate(speaker);
            } else {
                boolean isCommunicationSuccessful = communicationUpdate(speaker, listener);
                iterationStats.trackCommunicationResult(isCommunicationSuccessful);
            }
        }
        if (shouldUpdatePopulation) {
            populationUpdate(speaker);
        }
    }

    private boolean communicationUpdate(Agent speaker, Agent listener) {
        String word = speaker.speak();
        if (listener.knowsWord(word)) {
            speaker.recordSuccessfulCommunication();
            speaker.reinforceWord(word);
            listener.reinforceWord(word);
            return true;
        }

        speaker.diminishWord(word);
        listener.learnWord(word);
        return false;
    }

    private void populationUpdate(Agent agent) {
        double[] survivalResult = agent.survives(world);
        iterationStats.trackPSurv(survivalResult[1]);
        if (survivalResult[0] == 1) {
            Position emptyNeighbourPosition = world.getRandomEmptyNeighbourPosition(agent.getX(), agent.getY());
            if (emptyNeighbourPosition != null) {
                reproduce(agent, emptyNeighbourPosition);
                iterationStats.trackAgentBorn();
            }
        } else {
            world.killAgent(agent.getX(), agent.getY());
            iterationStats.trackAgentKilled();
        }
    }

    private void reproduce(Agent agent, Position position) {
        Agent childAgent = agent.reproduce(varConfig.P_MUT());
        world.addAgent(position.x(), position.y(), childAgent);
    }
}