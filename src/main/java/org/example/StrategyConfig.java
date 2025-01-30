package org.example;

import java.io.Serializable;

import org.example.strategies.evolution.EvolutionStrategy;
import org.example.strategies.learningAbilityAging.LAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.LAbInheritanceStrategy;
import org.example.strategies.neighborPositions.NeighborPositionsStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.strategies.pSurvival.PSurvivalStrategy;
import org.example.strategies.wordAcquisition.WordAcquisitionStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
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
    
}
