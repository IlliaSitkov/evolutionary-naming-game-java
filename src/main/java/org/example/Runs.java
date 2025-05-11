package org.example;

import java.util.List;

import org.example.strategies.evolution.EvolutionStrategy;
import org.example.strategies.evolution.FullEvolutionStrategy;
import org.example.strategies.evolution.ProbabilisticEvolutionStrategy;
import org.example.strategies.learningAbilityAging.ConstantDecreaseLAbAgingStrategy;
import org.example.strategies.learningAbilityAging.ConstantLAbAgingStrategy;
import org.example.strategies.learningAbilityAging.LAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.LAbInheritanceStrategy;
import org.example.strategies.learningAbilityInheritance.MutatedLAbInheritanceStrategy;
import org.example.strategies.learningAbilityInheritance.RandomLAbInheritanceStrategy;
import org.example.strategies.neighborPositions.Neighbor4PositionsStrategy;
import org.example.strategies.neighborPositions.Neighbor8PositionsStrategy;
import org.example.strategies.neighborPositions.NeighborPositionsStrategy;
import org.example.strategies.pCommunication.ConstantPCommunicationStrategy;
import org.example.strategies.pCommunication.ContinuousIncreasePCommunicationStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.strategies.pCommunication.SingleStepPCommunicationStrategy;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.strategies.pSurvival.PSurvivalStrategy;
import org.example.strategies.pSurvival.SuccessRatePSurvivalStrategy;
import org.example.strategies.wordAcquisition.LAbWordAquisitionStrategy;
import org.example.strategies.wordAcquisition.UnitWordAcquisitionStrategy;
import org.example.strategies.wordAcquisition.WordAcquisitionStrategy;

public class Runs {

    private final List<EvolutionStrategy> evolutionStrategies = List.of(
        new FullEvolutionStrategy(),
        new ProbabilisticEvolutionStrategy()
    );

    private final List<LAbAgingStrategy> lAbAgingStrategies = List.of(
        new ConstantDecreaseLAbAgingStrategy(5),
        new ConstantLAbAgingStrategy()
    );

    private final List<LAbInheritanceStrategy> lAbInheritanceStrategies = List.of(
        new RandomLAbInheritanceStrategy(),
        new MutatedLAbInheritanceStrategy(0.1)
    );

    private final List<NeighborPositionsStrategy> neighborPositionsStrategies = List.of(
        new Neighbor4PositionsStrategy(false),
        new Neighbor8PositionsStrategy()
    );

    private final List<PCommunicationStrategy> pCommunicationStrategies = List.of(
        new ConstantPCommunicationStrategy(0.5),
        new ContinuousIncreasePCommunicationStrategy(0.1, 0.5, 5000),
        new SingleStepPCommunicationStrategy(0.1, 80000, 0.98)
    );

    private final List<PSurvivalStrategy> pSurvivalStrategies = List.of(
        new AvgKnowledgePSurvivalStrategy(0.05, 5),
        new AvgKnowledgePSurvivalStrategy(0.05, 5, 0.5),
        new SuccessRatePSurvivalStrategy(0.05, 0.005)
    );

    private final List<WordAcquisitionStrategy> wordAcquisitionStrategies = List.of(
        // new LAbWordAquisitionStrategy(),
        new UnitWordAcquisitionStrategy()
    );

    public static void main(String[] args) {
    }
}
