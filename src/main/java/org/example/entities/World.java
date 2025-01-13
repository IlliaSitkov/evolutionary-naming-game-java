package org.example.entities;

import java.util.*;
import java.util.List;

import org.example.Config;
import org.example.utils.Position;

import lombok.Getter;

public class World {
    @Getter
    private final int size;
    private final Map<Position, Agent> agentsGrid = new HashMap<>();
    private final List<Position> agentsPositions = new ArrayList<>();

    public World(int size) {
        this.size = size;
        init();
    }

    private void init() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Agent agent = initAgent();
                addAgent(x, y, agent);
            }
        }
    }

    public void addAgent(int x, int y, Agent agent) {
        shouldBeEmpty(x, y);
        agent.setPosition(x, y);
        Position position = new Position(x, y);
        agentsPositions.add(position);
        agentsGrid.put(position, agent);
    }

    public void killAgent(int x, int y) {
        shouldNotBeEmpty(x, y);
        Position position = new Position(x, y);
        agentsGrid.remove(position);
        agentsPositions.remove(position);
    }

    public List<Position> getNeighbourPositions(int x, int y) {
        List<Position> neighbourPositions = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int newX = x + i;
                int newY = y + j;
                if (newX >= 0 && newX < size && newY >= 0 && newY < size) {
                    neighbourPositions.add(new Position(newX, newY));
                }
            }
        }
        return neighbourPositions;
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

    private Agent initAgent() {
        Lexicon lexicon = new Lexicon(Config.N);
        lexicon.addWord(Lexicon.generateWord(Config.WORD_LENGTH), 1);
        return new Agent(new Random().nextDouble(), lexicon);
    }

    public Agent getRandomAgent() {
        Position randomPosition = agentsPositions.get(new Random().nextInt(agentsPositions.size()));
        return agentsGrid.get(randomPosition);
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

    public double getAvgLearningAbility() {
        double totalLearningAbility = 0;
        for (Agent agent : agentsGrid.values()) {
            totalLearningAbility += agent.getLearningAbility();
        }
        return totalLearningAbility / agentsGrid.size();
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
}