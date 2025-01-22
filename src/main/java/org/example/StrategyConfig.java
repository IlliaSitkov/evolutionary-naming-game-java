package org.example;

import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.strategies.pSurvival.PSurvivalStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class StrategyConfig {

    @Getter
    private final PCommunicationStrategy pCommunicationStrategy;

    @Getter
    private final PSurvivalStrategy pSurvivalStrategy;


    @Override
    public String toString() {
        return "Strategies Config {\n" +
                "pCommunicationStrategy: " + pCommunicationStrategy +
                ",\npSurvivalStrategy: " + pSurvivalStrategy +
                "\n}";
    }
    
}
