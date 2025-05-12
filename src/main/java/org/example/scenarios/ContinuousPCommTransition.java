package org.example.scenarios;

import java.util.List;
import java.util.Map;

import org.example.StrategyConfig;
import org.example.VarConfig;
import org.example.VarConfig.ConfigKey;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.agentInitializer.LimitedLAbAgentInitializer;
import org.example.strategies.agentInitializer.RandomAgentInitializer;
import org.example.strategies.evolution.FullEvolutionStrategy;
import org.example.strategies.evolution.ProbabilisticEvolutionStrategy;
import org.example.strategies.learningAbilityAging.ConstantDecreaseLAbAgingStrategy;
import org.example.strategies.learningAbilityAging.ConstantLAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.MoloneyRandomLAbInheritanceStrategy;
import org.example.strategies.learningAbilityInheritance.MutatedLAbInheritanceStrategy;
import org.example.strategies.learningAbilityInheritance.RandomLAbInheritanceStrategy;
import org.example.strategies.neighborPositions.Neighbor8PositionsStrategy;
import org.example.strategies.neighborPositions.NeighborPositionsStrategy;
import org.example.strategies.pCommunication.ContinuousIncreasePCommunicationStrategy;
import org.example.strategies.pCommunication.InterpolatedPCommunicationStrategy;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.strategies.pSurvival.SuccessRatePSurvivalStrategy;
import org.example.strategies.wordAcquisition.LAbWordAquisitionStrategy;
import org.example.strategies.wordAcquisition.UnitWordAcquisitionStrategy;
import org.example.utils.RunUtils;

public class ContinuousPCommTransition {

        public static final String folder = "continuous_p_comm_transition";

        public static void original(int L, int N, double finalPComm, double A) {
                int nSteps = 80000;
                original(L, N, finalPComm, A, nSteps, nSteps);
        }

        public static void original(int L, int N, double finalPComm, double A, int nSteps, int nStepsSimulated) {
                original(L, N, finalPComm, A, nSteps, nStepsSimulated, new Neighbor8PositionsStrategy(), 0);
        }

        public static void original(int L, int N, double finalPComm, double A, int nSteps, int nStepsSimulated, NeighborPositionsStrategy neighborPositionsStrateg) {
                original(L, N, finalPComm, A, nSteps, nStepsSimulated, neighborPositionsStrateg, 0);
        }

        public static void original(int L, int N, double finalPComm, double A, int nSteps, int nStepsSimulated, int initAge) {
                original(L, N, finalPComm, A, nSteps, nStepsSimulated, new Neighbor8PositionsStrategy(), initAge);
        }

        public static void original(int L, int N, double finalPComm, double A, int nSteps, int nStepsSimulated, NeighborPositionsStrategy neighborPositionsStrategy, int initAge) {
                String folder = RunUtils.makePath(ContinuousPCommTransition.folder, "/original/L", L, "N", N);
                VarConfig varConfig = new VarConfig(Map.of(
                                ConfigKey.L, L,
                                ConfigKey.A, A,
                                ConfigKey.T, nSteps,
                                ConfigKey.INIT_AGE, initAge,
                                ConfigKey.N, N));
                StrategyConfig strategyConfig = new StrategyConfig(
                                new InterpolatedPCommunicationStrategy(0.1, finalPComm, nStepsSimulated),
                                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                                new RandomLAbInheritanceStrategy(),
                                new ConstantLAbAgingStrategy(),
                                neighborPositionsStrategy,
                                new UnitWordAcquisitionStrategy(),
                                new ProbabilisticEvolutionStrategy(),
                                new LimitedLAbAgentInitializer(0.1));

                SimulationStats simulationStats = new SimulationStats(
                                List.of(varConfig.T() - 1),
                                List.of(0.1, 0.12, 0.13, 0.15, 0.16, 0.17, 0.18, 0.2, 0.22, 0.25, 0.26, 0.28, 0.3, 0.35,
                                                0.4, 0.49, 0.5));

                Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

                RunUtils.runSimulation(simulation, folder);
        }

        public static void originalMoloney(int L, int N, double finalPComm, double A,  int nSteps, int nStepsSimulated) {
                String folder = RunUtils.makePath(ContinuousPCommTransition.folder, "/original_moloney/L", L, "N", N);
                VarConfig varConfig = new VarConfig(Map.of(
                                ConfigKey.L, L,
                                ConfigKey.A, A,
                                ConfigKey.T, nSteps,
                                ConfigKey.N, N,
                                ConfigKey.REPR_LIPOWSKA, 0));
                StrategyConfig strategyConfig = new StrategyConfig(
                                new ContinuousIncreasePCommunicationStrategy(0.1, finalPComm, nStepsSimulated),
                                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                                new MoloneyRandomLAbInheritanceStrategy(),
                                new ConstantLAbAgingStrategy(),
                                new Neighbor8PositionsStrategy(),
                                new UnitWordAcquisitionStrategy(),
                                new ProbabilisticEvolutionStrategy()
                                );

                SimulationStats simulationStats = new SimulationStats(
                                List.of(varConfig.T() - 1),
                                List.of(0.1, 0.12, 0.13, 0.15, 0.18, 0.22, 0.25, 0.28, 0.4, 0.49, 0.5));

                Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

                RunUtils.runSimulation(simulation, folder);
        }


        public static void mutatedLAb(int L, int N, double finalPComm, double A, int nSteps, int nStepsSimulated, double stdDev) {
                String folder = RunUtils.makePath(ContinuousPCommTransition.folder, "/mutated_l_ab/L", L, "N", N);
                VarConfig varConfig = new VarConfig(Map.of(
                                ConfigKey.L, L,
                                ConfigKey.A, A,
                                ConfigKey.T, nSteps,
                                ConfigKey.N, N));
                StrategyConfig strategyConfig = new StrategyConfig(
                                new InterpolatedPCommunicationStrategy(0.1, finalPComm, nStepsSimulated),
                                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                                new MutatedLAbInheritanceStrategy(stdDev),
                                new ConstantLAbAgingStrategy(),
                                new Neighbor8PositionsStrategy(),
                                new UnitWordAcquisitionStrategy(),
                                new ProbabilisticEvolutionStrategy(),
                                new LimitedLAbAgentInitializer(0.1));

                SimulationStats simulationStats = new SimulationStats(
                                List.of(varConfig.T() - 1),
                                List.of(0.1, 0.11, 0.12, 0.13, 0.15, 0.16, 0.17, 0.18,
                                0.19, 0.2, 0.21, 0.22, 0.23, 0.25, 0.24, 0.25, 0.26,
                                0.27, 0.28, 0.29, 0.3, 0.31, 0.35, 0.36, 0.37,
                                0.38, 0.39, 0.4, 0.41, 0.42, 0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5));

                Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

                RunUtils.runSimulation(simulation, folder);
        }



        public static void agedLAb(int L, int N, double finalPComm, double A, int nSteps, int nStepsSimulated, int startAge) {
                String folder = RunUtils.makePath(ContinuousPCommTransition.folder, "/aged_l_ab/L", L, "N", N);
                VarConfig varConfig = new VarConfig(Map.of(
                                ConfigKey.L, L,
                                ConfigKey.A, A,
                                ConfigKey.T, nSteps,
                                ConfigKey.N, N));
                StrategyConfig strategyConfig = new StrategyConfig(
                                new InterpolatedPCommunicationStrategy(0.1, finalPComm, nStepsSimulated),
                                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                                new RandomLAbInheritanceStrategy(),
                                new ConstantDecreaseLAbAgingStrategy(startAge),
                                new Neighbor8PositionsStrategy(),
                                new UnitWordAcquisitionStrategy(),
                                new ProbabilisticEvolutionStrategy(),
                                new LimitedLAbAgentInitializer(0.1));

                SimulationStats simulationStats = new SimulationStats(
                                List.of(varConfig.T() - 1),
                                List.of(0.1, 0.11, 0.12, 0.13, 0.15, 0.16, 0.17, 0.18,
                                0.19, 0.2, 0.21, 0.22, 0.23, 0.25, 0.24, 0.25, 0.26,
                                0.27, 0.28, 0.29, 0.3, 0.31, 0.35, 0.36, 0.37,
                                0.38, 0.39, 0.4, 0.41, 0.42, 0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5));

                Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

                RunUtils.runSimulation(simulation, folder);
        }

        public static void moloneyPSurv411(int L, int N, double finalPComm, double A, int nSteps, int nStepsSimulated) {
                String folder = RunUtils.makePath(ContinuousPCommTransition.folder, "/moloney_4_1_1_real/L", L, "N", N);
                VarConfig varConfig = new VarConfig(Map.of(
                                ConfigKey.L, L,
                                ConfigKey.A, A,
                                ConfigKey.T, nSteps,
                                ConfigKey.REPR_LIPOWSKA, 0,
                                ConfigKey.N, N));
                StrategyConfig strategyConfig = new StrategyConfig(
                                new InterpolatedPCommunicationStrategy(0.1, finalPComm, nStepsSimulated),
                                new SuccessRatePSurvivalStrategy(varConfig.A(), varConfig.C()),
                                new MoloneyRandomLAbInheritanceStrategy(),
                                new ConstantLAbAgingStrategy(),
                                new Neighbor8PositionsStrategy(),
                                new UnitWordAcquisitionStrategy(),
                                new ProbabilisticEvolutionStrategy(),
                                new LimitedLAbAgentInitializer(0.1));

                SimulationStats simulationStats = new SimulationStats(
                                List.of(varConfig.T() - 1),
                                List.of(0.1, 0.11, 0.12, 0.13, 0.15, 0.16, 0.17, 0.18,
                                0.19, 0.2, 0.21, 0.22, 0.23, 0.25, 0.24, 0.25, 0.26,
                                0.27, 0.28, 0.29, 0.3, 0.31, 0.35, 0.36, 0.37,
                                0.38, 0.39, 0.4, 0.41, 0.42, 0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5));

                Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

                RunUtils.runSimulation(simulation, folder);
        }

        public static void newPSurv(int L, int N, double finalPComm, double A, int nSteps, int nStepsSimulated, double otherKnwldgCoef) {
                String folder = RunUtils.makePath(ContinuousPCommTransition.folder, "/new_p_surv/L", L, "N", N);
                VarConfig varConfig = new VarConfig(Map.of(
                                ConfigKey.L, L,
                                ConfigKey.A, A,
                                ConfigKey.T, nSteps,
                                ConfigKey.N, N));
                StrategyConfig strategyConfig = new StrategyConfig(
                                new InterpolatedPCommunicationStrategy(0.1, finalPComm, nStepsSimulated),
                                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B(), otherKnwldgCoef),
                                new RandomLAbInheritanceStrategy(),
                                new ConstantLAbAgingStrategy(),
                                new Neighbor8PositionsStrategy(),
                                new UnitWordAcquisitionStrategy(),
                                new ProbabilisticEvolutionStrategy(),
                                new LimitedLAbAgentInitializer(0.1));

                SimulationStats simulationStats = new SimulationStats(
                                List.of(varConfig.T() - 1),
                                List.of(0.1, 0.11, 0.12, 0.13, 0.15, 0.16, 0.17, 0.18,
                                0.19, 0.2, 0.21, 0.22, 0.23, 0.25, 0.24, 0.25, 0.26,
                                0.27, 0.28, 0.29, 0.3, 0.31, 0.35, 0.36, 0.37,
                                0.38, 0.39, 0.4, 0.41, 0.42, 0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5));

                Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

                RunUtils.runSimulation(simulation, folder);
        }

        public static void wordAcquisition(int L, int N, double finalPComm, double A, int nSteps, int nStepsSimulated, Double inventedLAb, Double learntLAb, Double inheritedLAb, boolean isMoloney, double stdDev) {
                String folderPrefix = isMoloney ? "moloney_" : "";
                String folder = RunUtils.makePath(ContinuousPCommTransition.folder, "/", folderPrefix + "word_acquisition", "/", "L", L, "N", N);
                VarConfig varConfig = new VarConfig(Map.of(
                                ConfigKey.L, L,
                                ConfigKey.A, A,
                                ConfigKey.T, nSteps,
                                ConfigKey.REPR_LIPOWSKA, isMoloney ? 0 : 1,
                                ConfigKey.N, N));
                StrategyConfig strategyConfig = new StrategyConfig(
                                new InterpolatedPCommunicationStrategy(0.1, finalPComm, nStepsSimulated),
                                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                                isMoloney ? new MoloneyRandomLAbInheritanceStrategy() : new MutatedLAbInheritanceStrategy(stdDev),
                                new ConstantLAbAgingStrategy(),
                                new Neighbor8PositionsStrategy(),
                                new LAbWordAquisitionStrategy(inventedLAb, learntLAb, inheritedLAb),
                                new ProbabilisticEvolutionStrategy(),
                                isMoloney ? new RandomAgentInitializer() : new LimitedLAbAgentInitializer(0.1)
                                );

                SimulationStats simulationStats = new SimulationStats(
                                List.of(varConfig.T() - 1),
                                List.of(0.1, 0.11, 0.12, 0.13, 0.15, 0.16, 0.17, 0.18,
                                0.19, 0.2, 0.21, 0.22, 0.23, 0.25, 0.24, 0.25, 0.26,
                                0.27, 0.28, 0.29, 0.3, 0.31, 0.35, 0.36, 0.37,
                                0.38, 0.39, 0.4, 0.41, 0.42, 0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5));

                Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

                RunUtils.runSimulation(simulation, folder);
        }

        public static void wordAcquisitionAgedLAb(int L, int N, double finalPComm, double A, int nSteps, int nStepsSimulated, Double inventedLAb, Double learntLAb, Double inheritedLAb, double stdDev, int startAge) {
                wordAcquisitionAgedLAb(null, L, N, finalPComm, A, nSteps, nStepsSimulated, inventedLAb, learntLAb, inheritedLAb, stdDev, startAge);
        }

        public static void wordAcquisitionAgedLAb(String tag, int L, int N, double finalPComm, double A, int nSteps, int nStepsSimulated, Double inventedLAb, Double learntLAb, Double inheritedLAb, double stdDev, int startAge) {
                String folder = RunUtils.makePath(ContinuousPCommTransition.folder, "/", "word_acquisition_aged_l_ab_4a", "/", "L", L, "N", N);
                folder +=  tag == null ? "" : "/" + tag;

                VarConfig varConfig = new VarConfig(Map.of(
                                ConfigKey.L, L,
                                ConfigKey.A, A,
                                ConfigKey.T, nSteps,
                                ConfigKey.N, N));
                StrategyConfig strategyConfig = new StrategyConfig(
                                new InterpolatedPCommunicationStrategy(0.1, finalPComm, nStepsSimulated),
                                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                                new MutatedLAbInheritanceStrategy(stdDev),
                                new ConstantDecreaseLAbAgingStrategy(startAge),
                                new Neighbor8PositionsStrategy(),
                                new LAbWordAquisitionStrategy(inventedLAb, learntLAb, inheritedLAb),
                                new ProbabilisticEvolutionStrategy(),
                                new LimitedLAbAgentInitializer(0.1)
                                );

                SimulationStats simulationStats = new SimulationStats(
                                List.of(varConfig.T() - 1),
                                List.of(0.1, 0.11, 0.12, 0.13, 0.15, 0.16, 0.17, 0.18,
                                0.19, 0.2, 0.21, 0.22, 0.23, 0.25, 0.24, 0.25, 0.26,
                                0.27, 0.28, 0.29, 0.3, 0.31, 0.35, 0.36, 0.37,
                                0.38, 0.39, 0.4, 0.41, 0.42, 0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5));

                Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

                RunUtils.runSimulation(simulation, folder);
        }


        public static void wordAcquisitionNewPSurv(String tag, int L, int N, double finalPComm, double A, int nSteps, int nStepsSimulated, Double inventedLAb, Double learntLAb, Double inheritedLAb, double otherKnwldgCoef, double stdDev) {
                String folder = RunUtils.makePath(ContinuousPCommTransition.folder, "/", "word_acquisition_new_p_surv_4b", "/", "L", L, "N", N);
                folder +=  tag == null ? "" : "/" + tag;

                VarConfig varConfig = new VarConfig(Map.of(
                                ConfigKey.L, L,
                                ConfigKey.A, A,
                                ConfigKey.T, nSteps,
                                ConfigKey.N, N));
                StrategyConfig strategyConfig = new StrategyConfig(
                                new InterpolatedPCommunicationStrategy(0.1, finalPComm, nStepsSimulated),
                                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B(), otherKnwldgCoef),
                                new MutatedLAbInheritanceStrategy(stdDev),
                                new ConstantLAbAgingStrategy(),
                                new Neighbor8PositionsStrategy(),
                                new LAbWordAquisitionStrategy(inventedLAb, learntLAb, inheritedLAb),
                                new ProbabilisticEvolutionStrategy(),
                                new LimitedLAbAgentInitializer(0.1));

                SimulationStats simulationStats = new SimulationStats(
                                List.of(varConfig.T() - 1),
                                List.of(0.1, 0.11, 0.12, 0.13, 0.15, 0.16, 0.17, 0.18,
                                0.19, 0.2, 0.21, 0.22, 0.23, 0.25, 0.24, 0.25, 0.26,
                                0.27, 0.28, 0.29, 0.3, 0.31, 0.35, 0.36, 0.37,
                                0.38, 0.39, 0.4, 0.41, 0.42, 0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5));

                Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

                RunUtils.runSimulation(simulation, folder);
        }

        public static void wordAcquisitionNewPSurvAgedLAb(int L, int N, double A, double otherKnwldgCoef, int startAge, double stdDev) {
                wordAcquisitionNewPSurvAgedLAb(null, L, N, 0.5, A, 100000, 100000, null, null, null, otherKnwldgCoef, startAge,stdDev);
        }
        public static void wordAcquisitionNewPSurvAgedLAb(String tag, int L, int N, double A, double otherKnwldgCoef, int startAge, double stdDev) {
                wordAcquisitionNewPSurvAgedLAb(tag, L, N, 0.5, A, 100000, 100000, null, null, null, otherKnwldgCoef, startAge,stdDev);
        }

        public static void wordAcquisitionNewPSurvAgedLAb(String tag, int L, int N, double finalPComm, double A, int nSteps, int nStepsSimulated, Double inventedLAb, Double learntLAb, Double inheritedLAb, double otherKnwldgCoef, int startAge, double stdDev) {
                String folder = RunUtils.makePath(ContinuousPCommTransition.folder, "/word_acquisition_new_p_surv_aged_l_ab_4c_tagged/L", L, "N", N);
                folder +=  tag == null ? "" : "/" + tag;

                VarConfig varConfig = new VarConfig(Map.of(
                                ConfigKey.L, L,
                                ConfigKey.A, A,
                                ConfigKey.T, nSteps,
                                ConfigKey.N, N));
                StrategyConfig strategyConfig = new StrategyConfig(
                                new InterpolatedPCommunicationStrategy(0.1, finalPComm, nStepsSimulated),
                                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B(), otherKnwldgCoef),
                                new MutatedLAbInheritanceStrategy(stdDev),
                                new ConstantDecreaseLAbAgingStrategy(startAge),
                                new Neighbor8PositionsStrategy(),
                                new LAbWordAquisitionStrategy(inventedLAb, learntLAb, inheritedLAb),
                                new ProbabilisticEvolutionStrategy(),
                                new LimitedLAbAgentInitializer(0.1));

                SimulationStats simulationStats = new SimulationStats(
                                List.of(varConfig.T() - 1),
                                List.of(0.1, 0.11, 0.12, 0.13, 0.15, 0.16, 0.17, 0.18,
                                0.19, 0.2, 0.21, 0.22, 0.23, 0.25, 0.24, 0.25, 0.26,
                                0.27, 0.28, 0.29, 0.3, 0.31, 0.35, 0.36, 0.37,
                                0.38, 0.39, 0.4, 0.41, 0.42, 0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5));

                Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

                RunUtils.runSimulation(simulation, folder);
        }

        public static void moloneyFullWorldEvolution(int L, int N, double A, int nSteps) {
                String folder = RunUtils.makePath(ContinuousPCommTransition.folder, "/moloney_full_evol_414/L", L, "N", N);
                VarConfig varConfig = new VarConfig(Map.of(
                                ConfigKey.L, L,
                                ConfigKey.A, A,
                                ConfigKey.T, nSteps,
                                ConfigKey.REPR_LIPOWSKA, 0,
                                ConfigKey.N, N));
                StrategyConfig strategyConfig = new StrategyConfig(
                                new InterpolatedPCommunicationStrategy(0.1, 0.5, nSteps),
                                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                                new MoloneyRandomLAbInheritanceStrategy(),
                                new ConstantLAbAgingStrategy(),
                                new Neighbor8PositionsStrategy(),
                                new UnitWordAcquisitionStrategy(),
                                new FullEvolutionStrategy()
                                );

                SimulationStats simulationStats = new SimulationStats(
                                List.of(varConfig.T() - 1),
                                List.of(0.1, 0.11, 0.12, 0.13, 0.15, 0.16, 0.17, 0.18,
                                0.19, 0.2, 0.21, 0.22, 0.23, 0.25, 0.24, 0.25, 0.26,
                                0.27, 0.28, 0.29, 0.3, 0.31, 0.35, 0.36, 0.37,
                                0.38, 0.39, 0.4, 0.41, 0.42, 0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5));

                Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

                RunUtils.runSimulation(simulation, folder);
        } 

}
