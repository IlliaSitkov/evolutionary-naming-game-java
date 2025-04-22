package org.example;

import java.io.Serializable;

import org.example.strategies.agentInitializer.AgentInitializer;
import org.example.strategies.agentInitializer.RandomAgentInitializer;
import org.example.strategies.evolution.EvolutionStrategy;
import org.example.strategies.learningAbilityAging.LAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.LAbInheritanceStrategy;
import org.example.strategies.neighborPositions.NeighborPositionsStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.strategies.pSurvival.PSurvivalStrategy;
import org.example.strategies.wordAcquisition.WordAcquisitionStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class StrategyConfig implements Serializable {

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
                "}";
    }

}
