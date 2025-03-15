package org.example.entities;

import java.io.Serializable;
import java.util.*;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.strategies.agentInitializer.AgentInitializer;
import org.example.strategies.neighborPositions.NeighborPositionsStrategy;
import org.example.utils.Position;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class World implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    private final int rows;
    @Getter
    private final int cols;

    private final Map<Position, Agent> agentsGrid = new HashMap<>();
    private final VarConfig varConfig;
    private final StrategyConfig strategyConfig;

    private final NeighborPositionsStrategy neighborPositionsStrategy;
    private final AgentInitializer agentInitializer;

    public World(VarConfig varConfig, StrategyConfig strategyConfig) {
        this.varConfig = varConfig;
        this.rows = varConfig.L_ROWS() <= 0 ? varConfig.L() : varConfig.L_ROWS();
        this.cols = varConfig.L_COLS() <= 0 ? varConfig.L() : varConfig.L_COLS();
        this.strategyConfig = strategyConfig;
        this.neighborPositionsStrategy = strategyConfig.getNeighborPositionsStrategy();
        this.agentInitializer = strategyConfig.getAgentInitializer();
        init();
    }

    private void init() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Agent agent = this.agentInitializer.initAgent(varConfig, strategyConfig);
                addAgent(x, y, agent);
            }
        }
    }

    public void addAgent(int x, int y, Agent agent) {
        shouldBeEmpty(x, y);
        agent.setPosition(x, y);
        agentsGrid.put(new Position(x, y), agent);
    }

    public void killAgent(int x, int y) {
        shouldNotBeEmpty(x, y);
        agentsGrid.remove(new Position(x, y));
    }

    public List<Position> getNeighbourPositions(int x, int y) {
        return neighborPositionsStrategy.getNeighbourPositions(x, y, rows, cols);
    }

    public Agent getRandomNeighbour(int x, int y) {
        List<Position> aliveNeighbourPositions = new ArrayList<>();
        for (Position position : getNeighbourPositions(x, y)) {
            if (agentsGrid.containsKey(position)) {
                aliveNeighbourPositions.add(position);
            }
        }
        if (aliveNeighbourPositions.isEmpty()) {
            return null;
        }
        Position randomPosition = aliveNeighbourPositions.get(new Random().nextInt(aliveNeighbourPositions.size()));
        return agentsGrid.get(randomPosition);
    }

    public Position getRandomEmptyNeighbourPosition(int x, int y) {
        List<Position> emptyPositions = new ArrayList<>();
        for (Position position : getNeighbourPositions(x, y)) {
            if (!agentsGrid.containsKey(position)) {
                emptyPositions.add(position);
            }
        }
        if (emptyPositions.isEmpty()) {
            return null;
        }
        return emptyPositions.get(new Random().nextInt(emptyPositions.size()));
    }

    public Agent getRandomAgent() {
        Collection<Agent> agents = agentsGrid.values();
        return agents.stream().skip(new Random().nextInt(agents.size())).findFirst().get();
    }

    public void increaseAgentsAge() {
        for (Agent agent : agentsGrid.values()) {
            agent.increaseAge();
        }
    }

    public double getAvgKnowledge() {
        double totalKnowledge = 0;
        for (Agent agent : agentsGrid.values()) {
            totalKnowledge += agent.getKnowledge();
        }
        return totalKnowledge / agentsGrid.size();
    }

    public double getWeighedAvgKnowledge(double otherAgentsKnowledgeCoefficient, int x, int y) {
        double totalKnowledge = 0;
        for (Agent agent : agentsGrid.values()) {
            totalKnowledge += agent.getKnowledge() * (agent.isAtPostion(x, y) ? 1 : otherAgentsKnowledgeCoefficient);
        }
        return totalKnowledge / agentsGrid.size();
    }

    public Stats getStats() {
        double totalLearningAbility = 0;
        double totalLearningAbilityAtBirth = 0;
        Set<String> languages = new HashSet<>();
        int totalAge = 0;
        double totalKnowledge = 0;

        for (Agent agent : agentsGrid.values()) {
            totalLearningAbility += agent.getLearningAbility();
            totalLearningAbilityAtBirth += agent.getLearningAbilityAtBirth();
            
            if (!agent.getLexicon().isEmpty()) {
                languages.add(agent.getLexicon().getTopWord());
            }            
            totalAge += agent.getAge();
            totalKnowledge += agent.getKnowledge();
        }

        double avgLearningAbility = totalLearningAbility / agentsGrid.size();
        double avgLearningAbilityAtBirth = totalLearningAbilityAtBirth / agentsGrid.size();
        int languagesNumber = languages.size();
        double avgAge = (double) totalAge / agentsGrid.size();
        double avgKnowledge = totalKnowledge / agentsGrid.size();

        return new Stats(avgLearningAbilityAtBirth, avgLearningAbility, languagesNumber, avgAge, avgKnowledge);
    }

    public Agent getAgentAt(int x, int y) {
        return agentsGrid.get(new Position(x, y));
    }

    public boolean hasAgents() {
        return !agentsGrid.isEmpty();
    }

    public List<Agent> getAgents() {
        return new ArrayList<>(agentsGrid.values());
    }

    public int getNAgentsAlive() {
        return agentsGrid.size();
    }

    public int getCellsNumber() {
        return rows * cols;
    }

    private void shouldBeEmpty(int x, int y) {
        if (agentsGrid.containsKey(new Position(x, y))) {
            throw new IllegalArgumentException("Position (" + x + ", " + y + ") is already occupied");
        }
    }

    private void shouldNotBeEmpty(int x, int y) {
        if (!agentsGrid.containsKey(new Position(x, y))) {
            throw new IllegalArgumentException("Position (" + x + ", " + y + ") is empty");
        }
    }

    @AllArgsConstructor
    public static class Stats {
        @Getter
        private final double avgLearningAbilityAtBirth;
        @Getter
        private final double avgLearningAbility;
        @Getter
        private final int languagesNumber;
        @Getter
        private final double avgAge;
        @Getter
        private final double avgKnowledge;
    }

    public static World mergeWorlds(World world1, World world2, VarConfig varConfig, StrategyConfig strategyConfig) {
        World mergedWorld = new World(varConfig, strategyConfig);
        for (Agent agent : world1.getAgents()) {
            mergedWorld.addAgent(agent.getX(), agent.getY(), agent);
        }
        for (Agent agent : world2.getAgents()) {
            mergedWorld.addAgent(agent.getX() + world1.getCols(), agent.getY(), agent);
        }
        return mergedWorld;
    }
}