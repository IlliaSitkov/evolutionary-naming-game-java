package org.example;

import java.io.Serializable;

import org.example.strategies.agentInitializer.AgentInitializer;
import org.example.strategies.agentInitializer.RandomAgentInitializer;
import org.example.strategies.continueSimulation.BaseContinueSimulationStrategy;
import org.example.strategies.continueSimulation.ContinueSimulationStrategy;
import org.example.strategies.evolution.EvolutionStrategy;
import org.example.strategies.learningAbilityAging.LAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.LAbInheritanceStrategy;
import org.example.strategies.neighborPositions.NeighborPositionsStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.strategies.pSurvival.PSurvivalStrategy;
import org.example.strategies.wordAcquisition.WordAcquisitionStrategy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StrategyConfig implements Serializable {

    public StrategyConfig(
            PCommunicationStrategy pCommunicationStrategy,
            PSurvivalStrategy pSurvivalStrategy,
            LAbInheritanceStrategy learningAbilityInheritanceStrategy,
            LAbAgingStrategy learingAbilityAgingStrategy,
            NeighborPositionsStrategy neighborPositionsStrategy,
            WordAcquisitionStrategy wordAcquisitionStrategy,
            EvolutionStrategy evolutionStrategy,
            AgentInitializer agentInitializer) {
        this.pCommunicationStrategy = pCommunicationStrategy;
        this.pSurvivalStrategy = pSurvivalStrategy;
        this.learningAbilityInheritanceStrategy = learningAbilityInheritanceStrategy;
        this.learingAbilityAgingStrategy = learingAbilityAgingStrategy;
        this.neighborPositionsStrategy = neighborPositionsStrategy;
        this.wordAcquisitionStrategy = wordAcquisitionStrategy;
        this.evolutionStrategy = evolutionStrategy;
        this.agentInitializer = agentInitializer;
    }

    public StrategyConfig(
            PCommunicationStrategy pCommunicationStrategy,
            PSurvivalStrategy pSurvivalStrategy,
            LAbInheritanceStrategy learningAbilityInheritanceStrategy,
            LAbAgingStrategy learingAbilityAgingStrategy,
            NeighborPositionsStrategy neighborPositionsStrategy,
            WordAcquisitionStrategy wordAcquisitionStrategy,
            EvolutionStrategy evolutionStrategy,
            ContinueSimulationStrategy continueSimulationStrategy) {
        this.pCommunicationStrategy = pCommunicationStrategy;
        this.pSurvivalStrategy = pSurvivalStrategy;
        this.learningAbilityInheritanceStrategy = learningAbilityInheritanceStrategy;
        this.learingAbilityAgingStrategy = learingAbilityAgingStrategy;
        this.neighborPositionsStrategy = neighborPositionsStrategy;
        this.wordAcquisitionStrategy = wordAcquisitionStrategy;
        this.evolutionStrategy = evolutionStrategy;
        this.continueSimulationStrategy = continueSimulationStrategy;
    }

    @Getter
    private final PCommunicationStrategy pCommunicationStrategy;

    @Getter
    private final PSurvivalStrategy pSurvivalStrategy;

    @Getter
    private final LAbInheritanceStrategy learningAbilityInheritanceStrategy;

    @Getter
    private final LAbAgingStrategy learingAbilityAgingStrategy;

    @Getter
    private final NeighborPositionsStrategy neighborPositionsStrategy;

    @Getter
    private final WordAcquisitionStrategy wordAcquisitionStrategy;

    @Getter
    private final EvolutionStrategy evolutionStrategy;

    @Getter
    private AgentInitializer agentInitializer = new RandomAgentInitializer();

    @Getter
    private ContinueSimulationStrategy continueSimulationStrategy = new BaseContinueSimulationStrategy();

    @Override
    public String toString() {
        return "StrategyConfig {\n" +
                "  pCommunicationStrategy=" + pCommunicationStrategy + ",\n" +
                "  pSurvivalStrategy=" + pSurvivalStrategy + ",\n" +
                "  learningAbilityInheritanceStrategy=" + learningAbilityInheritanceStrategy + ",\n" +
                "  learingAbilityAgingStrategy=" + learingAbilityAgingStrategy + ",\n" +
                "  neighborPositionsStrategy=" + neighborPositionsStrategy + ",\n" +
                "  wordAcquisitionStrategy=" + wordAcquisitionStrategy + ",\n" +
                "  evolutionStrategy=" + evolutionStrategy + ",\n" +
                "  agentInitializer=" + agentInitializer + "\n" +
                "  continueSimulationStrategy=" + continueSimulationStrategy + "\n" +
                "}";
    }

}
