package org.example;

import org.example.strategies.learningAbilityAging.LAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.LearningAbilityInheritanceStrategy;
import org.example.strategies.neighborPositions.NeighborPositionsStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.strategies.pSurvival.PSurvivalStrategy;
import org.example.strategies.wordAcquisition.WordAcquisitionStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class StrategyConfig {

    @Getter
    private final PCommunicationStrategy pCommunicationStrategy;

    @Getter
    private final PSurvivalStrategy pSurvivalStrategy;

    @Getter
    private final LearningAbilityInheritanceStrategy learningAbilityInheritanceStrategy;

    @Getter
    private final LAbAgingStrategy learingAbilityAgingStrategy;

    @Getter
    private final NeighborPositionsStrategy neighborPositionsStrategy;

    @Getter
    private final WordAcquisitionStrategy wordAcquisitionStrategy;
    
}
