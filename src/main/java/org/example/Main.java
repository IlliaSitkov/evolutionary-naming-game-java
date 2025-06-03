package org.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.example.VarConfig.ConfigKey;
import org.example.export.IOUtils;
import org.example.plotting.SimulationPlots;
import org.example.scenarios.AbruptTransitionRuns;
import org.example.scenarios.ConstantPCommTransition;
import org.example.scenarios.ContinuousPCommTransition;
import org.example.scenarios.MultiWorld;
import org.example.scenarios.PCommDecrease;
import org.example.scenarios.PCommIncrease;
import org.example.simulation.Simulation;
import org.example.stats.SimulationStats;
import org.example.strategies.evolution.FullEvolutionStrategy;
import org.example.strategies.evolution.ProbabilisticEvolutionStrategy;
import org.example.strategies.learningAbilityAging.ConstantLAbAgingStrategy;
import org.example.strategies.learningAbilityInheritance.MutatedLAbInheritanceStrategy;
import org.example.strategies.learningAbilityInheritance.RandomLAbInheritanceStrategy;
import org.example.strategies.neighborPositions.Neighbor4PositionsStrategy;
import org.example.strategies.neighborPositions.Neighbor8PositionsStrategy;
import org.example.strategies.pCommunication.ConstantPCommunicationStrategy;
import org.example.strategies.pCommunication.ContinuousIncreasePCommunicationStrategy;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.example.strategies.pCommunication.SingleStepPCommunicationStrategy;
import org.example.strategies.pSurvival.AvgKnowledgePSurvivalStrategy;
import org.example.strategies.wordAcquisition.UnitWordAcquisitionStrategy;
import org.example.utils.RunUtils;
import org.example.utils.Timer;

import smile.validation.metric.AdjustedRandIndex;
import smile.validation.metric.RandIndex;

public class Main {
    public static void main(String[] args) {
        runSimulationsInParallel();
        // AbruptTransitionRuns.original("default", 40, 1,0.05);
        // MultiWorld.relocate("test", 20, 1, 0.005, 0.5, 0.5, 20000,100000, 0.95, 0.95);
        // AbruptTransitionRuns.original("formula_0.98_a0.5", 40, 1, 0.5);
        // ContinuousPCommTransition.original("A", 40, 1, 0.5, 0, 100000,200000);

        // AdjustedRandIndex ari = new AdjustedRandIndex();
        // RandIndex ri = new RandIndex();

        // int[] truth = new int[]{1, 2, 3};
        // int[] labels = new int[]{1,1, 0};
        // double ariScore = ari.score(truth, labels);
        // double riScore = ri.score(truth, labels);

        // System.out.println(ariScore);
        // System.out.println(riScore);

        // ConstantPCommTransition.run("mut_0.5", 20, 1, 0.0, 0.05, 10000, 0.5);
        // ConstantPCommTransition.run("mut_0.01", 20, 1, 0.0, 0.05, 50000, 0.5);
        // ConstantPCommTransition.run("mut_0.005", 20, 1, 0.0, 0.05, 50000, 0.5);

        // ConstantPCommTransition.run("test", 30, 1, 0.2, 0.05, 50000, 0);

        // ConstantPCommTransition.run("m_0/p_0", 30, 1, 0.0, 0.05, 50000, 0.0);

        // investigateA(20, 2000, 0.1, 0.97, 0.99, 0.01);
        // investigatePComm(20, 2000, 0.03, 0.05, 0.8, 0.99, 0.01);

        // investigateA(20, 2000, 0, 1, 0.5, 0.99, 0.05, 0.02, 2);

        // investigatePComm(20, 2000, 0, 1, 0.5, 0.99, 0.05, 0.02, 2);
}

    public static void runSimulationsInParallel() {
        ExecutorService executorService = Executors.newFixedThreadPool(16);

        List<Runnable> simulationTasks = List.of(
            // () -> ContinuousPCommTransition.original(40, 1, 0.5, 0.05, 100000, 100000),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.005, 100000, 100000, 0.01),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.005, 100000, 100000, 0.05),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.005, 100000, 100000, 5),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.005, 100000, 100000, 0.0)
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.05, 100000, 100000, 5),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.05, 200000, 200000, 0.05),
            // () -> ContinuousPCommTransition.original(40, 1, 0.98, 0.05, 200000, 200000)

            // якщо використовувати маленьку stddev, то цікаво, як довго буде досягатись висока здатність до навчання
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.005, 100000, 100000, 0.001)

            // () -> ContinuousPCommTransition.mutatedLAb(60, 1, 0.5, 0.05, 100000, 100000, 0.1),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.05, 200000, 200000, 0.05),
            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.05, 100000, 100000, 5)

            // () -> ContinuousPCommTransition.mutatedLAb(40, 1, 0.5, 0.05, 100000, 100000, 0.01)


            // () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, 1),
            // () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, 2)
            // () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, 3),
            // () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, 4),
            // () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, 5)

            // () -> PCommIncrease.mutatedLAb(40, 0.05, 1, 10000, 3000,  0.14, 0.01, 0.0),
            // () -> PCommIncrease.mutatedLAb(40, 0.05, 1, 10000, 3000,  0.14, 0.01, 5),
            // () -> PCommIncrease.mutatedLAb(40, 0.05, 1, 10000, 3000,  0.18, 0.08, 0.01)

            // () -> ContinuousPCommTransition.moloneyPSurv411(40, 1,0.5,0.005,100000,100000),
            // () -> ContinuousPCommTransition.moloneyPSurv411(40, 1,0.5,0.005,100000,100000),
            // () -> ContinuousPCommTransition.moloneyPSurv411(40, 1,0.5,0.005,100000,100000),
            // () -> ContinuousPCommTransition.moloneyPSurv411(40, 1,0.5,0.005,100000,100000),

            // () -> ContinuousPCommTransition.newPSurv(40, 1,0.5,0.005,100000,100000, 0.9),
            // () -> ContinuousPCommTransition.newPSurv(40, 1,0.5,0.005,100000,100000, 1.0)
            // () -> ContinuousPCommTransition.newPSurv(40, 1,0.5,0.005,100000,100000, 0.75),
            // () -> ContinuousPCommTransition.newPSurv(40, 1,0.5,0.005,200000,200000, 0.25),
            // () -> ContinuousPCommTransition.newPSurv(40, 1,0.5,0.005,100000,100000, 0),

            // () -> ContinuousPCommTransition.newPSurv(40, 1,0.5,0.05,100000,100000, 0.5),
            // () -> ContinuousPCommTransition.newPSurv(40, 1,0.5,0.05,100000,100000, 0.75),
            // () -> ContinuousPCommTransition.newPSurv(40, 1,0.5,0.005,100000,100000, 0.25),
            // () -> ContinuousPCommTransition.newPSurv(40, 1,0.5,0.005,100000,100000, 0.1),
            // () -> ContinuousPCommTransition.newPSurv(40, 1,0.5,0.005,100000,100000, 0.01)
            // () -> ContinuousPCommTransition.newPSurv(40, 1,0.5,0.05,100000,100000, 0)

            // () -> ContinuousPCommTransition.newPSurv(40, 1,0.5,0.005,100000,100000, 0.05)



            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, true, 0.0)
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 50000, 50000, 1.0, null, null, true, 0.0),
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 50000, 50000, 1.0, null, 1.0, true, 0.0)

            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, 1.0, 1.0, null, true, 0.0),
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, 1.0, null, 1.0, true, 0.0),
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, null, 1.0, 1.0, true, 0.0)

            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, false, 0.0),
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, false, 0.0)
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, false, 0.0),
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, false, 0.0)
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, false, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, false, 0.05),
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, false, 0.5),
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, false, 0.75),
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, false, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, false, 1),
            // () -> ContinuousPCommTransition.wordAcquisition(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, false, 5)

            ///////////////// Word Acquisition Aged LAb /////////////////
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 8),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb(30, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 8),

            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb(40, 10, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 8),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb(40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.1, 8),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.5, 8),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 3),

            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.5, 3),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 5, 0),
            
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb(40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.5, 3),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb(40, 10, 0.5, 0.05, 100000, 100000, null, null, null, 0.5, 3),

            // //
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb(40, 1000, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 8),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb(20, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 8),

            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb("A_lg", 40, 1, 0.5, 1, 100000, 100000, null, null, null, 0.1, 8),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb("A_lg", 40, 1, 0.5, 2, 100000, 100000, null, null, null, 0.1, 8),

            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb("A_lg_dev", 40, 1, 0.5, 1, 100000, 100000, null, null, null, 0.5, 8),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb("A_lg_dev", 40, 1, 0.5, 2, 100000, 100000, null, null, null, 0.5, 8),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb("A_lg_dev", 40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.5, 8)

            // 5.a.iii influence of stddev
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb("stddev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.8, 8),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb("stddev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 11),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb("stddev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.8, 11)


            //////////// Word Acquisition New P_Surv ////////////
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("base", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 0.1),

            // see A vs sm and lg stddev
        
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("stddev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 0.2),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("stddev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 0.5),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("stddev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 0.8),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("N_big", 40, 10, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("N_big", 40, 1000, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 0.1),
            
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("N_sm", 40, 10, 0.5, 0.005, 100000, 100000, null, null, null, 0.25, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("N_sm", 40, 1000, 0.5, 0.005, 100000, 100000, null, null, null, 0.25, 0.1),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(null, 20, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(null, 30, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 0.1),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("A_lg", 40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.9, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("A_lg", 40, 1, 0.5, 0.5, 100000, 100000, null, null, null, 0.9, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("A_lg", 40, 1, 0.5, 1, 100000, 100000, null, null, null, 0.9, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("A_lg", 40, 1, 0.5, 2, 100000, 100000, null, null, null, 0.9, 0.1),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("A_sm", 40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.25, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("A_sm", 40, 1, 0.5, 0.5, 100000, 100000, null, null, null, 0.25, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("A_sm", 40, 1, 0.5, 1, 100000, 100000, null, null, null, 0.25, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("A_sm", 40, 1, 0.5, 2, 100000, 100000, null, null, null, 0.25, 0.1)

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("alpha", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.0, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("alpha", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.05, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("alpha", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("alpha", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.25, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("alpha", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.5, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("alpha", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.75, 0.1)


            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("alpha_sm_dev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.0, 0.05),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("alpha_sm_dev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 0.05),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("alpha_sm_dev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.25, 0.05),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("alpha_lg_dev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.0, 0.5),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("alpha_lg_dev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 0.5),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("alpha_lg_dev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.25, 0.5)

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("base", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 0.05)

            // A_lg - small stddev + A_lg_stddev - large stddev
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("A_lg_stddev", 40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.9, 0.5),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("A_lg_stddev", 40, 1, 0.5, 0.5, 100000, 100000, null, null, null, 0.9, 0.5),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("A_lg_stddev", 40, 1, 0.5, 1, 100000, 100000, null, null, null, 0.9, 0.5),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("A_lg_stddev", 40, 1, 0.5, 2, 100000, 100000, null, null, null, 0.9, 0.5)


            ////////////////////////////////
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.75),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.1, 100000, 100000, null, null, null, 0.9),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.5),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.0),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.9),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 10, 0.5, 0.05, 100000, 100000, null, null, null, 0.9),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1000, 0.5, 0.05, 100000, 100000, null, null, null, 0.9),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(30, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(20, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9),


            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 10, 0.5, 0.005, 100000, 100000, null, null, null, 0.0),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1000, 0.5, 0.005, 100000, 100000, null, null, null, 0.0),
            
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.0),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.0),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.5, 100000, 100000, null, null, null, 0.0),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1000, 0.5, 0.005, 100000, 100000, 1.0, 1.0, 1.0, 0.0),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1000, 0.5, 0.005, 100000, 100000, null, null, null, 0.0),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 10, 0.5, 0.005, 100000, 100000, null, null, null, 0.9),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1000, 0.5, 0.005, 100000, 100000, null, null, null, 0.9),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.25),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.05),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.01),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.25),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.25),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.5, 100000, 100000, null, null, null, 0.25),
            
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.75, 100000, 100000, null, null, null, 0.9),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 1, 100000, 100000, null, null, null, 0.9),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 2, 100000, 100000, null, null, null, 0.9),

            // // зі збільшенням А і при зменшенні а як змігюється поведінка (п. b.iii)
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.75),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.5),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.25),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv(40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.1)

            //////// 3 strategies //////////
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(20, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(30, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 0.1),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.9, 8, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 1, 0.5, 0.75, 100000, 100000, null, null, null, 0.9, 8, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 1, 0.5, 1, 100000, 100000, null, null, null, 0.9, 8, 0.1),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.5, 8, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.25, 8, 0.1),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 10, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 1000, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 0.1),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 0.25),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 0.5),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 5),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 3, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 1, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb(40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 5, 0.1)


            /// ALL COMBINED //////////////////////////////////////////////////////
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("base", 40, 1, 0.005, 0.99, 15, 0.01),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("alpha_lg_age", 40, 1, 0.005, 0.95, 15, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("alpha_lg_age", 40, 1, 0.005, 0.5, 15, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("alpha_lg_age", 40, 1, 0.005, 0.25, 15, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("alpha_lg_age", 40, 1, 0.005, 0.1, 15, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("alpha_lg_age", 40, 1, 0.005, 0.0, 15, 0.01),
            
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("alpha_sm_age", 40, 1, 0.005, 0.99, 8, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("alpha_sm_age", 40, 1, 0.005, 0.95, 8, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("alpha_sm_age", 40, 1, 0.005, 0.5, 8, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("alpha_sm_age", 40, 1, 0.005, 0.25, 8, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("alpha_sm_age", 40, 1, 0.005, 0.1, 8, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("alpha_sm_age", 40, 1, 0.005, 0.0, 8, 0.01),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_lg_age", 40, 1, 0.005, 0.99, 15, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_lg_age", 40, 1, 0.005, 0.99, 15, 0.5),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_lg_age", 40, 1, 0.005, 0.99, 15, 0.8),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_lg_age", 40, 1, 0.005, 0.99, 15, 5),
            
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_sm_alpha", 40, 1, 0.005, 0.25, 15, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_sm_alpha", 40, 1, 0.005, 0.25, 15, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_sm_alpha", 40, 1, 0.005, 0.25, 15, 0.5),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_sm_alpha", 40, 1, 0.005, 0.25, 15, 0.8),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_sm_alpha", 40, 1, 0.005, 0.25, 15, 5),
            
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_sm_age", 40, 1, 0.005, 0.99, 8, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_sm_age", 40, 1, 0.005, 0.99, 8, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_sm_age", 40, 1, 0.005, 0.99, 8, 0.5),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_sm_age", 40, 1, 0.005, 0.99, 8, 0.8),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("stddev_sm_age", 40, 1, 0.005, 0.99, 8, 5),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("age", 40, 1, 0.005, 0.99, 8, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("age", 40, 1, 0.005, 0.99, 5, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("age", 40, 1, 0.005, 0.99, 1, 0.01),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("A_lg_age", 40, 1, 0.05, 0.99, 15, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("A_lg_age", 40, 1, 0.5, 0.99, 15, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("A_lg_age", 40, 1, 2, 0.99, 15, 0.01),
            
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("A_sm_alpha", 40, 1, 0.005, 0.25, 15, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("A_sm_alpha", 40, 1, 0.05, 0.25, 15, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("A_sm_alpha", 40, 1, 0.5, 0.25, 15, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("A_sm_alpha", 40, 1, 2, 0.25, 15, 0.01),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("A_sm_age", 40, 1, 0.005, 0.99, 3, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("A_sm_age", 40, 1, 0.05, 0.99, 3, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("A_sm_age", 40, 1, 0.5, 0.99, 3, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("A_sm_age", 40, 1, 2, 0.99, 3, 0.01)

            ///////////// 3 Strategies same params
            
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/base", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 0.1),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/L20", 20, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 0.1),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/N1000", 40, 1000, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 0.1),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/A", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/A", 40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.9, 8, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/A", 40, 1, 0.5, 0.5, 100000, 100000, null, null, null, 0.9, 8, 0.1),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/age", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 3, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/age", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 15, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/age", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 1, 0.1),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/stddev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 0.8),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/stddev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 0.01),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/stddev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.9, 8, 5),

            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/alpha", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.75, 8, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/alpha", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0, 8, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/alpha", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.25, 8, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/alpha", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.99, 8, 0.1)
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurvAgedLAb("same_param/alpha", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0, 8, 0.1)







            /////////////// AGED LAB FIXED /////////////////////////////////
            // () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, Integer.MAX_VALUE),
            // () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, 8),
            // () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, 3),
            // () -> ContinuousPCommTransition.agedLAb(40, 1, 0.5, 0.005, 100000, 100000, 0)

            /////////////// 5 a fixed ////////////////////////////
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb("base", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 8),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb("L", 20, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 8),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb("age", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 3),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb("stddev", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.8, 8),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb(null, 40, 1000, 0.5, 0.005, 100000, 100000, null, null, null, 0.1, 8),
            // () -> ContinuousPCommTransition.wordAcquisitionAgedLAb("A", 40, 1, 0.5, 0.05, 100000, 100000, null, null, null, 0.1, 8)

            ////////////////// JOINT TERRITORY ///////////////////////
            // () -> MultiWorld.joinTerritory("base1", 40, 1, 0.005, 0.5, 50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("base1", 40, 1, 0.005, 0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("base1", 40, 1, 0.005, 0.5,  50000,100000, 0.95, 0.95),
            
            // () -> MultiWorld.joinTerritory("base_diff_lab", 40, 1, 0.005, 0.5, 50000,100000, 0.95, 0.0),
            // () -> MultiWorld.joinTerritory("base_diff_lab", 40, 1, 0.005, 0.5,  50000,100000, 0.95, 0.0),
            // () -> MultiWorld.joinTerritory("base_diff_lab", 40, 1, 0.005, 0.5,  50000,100000, 0.95, 0.0),

            // () -> MultiWorld.joinTerritory(null,30, 1, 0.005, 0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory(null,30, 1, 0.005, 0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory(null, 20, 1, 0.005, 0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory(null, 20, 1, 0.005, 0.5,  50000,100000, 0.95, 0.95),

            // () -> MultiWorld.joinTerritory("p_comm1", 40, 1, 0.005, 0.01,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("p_comm1", 40, 1, 0.005, 0.001,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("p_comm1", 40, 1, 0.005, 0.1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("p_comm1", 40, 1, 0.005, 0.25,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("p_comm1", 40, 1, 0.005, 0.75,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("p_comm1", 40, 1, 0.005, 1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("p_comm1", 40, 1, 0.005, 0,  50000,100000, 0.95, 0.95),

            // () -> MultiWorld.joinTerritory("p_comm_cont", 40, 1, 0.005, 0.1, 0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("p_comm_cont", 40, 1, 0.005, 0, 0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("p_comm_cont", 40, 1, 0.005, 0.1, 0.98,  50000,100000, 0.95, 0.95)

            // () -> MultiWorld.joinTerritory("neighb_hor", 40, 1, 0.005, 0.5, null, new Neighbor4PositionsStrategy(false),  50000,100000, 0.95),
            // () -> MultiWorld.joinTerritory("neighb_hor", 40, 1, 0.005, 0.5, null, new Neighbor4PositionsStrategy(false),  50000,100000, 0.95),

            // () -> MultiWorld.joinTerritory("neighb_diag", 40, 1, 0.005, 0.5, null, new Neighbor4PositionsStrategy(true),  50000,100000, 0.95),
            // () -> MultiWorld.joinTerritory("neighb_diag", 40, 1, 0.005, 0.5, null, new Neighbor4PositionsStrategy(true),  50000,100000, 0.95),

            // todo
            // () -> MultiWorld.joinTerritory("high_p_comm_N2", 40, 2, 0.005, 1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N2", 40, 2, 0.005, 1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N5", 40, 5, 0.005, 1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N5", 40, 5, 0.005, 1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N10", 40, 10, 0.005, 1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N10", 40, 10, 0.005, 1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N1000", 40, 1000, 0.005, 1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N1000", 40, 1000, 0.005, 1,  50000,100000, 0.95, 0.95),

            // () -> MultiWorld.joinTerritory("high_p_comm_N2_sm_pcomm", 40, 2, 0.005, 0.001,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N2_sm_pcomm", 40, 2, 0.005, 0.001,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N5_sm_pcomm", 40, 5, 0.005, 0.001,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N5_sm_pcomm", 40, 5, 0.005, 0.001,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N10_sm_pcomm", 40, 10, 0.005, 0.001,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N10_sm_pcomm", 40, 10, 0.005, 0.001,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N1000_sm_pcomm", 40, 1000, 0.005, 0.001,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("high_p_comm_N1000_sm_pcomm", 40, 1000, 0.005, 0.001,  50000,100000, 0.95, 0.95),

            // () -> MultiWorld.joinTerritory("base_diff_lab_1", 40, 1, 0.005, 0.5, 50000,100000, 0.95, 0.0),
            // () -> MultiWorld.joinTerritory("base_diff_lab_1", 40, 1, 0.005, 0.5,  50000,100000, 0.95, 0.0),
            // () -> MultiWorld.joinTerritory("base_diff_lab_1", 40, 1, 0.005, 0.5,  50000,100000, 0.95, 0.0),

            // () -> MultiWorld.joinTerritory("base_diff_lab_w2", 40, 1, 0.005, 0.5, 50000,100000, 0.0, 0.95),
            // () -> MultiWorld.joinTerritory("base_diff_lab_w2", 40, 1, 0.005, 0.5,  50000,100000, 0.0, 0.95),
            // () -> MultiWorld.joinTerritory("base_diff_lab_w2", 40, 1, 0.005, 0.5,  50000,100000, 0.0, 0.95),
            // () -> MultiWorld.joinTerritory("base_diff_lab_w2", 40, 1, 0.005, 0.5, 50000,100000, 0.0, 0.95),
            // () -> MultiWorld.joinTerritory("base_diff_lab_w2", 40, 1, 0.005, 0.5,  50000,100000, 0.0, 0.95),
            // () -> MultiWorld.joinTerritory("base_diff_lab_w2", 40, 1, 0.005, 0.5,  50000,100000, 0.0, 0.95),


            // () -> MultiWorld.joinTerritory("N", 40, 2, 0.005,  0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("N", 40, 5, 0.005,  0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("N", 40, 10, 0.005,  0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.joinTerritory("N", 40, 1000, 0.005, 0.5,  50000,100000, 0.95, 0.95)




            
            ////////////////// RELOCATED ///////////////////////
            // () -> MultiWorld.relocate("base", 40, 1, 0.005, 0.5,  0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("base", 40, 1, 0.005, 0.5, 0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("base", 40, 1, 0.005, 0.5, 0.5,  50000,100000, 0.95, 0.95),

            // () -> MultiWorld.relocate("base_p0.5", 40, 1, 0.005, 0.5,  0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("base_p0.5", 40, 1, 0.005, 0.5, 0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("base_p0.5", 40, 1, 0.005, 0.5, 0.5,  50000,100000, 0.95, 0.95),

            // () -> MultiWorld.relocate("N", 40, 2, 0.005, 0.5,  0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("N", 40, 5, 0.005, 0.5,  0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("N", 40, 10, 0.005, 0.5,  0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("N", 40, 1000, 0.005, 0.5,  0.5,  50000,100000, 0.95, 0.95),

            // () -> MultiWorld.relocate("N_sm_pcomm", 40, 2, 0.005, 0.5,  0.001,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("N_sm_pcomm", 40, 5, 0.005, 0.5,  0.001,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("N_sm_pcomm", 40, 10, 0.005, 0.5,  0.001,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("N_sm_pcomm", 40, 1000, 0.005, 0.5,  0.001,  50000,100000, 0.95, 0.95),

            // () -> MultiWorld.relocate("portion", 40, 1, 0.005, 0.1,  0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("portion", 40, 1, 0.005, 0.3,  0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("portion", 40, 1, 0.005, 0.5,  0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("portion", 40, 1, 0.005, 0.25,  0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("portion", 40, 1, 0.005, 0.75,  0.5,  50000,100000, 0.95, 0.95),

            // () -> MultiWorld.relocate(null,30, 1, 0.005, 0.5, 0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate(null, 20, 1, 0.005, 0.5, 0.5,  50000,100000, 0.95, 0.95),

            // () -> MultiWorld.relocate("p_comm", 40, 1, 0.005, 0.5, 0.01,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("p_comm", 40, 1, 0.005, 0.5, 0.001,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("p_comm", 40, 1, 0.005,0.5, 0.1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("p_comm", 40, 1, 0.005, 0.5, 0.25,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("p_comm", 40, 1, 0.005, 0.5, 0.75,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("p_comm", 40, 1, 0.005, 0.5, 1,  50000,100000, 0.95, 0.95),

            // () -> MultiWorld.relocate("p_comm_cont", 40, 1, 0.005, 0.5, 0.1, 0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("p_comm_cont", 40, 1, 0.005, 0.5, 0, 0.5,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("p_comm_cont", 40, 1, 0.005, 0.5, 0.1, 0.98,  50000,100000, 0.95, 0.95),

            // () -> MultiWorld.relocate("compare_terr", 40, 1, 0.005, 0.5, 1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("compare_terr", 40, 1, 0.005, 0.5, 1, 50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("compare_terr", 40, 1, 0.005, 0.5, 0,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("compare_terr", 40, 1, 0.005, 0.5, 0,  50000,100000, 0.95, 0.95),

            // () -> MultiWorld.relocate("who_wins_w1", 40, 1, 0.005, 0.5, 0.5, 50000,100000, 0.95, 0),
            // () -> MultiWorld.relocate("who_wins_w1", 40, 1, 0.005, 0.5, 0.5, 50000,100000, 0.95, 0),
            // () -> MultiWorld.relocate("who_wins_w1", 40, 1, 0.005, 0.5, 0.5, 50000,100000, 0.95, 0),
            
            // () -> MultiWorld.relocate("who_wins_w2", 40, 1, 0.005, 0.5, 0.5, 50000,100000, 0, 0.95),
            // () -> MultiWorld.relocate("who_wins_w2", 40, 1, 0.005, 0.5, 0.5, 50000,100000, 0, 0.95),
            // () -> MultiWorld.relocate("who_wins_w2", 40, 1, 0.005, 0.5, 0.5, 50000,100000, 0, 0.95),

            // () -> MultiWorld.relocate("who_wins_0.3", 40, 1, 0.005, 0.3, 0.5, 50000,100000, 0, 0.95),
            // () -> MultiWorld.relocate("who_wins_0.3", 40, 1, 0.005, 0.3, 0.5, 50000,100000, 0, 0.95),
            // () -> MultiWorld.relocate("who_wins_0.3", 40, 1, 0.005, 0.3, 0.5, 50000,100000, 0, 0.95),


            // () -> MultiWorld.relocate("high_p_comm_N2", 40, 2, 0.005, 0.5, 1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("high_p_comm_N5", 40, 5, 0.005, 0.5, 1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("high_p_comm_N10", 40, 10, 0.005, 0.5, 1,  50000,100000, 0.95, 0.95),
            // () -> MultiWorld.relocate("high_p_comm_N1000", 40, 1000, 0.005, 0.5, 1,  50000,100000, 0.95, 0.95)



            // () -> MultiWorld.relocate("neighb_hor", 40, 1, 0.005, 0.3, 0.5, null, new Neighbor4PositionsStrategy(false),  50000,100000, 0.95),
            // () -> MultiWorld.relocate("neighb_hor", 40, 1, 0.005, 0.3,  0.5, null, new Neighbor4PositionsStrategy(false),  50000,100000, 0.95),
            
            // () -> MultiWorld.relocate("neighb_diag", 40, 1, 0.005, 0.3, 0.5, null, new Neighbor4PositionsStrategy(true),  50000,100000, 0.95),
            // () -> MultiWorld.relocate("neighb_diag", 20, 1, 0.005, 0.3, 0.5, null, new Neighbor4PositionsStrategy(true),  50000,100000, 0.95)

            //// TEST ALPHA 0 ////////
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("test_a_0", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.0, 0.1),
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("test_a_0", 40, 1, 0.5, 0.05, 40000, 100000, null, null, null, 0.0, 0.1)
            // () -> ContinuousPCommTransition.wordAcquisitionNewPSurv("test_a_0", 40, 1, 0.5, 0.005, 100000, 100000, null, null, null, 0.0, 0.001)

            // () -> ContinuousPCommTransition.newPSurv(40, 1, 0.5, 0.005, 100000, 100000, 0)

            // () -> AbruptTransitionRuns.original("lang_takeover_0.99", 40, 1, 0.05, 0),
            // () -> AbruptTransitionRuns.original("lang_takeover_0.99", 40, 1, 0.05, 0),
            // () -> AbruptTransitionRuns.original("lang_takeover_0.99", 40, 1, 0.05, 0),
            // () -> AbruptTransitionRuns.original("lang_takeover_0.99", 40, 1, 0.05, 0),

            // () -> AbruptTransitionRuns.original("lang_takeover_0.95", 40, 1, 0.05, 0),
            // () -> AbruptTransitionRuns.original("lang_takeover_0.95", 40, 1, 0.05, 0),
            // () -> AbruptTransitionRuns.original("lang_takeover_0.95", 40, 1, 0.05, 0),
            // () -> AbruptTransitionRuns.original("lang_takeover_0.95", 40, 1, 0.05, 0)

            // () -> ContinuousPCommTransition.original(60, 1, 0.5, 0.05, 100000, 200000)

            ////////// ABRUPT FOR FORMULA /////////////
            
            // () -> AbruptTransitionRuns.original("default", 40, 1,0.05, 0, new Neighbor8PositionsStrategy()),
            // () -> AbruptTransitionRuns.originalMoloney(40, 1,0.05, 0, new Neighbor8PositionsStrategy())

            // () -> AbruptTransitionRuns.original("neighb", 40, 1,0.05, 0, new Neighbor4PositionsStrategy(false)),
            // () -> AbruptTransitionRuns.original("neighb", 40, 1,0.05, 0, new Neighbor8PositionsStrategy())

            // () -> AbruptTransitionRuns.original("neighb", 40, 1, 0.05, 0, new Neighbor4PositionsStrategy(false)),
            // () -> AbruptTransitionRuns.original("neighb", 40, 1, 0.05)
            // () -> AbruptTransitionRuns.original("neighb", 40, 1, 0.05, 0, new Neighbor4PositionsStrategy(true))
           
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.01", 20, 1, 0.01),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.01", 20, 1, 0.01),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.01", 20, 1, 0.01),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.01", 20, 1, 0.01),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.01", 20, 1, 0.01),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.01", 20, 1, 0.01),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.01", 20, 1, 0.01),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.01", 20, 1, 0.01),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.01", 20, 1, 0.01),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.01", 20, 1, 0.01)
            
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.075", 20, 1, 0.075),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.075", 20, 1, 0.075),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.075", 20, 1, 0.075),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.075", 20, 1, 0.075),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.075", 20, 1, 0.075),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.075", 20, 1, 0.075),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.075", 20, 1, 0.075),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.075", 20, 1, 0.075),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.075", 20, 1, 0.075),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.075", 20, 1, 0.075)
           
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.8", 40, 1, 0.8),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.8", 40, 1, 0.8),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.8", 40, 1, 0.8),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.8", 40, 1, 0.8),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.8", 40, 1, 0.8),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.8", 40, 1, 0.8),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.8", 40, 1, 0.8),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.8", 40, 1, 0.8),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.8", 40, 1, 0.8),
            // () -> AbruptTransitionRuns.original("formula_0.98_a0.8", 40, 1, 0.8)

            // () -> AbruptTransitionRuns.original("formula_0.96", 40, 1, 0.05),
            // () -> AbruptTransitionRuns.original("formula_0.96", 40, 1, 0.05),
            // () -> AbruptTransitionRuns.original("formula_0.96", 40, 1, 0.05),
            // () -> AbruptTransitionRuns.original("formula_0.96", 40, 1, 0.05),
            // () -> AbruptTransitionRuns.original("formula_0.96", 40, 1, 0.05),
            // () -> AbruptTransitionRuns.original("formula_0.96", 40, 1, 0.05),
            // () -> AbruptTransitionRuns.original("formula_0.96", 40, 1, 0.05),
            // () -> AbruptTransitionRuns.original("formula_0.96", 40, 1, 0.05),
            // () -> AbruptTransitionRuns.original("formula_0.96", 40, 1, 0.05),
            // () -> AbruptTransitionRuns.original("formula_0.96", 20, 1, 0.05)


            // () -> AbruptTransitionRuns.original("decreasing_A_096", 40, 1,0.004),
            // () -> AbruptTransitionRuns.original("decreasing_A_096", 40, 1,0.003),
            // () -> AbruptTransitionRuns.original("decreasing_A_096", 40, 1,0.002),
            // () -> AbruptTransitionRuns.original("decreasing_A_096", 40, 1,0.001),
            // () -> AbruptTransitionRuns.original("decreasing_A_096", 40, 1,0.005)

            // () -> ContinuousPCommTransition.original(60, 1, 0.5, 0.005, 100000,200000),
            // () -> ContinuousPCommTransition.originalMoloney(60, 1, 0.5, 0.05, 100000,200000),
            // () -> ContinuousPCommTransition.originalMoloney(60, 1, 0.5, 0.005, 100000,200000)

            ///////////////////////////////////////////////////////////////////////////////////////
            // () -> ContinuousPCommTransition.original("base", 40, 1, 0.5, 0.05, 100000,200000),
            // () -> ContinuousPCommTransition.original("base", 40, 1, 0.5, 0.05, 100000,200000),
            // () -> ContinuousPCommTransition.original("base", 40, 1, 0.5, 0.05, 100000,200000),

            // () -> ContinuousPCommTransition.original("N", 40, 5, 0.5, 0.05, 100000,200000),
            // () -> ContinuousPCommTransition.original("N", 40, 5, 0.5, 0.05, 100000,200000),
            // () -> ContinuousPCommTransition.original("N", 40, 1000, 0.5, 0.05, 100000,200000),
            // () -> ContinuousPCommTransition.original("N", 40, 1000, 0.5, 0.05, 100000,200000),

            // () -> ContinuousPCommTransition.original("age", 40, 1, 0.5, 0.05, 100000,200000, new Neighbor8PositionsStrategy(), 1),
            // () -> ContinuousPCommTransition.original("age", 40, 1, 0.5, 0.05, 100000,200000, new Neighbor8PositionsStrategy(), 1),
            // () -> ContinuousPCommTransition.original("age", 40, 1, 0.5, 0.05, 100000,200000, new Neighbor8PositionsStrategy(), 5),
            // () -> ContinuousPCommTransition.original("age", 40, 1, 0.5, 0.05, 100000,200000, new Neighbor8PositionsStrategy(), 5),
            // () -> ContinuousPCommTransition.original("age", 40, 1, 0.5, 0.05, 100000,200000, new Neighbor8PositionsStrategy(), 10),
            // () -> ContinuousPCommTransition.original("age", 40, 1, 0.5, 0.05, 100000,200000, new Neighbor8PositionsStrategy(), 10),

            // () -> ContinuousPCommTransition.original("L", 20, 1, 0.5, 0.05, 100000,200000),
            // () -> ContinuousPCommTransition.original("L", 20, 1, 0.5, 0.05, 100000,200000),
            // () -> ContinuousPCommTransition.original("L", 30, 1, 0.5, 0.05, 100000,200000),
            // () -> ContinuousPCommTransition.original("L", 30, 1, 0.5, 0.05, 100000,200000),

            // () -> ContinuousPCommTransition.original("A", 40, 1, 0.5, 0, 100000,200000),
            // () -> ContinuousPCommTransition.original("A", 40, 1, 0.5, 0.005, 100000,200000),
            // () -> ContinuousPCommTransition.original("A", 40, 1, 0.5, 0.005, 100000,200000),
            // () -> ContinuousPCommTransition.original("A", 40, 1, 0.5, 0.1, 100000,200000),
            // () -> ContinuousPCommTransition.original("A", 40, 1, 0.5, 0.1, 100000,200000),
            // () -> ContinuousPCommTransition.original("A", 40, 1, 0.5, 0.5, 100000,200000),
            // () -> ContinuousPCommTransition.original("A", 40, 1, 0.5, 0.5, 100000,200000),

            // () -> ContinuousPCommTransition.original("neighb_not_diag", 40, 1, 0.5, 0.05, 100000,200000, new Neighbor4PositionsStrategy(false)),
            // () -> ContinuousPCommTransition.original("neighb_not_diag", 40, 1, 0.5, 0.05, 100000,200000, new Neighbor4PositionsStrategy(false)),
            // () -> ContinuousPCommTransition.original("neighb_not_diag", 40, 1, 0.5, 0.05, 100000,200000, new Neighbor4PositionsStrategy(false)),
            
            // () -> ContinuousPCommTransition.original("neighb", 40, 1, 0.5, 0.05, 100000,200000, new Neighbor4PositionsStrategy(true)),
            // () -> ContinuousPCommTransition.original("neighb", 40, 1, 0.5, 0.05, 100000,200000, new Neighbor4PositionsStrategy(true)),
            // () -> ContinuousPCommTransition.original("neighb", 40, 1, 0.5, 0.05, 100000,200000, new Neighbor4PositionsStrategy(true))

            // () -> ContinuousPCommTransition.original("A_small", 40, 1, 0.5, 0.003, 100000,200000),
            // () -> ContinuousPCommTransition.original("A_small", 40, 1, 0.5, 0.001, 100000,200000),
            // () -> ContinuousPCommTransition.original("A_small", 40, 1, 0.5, 0.002, 100000,200000),
            // () -> ContinuousPCommTransition.original("A_small", 40, 1, 0.5, 0.004, 100000,200000),
            // () -> ContinuousPCommTransition.original("A_small", 40, 1, 0.5, 0.0005, 100000,200000)
            
            // () -> ContinuousPCommTransition.original("A_big", 40, 1, 0.5, 2, 100000,200000),
            // () -> ContinuousPCommTransition.original("A_big", 40, 1, 0.5, 3, 100000,200000),
            // () -> ContinuousPCommTransition.original("A_big", 40, 1, 0.5, 5, 100000,200000),
            // () -> ContinuousPCommTransition.original("A_big", 40, 1, 0.5, 4, 100000,200000)

            // () -> ContinuousPCommTransition.originalMoloney(60, 1, 0.5, 0.05, 100000,200000)

            // () -> PCommDecrease.original("base", 60, 1, 0.05, false, 3000, 7000, 0.01, 0.18),

            // () -> PCommIncrease.originalMoloney(60, 0.05, 1, 7000, 3000, 0.19),
            // () -> PCommDecrease.original("base_0.05", 60, 1, 0.05, true, 3000, 7000, 0.01, 0.15)
            // () -> PCommDecrease.original("base_0.005", 60, 1, 0.005, true, 3000, 7000, 0.0, 0.10)

            // () -> PCommDecrease.original("base", 40, 1, 0.05, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("base", 40, 1, 0.05, false, 3000, 10000, 0.01, 0.15),

            // () -> PCommDecrease.original("L", 20, 1, 0.05, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("L", 20, 1, 0.05, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("L", 30, 1, 0.05, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("L", 30, 1, 0.05, false, 3000, 10000, 0.01, 0.15),

            // () -> PCommDecrease.original("N", 40, 5, 0.05, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("N", 40, 5, 0.05, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("N", 40, 1000, 0.05, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("N", 40, 1000, 0.05, false, 3000, 10000, 0.01, 0.15),

            // () -> PCommDecrease.original("A", 40, 1, 0.005, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("A", 40, 1, 0.005, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("A", 40, 1, 0.5, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("A", 40, 1, 0.5, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("A", 40, 1, 0.1, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("A", 40, 1, 0.1, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("A", 40, 1, 2, false, 3000, 10000, 0.01, 0.15),
            // () -> PCommDecrease.original("A", 40, 1, 2, false, 3000, 10000, 0.01, 0.15)

            // () -> PCommIncrease.original(40, 0.05, 1, false, 8000, 3000, 0.19, 0.11, new Neighbor4PositionsStrategy(false)),
            // () -> PCommIncrease.original(40, 0.05, 1, false, 8000, 3000, 0.19, 0.11, new Neighbor4PositionsStrategy(false)),
            // () -> PCommIncrease.original(40, 0.05, 1, false, 8000, 3000, 0.19, 0.11, new Neighbor4PositionsStrategy(true)),
            // () -> PCommIncrease.original(40, 0.05, 1, false, 8000, 3000, 0.19, 0.11, new Neighbor4PositionsStrategy(true)),

            // () -> PCommDecrease.original("neighb", 40, 1, 0.05, false, 3000, 8000, 0.01, 0.15, new Neighbor4PositionsStrategy(false)),
            // () -> PCommDecrease.original("neighb", 40, 1, 0.05, false, 3000, 8000, 0.01, 0.15, new Neighbor4PositionsStrategy(false)),
            // () -> PCommDecrease.original("neighb_diag", 40, 1, 0.05, false, 3000, 8000, 0.01, 0.15, new Neighbor4PositionsStrategy(true)),
            // () -> PCommDecrease.original("neighb_diag", 40, 1, 0.05, false, 3000, 8000, 0.01, 0.15, new Neighbor4PositionsStrategy(true))


            // () -> ConstantPCommTransition.run("p_0.5", 20, 1, 0.0, 0.05, 10000, 0.001),
            // () -> ConstantPCommTransition.run("p_0.5", 20, 1, 0.0, 0.05, 10000, 0.001),
            // () -> ConstantPCommTransition.run("p_0.5", 20, 1, 0.0, 0.05, 10000, 0.001),

            /////////////////////////////// CORRELATION

            // () -> ConstantPCommTransition.run("mut_0/p_0", 30, 1, 0.0, 0.05, 50000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0/p_0", 30, 1, 0.0, 0.05, 50000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0/p_0", 30, 1, 0.0, 0.05, 50000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0/p_0", 30, 1, 0.0, 0.05, 50000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0/p_0", 30, 1, 0.0, 0.05, 50000, 0.001),
           
            // () -> ConstantPCommTransition.run("mut_0/p_0.1", 30, 1, 0.1, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.1", 30, 1, 0.1, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.1", 30, 1, 0.1, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.1", 30, 1, 0.1, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.1", 30, 1, 0.1, 0.05, 50000, 0),
            
            // () -> ConstantPCommTransition.run("mut_0/p_0.2", 30, 1, 0.2, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.2", 30, 1, 0.2, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.2", 30, 1, 0.2, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.2", 30, 1, 0.2, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.2", 30, 1, 0.2, 0.05, 50000, 0),
           
            // () -> ConstantPCommTransition.run("mut_0/p_0.3", 30, 1, 0.3, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.3", 30, 1, 0.3, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.3", 30, 1, 0.3, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.3", 30, 1, 0.3, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.3", 30, 1, 0.3, 0.05, 50000, 0),
            
            // () -> ConstantPCommTransition.run("mut_0/p_0.4", 30, 1, 0.4, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.4", 30, 1, 0.4, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.4", 30, 1, 0.4, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.4", 30, 1, 0.4, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.4", 30, 1, 0.4, 0.05, 50000, 0),
           
            // () -> ConstantPCommTransition.run("mut_0/p_0.5", 30, 1, 0.5, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.5", 30, 1, 0.5, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.5", 30, 1, 0.5, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.5", 30, 1, 0.5, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.5", 30, 1, 0.5, 0.05, 50000, 0),
            
            // () -> ConstantPCommTransition.run("mut_0/p_0.6", 30, 1, 0.6, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.6", 30, 1, 0.6, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.6", 30, 1, 0.6, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.6", 30, 1, 0.6, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.6", 30, 1, 0.6, 0.05, 50000, 0),

            // () -> ConstantPCommTransition.run("mut_0/p_0.7", 30, 1, 0.7, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.7", 30, 1, 0.7, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.7", 30, 1, 0.7, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.7", 30, 1, 0.7, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.7", 30, 1, 0.7, 0.05, 50000, 0),
           
            // () -> ConstantPCommTransition.run("mut_0/p_0.8", 30, 1, 0.8, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.8", 30, 1, 0.8, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.8", 30, 1, 0.8, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.8", 30, 1, 0.8, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.8", 30, 1, 0.8, 0.05, 50000, 0),
            
            // () -> ConstantPCommTransition.run("mut_0/p_0.9", 30, 1, 0.9, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.9", 30, 1, 0.9, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.9", 30, 1, 0.9, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.9", 30, 1, 0.9, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.9", 30, 1, 0.9, 0.05, 50000, 0),
        
            // () -> ConstantPCommTransition.run("mut_0/p_1", 30, 1, 1, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_1", 30, 1, 1, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_1", 30, 1, 1, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_1", 30, 1, 1, 0.05, 50000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_1", 30, 1, 1, 0.05, 50000, 0)

              
            ////////////// CORRELATION MUTATION ONLY
            
            // () -> ConstantPCommTransition.run("p_0/mut_0.001", 30, 1, 0.0, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("p_0/mut_0.001", 30, 1, 0.0, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("p_0/mut_0.001", 30, 1, 0.0, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("p_0/mut_0.001", 30, 1, 0.0, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("p_0/mut_0.001", 30, 1, 0.0, 0.05, 30000, 0.001),

            // () -> ConstantPCommTransition.run("p_0/mut_0.005", 30, 1, 0.0, 0.05, 30000, 0.005),
            // () -> ConstantPCommTransition.run("p_0/mut_0.005", 30, 1, 0.0, 0.05, 30000, 0.005),
            // () -> ConstantPCommTransition.run("p_0/mut_0.005", 30, 1, 0.0, 0.05, 30000, 0.005),
            // () -> ConstantPCommTransition.run("p_0/mut_0.005", 30, 1, 0.0, 0.05, 30000, 0.005),
            // () -> ConstantPCommTransition.run("p_0/mut_0.005", 30, 1, 0.0, 0.05, 30000, 0.005),

            // () -> ConstantPCommTransition.run("p_0/mut_0.5", 30, 1, 0.0, 0.05, 30000, 0.5),
            // () -> ConstantPCommTransition.run("p_0/mut_0.5", 30, 1, 0.0, 0.05, 30000, 0.5),
            // () -> ConstantPCommTransition.run("p_0/mut_0.5", 30, 1, 0.0, 0.05, 30000, 0.5),
            // () -> ConstantPCommTransition.run("p_0/mut_0.5", 30, 1, 0.0, 0.05, 30000, 0.5),
            // () -> ConstantPCommTransition.run("p_0/mut_0.5", 30, 1, 0.0, 0.05, 30000, 0.5),
            
            // () -> ConstantPCommTransition.run("p_0/mut_0.01", 30, 1, 0.0, 0.05, 30000, 0.01),
            // () -> ConstantPCommTransition.run("p_0/mut_0.01", 30, 1, 0.0, 0.05, 30000, 0.01),
            // () -> ConstantPCommTransition.run("p_0/mut_0.01", 30, 1, 0.0, 0.05, 30000, 0.01),
            // () -> ConstantPCommTransition.run("p_0/mut_0.01", 30, 1, 0.0, 0.05, 30000, 0.01),
            // () -> ConstantPCommTransition.run("p_0/mut_0.01", 30, 1, 0.0, 0.05, 30000, 0.01),

            // () -> ConstantPCommTransition.run("p_0/mut_0.0001", 30, 1, 0.0, 0.05, 30000, 0.0001),
            // () -> ConstantPCommTransition.run("p_0/mut_0.0001", 30, 1, 0.0, 0.05, 30000, 0.0001),
            // () -> ConstantPCommTransition.run("p_0/mut_0.0001", 30, 1, 0.0, 0.05, 30000, 0.0001),
            // () -> ConstantPCommTransition.run("p_0/mut_0.0001", 30, 1, 0.0, 0.05, 30000, 0.0001),
            // () -> ConstantPCommTransition.run("p_0/mut_0.0001", 30, 1, 0.0, 0.05, 30000, 0.0001)
        
        
            ///////// COMMUNICATION ONLY

            // () -> ConstantPCommTransition.run("mut_0/p_0.1", 30, 1, 0.1, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.1", 30, 1, 0.1, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.1", 30, 1, 0.1, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.1", 30, 1, 0.1, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.1", 30, 1, 0.1, 0.05, 30000, 0),
            
            // () -> ConstantPCommTransition.run("mut_0/p_0.2", 30, 1, 0.2, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.2", 30, 1, 0.2, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.2", 30, 1, 0.2, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.2", 30, 1, 0.2, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.2", 30, 1, 0.2, 0.05, 30000, 0),
           
            // () -> ConstantPCommTransition.run("mut_0/p_0.3", 30, 1, 0.3, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.3", 30, 1, 0.3, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.3", 30, 1, 0.3, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.3", 30, 1, 0.3, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.3", 30, 1, 0.3, 0.05, 30000, 0),
            
            // () -> ConstantPCommTransition.run("mut_0/p_0.4", 30, 1, 0.4, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.4", 30, 1, 0.4, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.4", 30, 1, 0.4, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.4", 30, 1, 0.4, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.4", 30, 1, 0.4, 0.05, 30000, 0),
           
            // () -> ConstantPCommTransition.run("mut_0/p_0.5", 30, 1, 0.5, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.5", 30, 1, 0.5, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.5", 30, 1, 0.5, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.5", 30, 1, 0.5, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.5", 30, 1, 0.5, 0.05, 30000, 0),
            
            // () -> ConstantPCommTransition.run("mut_0/p_0.6", 30, 1, 0.6, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.6", 30, 1, 0.6, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.6", 30, 1, 0.6, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.6", 30, 1, 0.6, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.6", 30, 1, 0.6, 0.05, 30000, 0),

            // () -> ConstantPCommTransition.run("mut_0/p_0.7", 30, 1, 0.7, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.7", 30, 1, 0.7, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.7", 30, 1, 0.7, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.7", 30, 1, 0.7, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.7", 30, 1, 0.7, 0.05, 30000, 0),
           
            // () -> ConstantPCommTransition.run("mut_0/p_0.8", 30, 1, 0.8, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.8", 30, 1, 0.8, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.8", 30, 1, 0.8, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.8", 30, 1, 0.8, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.8", 30, 1, 0.8, 0.05, 30000, 0),
            
            // () -> ConstantPCommTransition.run("mut_0/p_0.9", 30, 1, 0.9, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.9", 30, 1, 0.9, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.9", 30, 1, 0.9, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.9", 30, 1, 0.9, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_0.9", 30, 1, 0.9, 0.05, 30000, 0),
        
            // () -> ConstantPCommTransition.run("mut_0/p_1", 30, 1, 1, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_1", 30, 1, 1, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_1", 30, 1, 1, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_1", 30, 1, 1, 0.05, 30000, 0),
            // () -> ConstantPCommTransition.run("mut_0/p_1", 30, 1, 1, 0.05, 30000, 0)


            ////// COMMUNICATION + MUTATION
            
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.1", 30, 1, 0.1, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.1", 30, 1, 0.1, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.1", 30, 1, 0.1, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.1", 30, 1, 0.1, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.1", 30, 1, 0.1, 0.05, 30000, 0.001),
            
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.2", 30, 1, 0.2, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.2", 30, 1, 0.2, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.2", 30, 1, 0.2, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.2", 30, 1, 0.2, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.2", 30, 1, 0.2, 0.05, 30000, 0.001),
           
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.3", 30, 1, 0.3, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.3", 30, 1, 0.3, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.3", 30, 1, 0.3, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.3", 30, 1, 0.3, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.3", 30, 1, 0.3, 0.05, 30000, 0.001),
            
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.4", 30, 1, 0.4, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.4", 30, 1, 0.4, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.4", 30, 1, 0.4, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.4", 30, 1, 0.4, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.4", 30, 1, 0.4, 0.05, 30000, 0.001),
           
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.5", 30, 1, 0.5, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.5", 30, 1, 0.5, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.5", 30, 1, 0.5, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.5", 30, 1, 0.5, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.5", 30, 1, 0.5, 0.05, 30000, 0.001),
            
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.6", 30, 1, 0.6, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.6", 30, 1, 0.6, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.6", 30, 1, 0.6, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.6", 30, 1, 0.6, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.6", 30, 1, 0.6, 0.05, 30000, 0.001),

            // () -> ConstantPCommTransition.run("mut_0.001/p_0.7", 30, 1, 0.7, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.7", 30, 1, 0.7, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.7", 30, 1, 0.7, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.7", 30, 1, 0.7, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.7", 30, 1, 0.7, 0.05, 30000, 0.001),
           
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.8", 30, 1, 0.8, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.8", 30, 1, 0.8, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.8", 30, 1, 0.8, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.8", 30, 1, 0.8, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.8", 30, 1, 0.8, 0.05, 30000, 0.001),
            
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.9", 30, 1, 0.9, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.9", 30, 1, 0.9, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.9", 30, 1, 0.9, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.9", 30, 1, 0.9, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_0.9", 30, 1, 0.9, 0.05, 30000, 0.001),
        
            // () -> ConstantPCommTransition.run("mut_0.001/p_1", 30, 1, 1, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_1", 30, 1, 1, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_1", 30, 1, 1, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_1", 30, 1, 1, 0.05, 30000, 0.001),
            // () -> ConstantPCommTransition.run("mut_0.001/p_1", 30, 1, 1, 0.05, 30000, 0.001)


            //////////////      A PARAM       /////////////////////////////////////////////////
            
            // () -> investigateA(20, 2000, 1, 0.8, 0.99, 0.01, 2),
            // () -> investigateA(40, 2000, 1, 0.8, 0.99, 0.01, 2),

            // () -> investigatePComm(20, 2000, 0, 1, 0.5, 0.99, 0.01, 2),
            // () -> investigatePComm(40, 2000, 0, 1, 0.5, 0.99, 0.01, 2)


            // () -> investigateA(40, 2000, 1000, 0, 1, 0.5, 0.99, 0.05, 0.02, 2),

            // () -> investigatePComm(40, 2000, 1000, 0, 1, 0.5, 0.99, 0.05, 0.02, 2)

            // () -> ContinuousPCommTransition.originalMoloney(60, 1, 0.5, 0.005, 20000, 200000),

            // () -> PCommIncrease.original(60, 0.005, 1, true, 7000, 3000, 0.17, 0.1, new Neighbor8PositionsStrategy() )

            () -> ConstantPCommTransition.run("p_comm_0_mut_0", 30, 1, 0.0, 0.05, 30000, 0.0),
            () -> ConstantPCommTransition.run("p_comm_0_mut_0", 30, 1, 0.0, 0.05, 30000, 0.0),
            () -> ConstantPCommTransition.run("p_comm_0_mut_0", 30, 1, 0.0, 0.05, 30000, 0.0),
            () -> ConstantPCommTransition.run("p_comm_0_mut_0", 30, 1, 0.0, 0.05, 30000, 0.0),
            () -> ConstantPCommTransition.run("p_comm_0_mut_0", 30, 1, 0.0, 0.05, 30000, 0.0)


            );




        for (Runnable task : simulationTasks) {
            executorService.submit(task);
        }

        executorService.shutdown();
    }

    public static void investigateA(int L, int T, int skipT, double minA, double maxA, double minPComm, double maxPComm, double pCommStep, double pCommStepSmall, int nRuns) {
        String parentFolder = "investigate_A";
        String folder = parentFolder + "/alive_depend_a_L_" + L + "/" + new Date().getTime();
        Map<Double, Object> allStats = new HashMap<>();
        for (double pComm = minPComm; pComm <= maxPComm; pComm = round(pComm + (pComm < 0.8 ? pCommStep : pCommStepSmall), 10000)) {
            List<Double> valuesA = new ArrayList<>();
            List<Double> valuesAvgAlive = new ArrayList<>();
            List<Double> valuesActualPUpds = new ArrayList<>();
            for (double A = minA; A <= maxA; A = round(A + (A < 0.01 ? 0.002 : A < 0.1 ? 0.01 : 0.1), 100000)) {
                valuesA.add(A);
                double totalAvgAlive = 0;
                double count = 0;
                
                System.out.println("pComm = " + pComm + ", A = " + A);
                for (int i = 0; i < nRuns; i++) {
                    VarConfig varConfig = new VarConfig(Map.of(ConfigKey.T, T, ConfigKey.L, L, ConfigKey.A, A));
                    StrategyConfig strategyConfig = new StrategyConfig(
                        new ConstantPCommunicationStrategy(pComm),
                        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                        new RandomLAbInheritanceStrategy(),
                        new ConstantLAbAgingStrategy(),
                        new Neighbor8PositionsStrategy(),
                        new UnitWordAcquisitionStrategy(),
                        new ProbabilisticEvolutionStrategy());
                    SimulationStats stats = new SimulationStats();
                    Simulation simulation = new Simulation(stats, varConfig, strategyConfig);
                    simulation.start();

                    System.out.println(stats.getNAgentsAlive().size());
                    if (stats.getNAgentsAlive().size() < skipT) {
                        continue;
                    }
                    double[] results = RunUtils.getSeriesStats(stats.getNAgentsAlive().subList(skipT, stats.getAvgAges().size()));
                    totalAvgAlive += results[0];
                    count++;
                }
                double agentsAlive = count == 0 ? 0 : totalAvgAlive/count;
                valuesAvgAlive.add(agentsAlive);
                valuesActualPUpds.add(agentsAlive == 0 ? 0 : L*L / agentsAlive * (1 - pComm));
            }

            SimulationPlots plots = new SimulationPlots(folder);
            plots.plotSeriesAsDependentOnAnother(valuesA, valuesAvgAlive, "alive_over_A_pComm=" + pComm, "A", "# Alive Agents", "A", 0.0, (double) L*L, true);
            plots.plotSeriesAsDependentOnAnother(valuesA, valuesActualPUpds, "pUpd_over_A_pComm=" + pComm, "A", "# Actual p_upd", "A", 0.0, 6.0, true);
            List<List<Double>> data = List.of(valuesA, valuesAvgAlive, valuesActualPUpds);
            IOUtils.exportToJson(data, "out/" + folder + "/data_pComm=" + pComm + ".json");
            allStats.put(pComm, data);
        }
        IOUtils.exportToJson(allStats, "out/" + folder + "/data_all.json");
    }

    public static double round(double d, double prec) {
        return Math.round(d * prec)/prec;
    }

    public static void investigatePComm(int L, int T, int skipT, double minA, double maxA, double minPComm, double maxPComm, double pCommStep, double pCommStepSmall, int nRuns) {
        String parentFolder = "investigate_p_comm";
        String folder = parentFolder + "/alive_depend_p_comm_L_" + L + "/" + new Date().getTime();
        Map<Double, Object> allStats = new HashMap<>();
        for (double A = minA; A < maxA; A = round(A + (A < 0.01 ? 0.002 : A < 0.1 ? 0.01 : 0.1), 100000)) {
            List<Double> valuesPComm = new ArrayList<>();
            List<Double> valuesAvgAlive = new ArrayList<>();
            List<Double> valuesActualPUpds = new ArrayList<>();
            for (double pComm = minPComm; pComm <= maxPComm; pComm = round(pComm + (pComm < 0.8 ? pCommStep : pCommStepSmall), 10000)) {
                valuesPComm.add(pComm);
                double totalAvgAlive = 0;
                int count = 0;
                
                System.out.println("A = " + A + ", pComm = " + pComm);
                for (int i = 0; i < nRuns; i++) {
                    VarConfig varConfig = new VarConfig(Map.of(ConfigKey.T, T, ConfigKey.L, L, ConfigKey.A, A));
                    StrategyConfig strategyConfig = new StrategyConfig(
                        new ConstantPCommunicationStrategy(pComm),
                        new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                        new RandomLAbInheritanceStrategy(),
                        new ConstantLAbAgingStrategy(),
                        new Neighbor8PositionsStrategy(),
                        new UnitWordAcquisitionStrategy(),
                        new ProbabilisticEvolutionStrategy());
                    SimulationStats stats = new SimulationStats();
                    Simulation simulation = new Simulation(stats, varConfig, strategyConfig);
                    simulation.start();

                    System.out.println(stats.getNAgentsAlive().size());
                    if (stats.getNAgentsAlive().size() < skipT) {
                        continue;
                    }
                    double[] results = RunUtils.getSeriesStats(stats.getNAgentsAlive().subList(skipT, stats.getAvgAges().size()));
                    totalAvgAlive += results[0];
                    count++;
                }
                double agentsAlive = count == 0 ? 0 : totalAvgAlive/count;
                valuesAvgAlive.add(agentsAlive);
                System.out.println("agentsAlive = " + agentsAlive);
                valuesActualPUpds.add(agentsAlive == 0 ? 0 : L*L / agentsAlive * (1 - pComm));
            }
            SimulationPlots plots = new SimulationPlots(folder);
            plots.plotSeriesAsDependentOnAnother(valuesPComm, valuesAvgAlive, "alive_over_pComm_A=" + A, "p_comm", "# Alive Agents", "PComm", 0.0, (double) L*L);
            plots.plotSeriesAsDependentOnAnother(valuesPComm, valuesActualPUpds, "pUpd_over_pComm_A=" + A, "p_comm", "# Actual p_upd", "PComm", 0.0, 1.05);
            List<List<Double>> data = List.of(valuesPComm, valuesAvgAlive, valuesActualPUpds);
            IOUtils.exportToJson(data, "out/" + folder + "/data_A=" + A + ".json");
            allStats.put(A, data);
        }
        IOUtils.exportToJson(allStats, "out/" + folder + "/data_all.json");
    }

    public static void abruptPCommIncrease(int L, int N, double stdDev) {
        String folder = "abrupt_p_comm_increase/L=" + L + "_N=" + N+"_stdDev="+stdDev;
        VarConfig varConfig = new VarConfig(Map.of(
                ConfigKey.L, L,
                ConfigKey.T, 50000,
                ConfigKey.N, N
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                new SingleStepPCommunicationStrategy(0.1, 8000, 0.98),
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new MutatedLAbInheritanceStrategy(stdDev),
                new ConstantLAbAgingStrategy(),
                new Neighbor8PositionsStrategy(),
                new UnitWordAcquisitionStrategy(),
                new ProbabilisticEvolutionStrategy()
        );

        SimulationStats simulationStats = new SimulationStats(
                List.of(1000, 5000, 7990, 8000, 8500, 10000, 30000, 40000, varConfig.T() - 1),
                List.of()
        );

        Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

        runSimulation(simulation, folder);
    }

    public static void runSimulation(Simulation simulation, String folder) {
        Timer timer = new Timer();
        timer.start();

        SimulationPlots simulationPlots = new SimulationPlots(folder);

        simulation.start();

        timer.stop("Simulation ended");

        simulationPlots.saveSimulationStats(
            simulation.getSimulationStats(),
            simulation.getStrategyConfig().getPCommunicationStrategy(),
            simulation.getVarConfig().T());
        IOUtils.saveSimulationConfig(folder, simulation);
    }


    public static void continuousPCommIncrease(int L, int N, double stdDev) {
        String folder = "continuous_p_comm_increase0.1-0.5/L=" + L+"_N="+N+"_stdDev="+stdDev;
        VarConfig varConfig = new VarConfig(Map.of(
                ConfigKey.L, L,
                ConfigKey.T, 80000,
                ConfigKey.N, N
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                new ContinuousIncreasePCommunicationStrategy(0.1, 0.5, varConfig.T()),
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new MutatedLAbInheritanceStrategy(stdDev),
                new ConstantLAbAgingStrategy(),
                new Neighbor8PositionsStrategy(),
                new UnitWordAcquisitionStrategy(),
                new ProbabilisticEvolutionStrategy()
        );

        SimulationStats simulationStats = new SimulationStats(
                List.of(varConfig.T() - 1),
                List.of(0.1, 0.12, 0.13, 0.15, 0.18, 0.22, 0.25, 0.28, 0.4, 0.49)
        );

        Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

        runSimulation(simulation, folder);
    }
    
    public static void runOrdinarySimulation() {
        String folder = "TEST";
        VarConfig varConfig = new VarConfig(Map.of(
                ConfigKey.L, 20,
                ConfigKey.T, 10000
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                new SingleStepPCommunicationStrategy(0.1, 5000, 0.98),
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new MutatedLAbInheritanceStrategy(0.1),
                new ConstantLAbAgingStrategy(),
                new Neighbor8PositionsStrategy(),
                new UnitWordAcquisitionStrategy(),
                new ProbabilisticEvolutionStrategy()
        );

        SimulationStats simulationStats = new SimulationStats();

        Simulation simulation = new Simulation(simulationStats, varConfig, strategyConfig);

        runSimulation(simulation, folder);
    }

    public static void runPCommIncreaseSimulations() {

        VarConfig varConfig = new VarConfig(Map.of(
                ConfigKey.T, 3000
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                null,
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new RandomLAbInheritanceStrategy(),
                new ConstantLAbAgingStrategy(),
                new Neighbor8PositionsStrategy(),
                new UnitWordAcquisitionStrategy(),
                new ProbabilisticEvolutionStrategy()
        );

        int nSkipIterations = 1000;

        String folder = "p_comm_new_0.11-0.27_3k_iters";
        SimulationPlots simulationPlots = new SimulationPlots(folder);

        List<Double> pCommunicationValues = List.of(0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.2, 0.21, 0.22, 0.23, 0.24, 0.25, 0.26, 0.27);
        List<Double> avgSuccessRates = new ArrayList<>();
        List<Double> avgLearningAbilities = new ArrayList<>();

        Simulation simulation = new Simulation(null, varConfig, strategyConfig);

        IOUtils.saveSimulationConfig(folder, simulation);

        Timer timer = new Timer();
        timer.start();

        for (double pComm : pCommunicationValues) {
            System.out.println("Starting simulation for pComm = " + pComm);

            Timer simulationTimer = new Timer();
            simulationTimer.start();
            
            PCommunicationStrategy strategy = new ConstantPCommunicationStrategy(pComm);
            SimulationStats simulationStats = new SimulationStats(nSkipIterations);

            simulation.setPCommunicationStrategy(strategy);
            simulation.setSimulationStats(simulationStats);
            simulation.start();

            simulationTimer.stop("Simulation for pComm = " + pComm + " done");
            
            Double avgLearningAbility = simulationStats.getAvgLearningAbility();
            avgLearningAbilities.add(avgLearningAbility);
            
            Double avgSuccessRate = simulationStats.getAvgSuccessRate();
            avgSuccessRates.add(avgSuccessRate);

            simulationPlots.plotTwoSeriesOverIterations(simulationStats.getAvgLearningAbilities(),
                simulationStats.getSuccessRates(), "l_ab_&_s_rate, pComm = " + pComm, "Iteration",
                "Value", 0, 1, "Learning Ability", "Success Rate", 0.0, 1.2);

            IOUtils.exportToJson(avgLearningAbility, "out/" + folder + "/l_ab_pComm_"+pComm+".json");
            IOUtils.exportToJson(avgSuccessRate, "out/" + folder + "/s_rate_pComm_"+pComm+".json");
            IOUtils.saveWorld(simulation.getWorld(), "out/" + folder + "/world_after_pComm_"+pComm+".ser");
        }

        simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgLearningAbilities, "l_ab_over_p_comm", "P_Communication", "Learning Ability", "Learning Ability", null, null, true);
        simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgSuccessRates, "s_rate_over_p_comm", "P_Communication", "Success Rate", "Success Rate", null, null, true);
        
        IOUtils.exportToJson(avgLearningAbilities, "out/" + folder + "/l_ab_pComm_all.json");
        IOUtils.exportToJson(avgSuccessRates, "out/" + folder + "/s_rate_pComm_all.json");

        timer.stop("All simulations done");
    }

    public static void runPCommDecreaseSimulations() {

        VarConfig varConfig = new VarConfig(Map.of(
                ConfigKey.T, 100
        ));
        StrategyConfig strategyConfig = new StrategyConfig(
                new ConstantPCommunicationStrategy(0.99),
                new AvgKnowledgePSurvivalStrategy(varConfig.A(), varConfig.B()),
                new RandomLAbInheritanceStrategy(),
                new ConstantLAbAgingStrategy(),
                new Neighbor8PositionsStrategy(),
                new UnitWordAcquisitionStrategy(),
                new FullEvolutionStrategy()
        );
        String folder = "p_comm_decrease/full_pre-sim";
        String preSimulationStatsFolder = folder + "/pre-simulation";
        SimulationPlots preSimulationPlots = new SimulationPlots(preSimulationStatsFolder);
        SimulationPlots simulationPlots = new SimulationPlots(folder);

        Simulation simulation = new Simulation(new SimulationStats(List.of(4000, 4500, 4999), List.of()), varConfig, strategyConfig);

        IOUtils.saveSimulationConfig(preSimulationStatsFolder, simulation);
        simulation.start();

        preSimulationPlots.saveSimulationStats(simulation.getSimulationStats(), strategyConfig.getPCommunicationStrategy(), varConfig.T());
        varConfig.setT(3000);

        int nSkipIterations = 1000;

        List<Double> pCommunicationValues = List.of(0.27, 0.26, 0.25, 0.24, 0.23, 0.22, 0.21, 0.2, 0.19, 0.18, 0.17, 0.16, 0.15, 0.14, 0.13, 0.12, 0.11);
        List<Double> avgSuccessRates = new ArrayList<>();
        List<Double> avgLearningAbilities = new ArrayList<>();

        Timer timer = new Timer();
        timer.start();

        for (double pComm : pCommunicationValues) {
            System.out.println("Starting simulation for pComm = " + pComm);

            Timer simulationTimer = new Timer();
            simulationTimer.start();
            
            PCommunicationStrategy strategy = new ConstantPCommunicationStrategy(pComm);
            SimulationStats simulationStats = new SimulationStats(nSkipIterations);

            simulation.setPCommunicationStrategy(strategy);
            simulation.setSimulationStats(simulationStats);
            simulation.start();

            simulationTimer.stop("Simulation for pComm = " + pComm + " done");
            
            Double avgLearningAbility = simulationStats.getAvgLearningAbility();
            avgLearningAbilities.add(avgLearningAbility);
            
            Double avgSuccessRate = simulationStats.getAvgSuccessRate();
            avgSuccessRates.add(avgSuccessRate);

            simulationPlots.plotTwoSeriesOverIterations(simulationStats.getAvgLearningAbilities(),
                simulationStats.getSuccessRates(), "l_ab_&_s_rate, pComm = " + pComm, "Iteration",
                "Value", 0, 1, "Learning Ability", "Success Rate", 0.0, 1.2);

            IOUtils.exportToJson(avgLearningAbility, "out/" + folder + "/l_ab_pComm_"+pComm+".json");
            IOUtils.exportToJson(avgSuccessRate, "out/" + folder + "/s_rate_pComm_"+pComm+".json");
            IOUtils.saveWorld(simulation.getWorld(), "out/" + folder + "/world_after_pComm_"+pComm+".ser");
        }

        simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgLearningAbilities, "l_ab_over_p_comm_avg", "P_Communication", "Learning Ability", "Learning Ability", null, null, true);
        simulationPlots.plotSeriesAsDependentOnAnother(pCommunicationValues, avgSuccessRates, "s_rate_over_p_comm_avg", "P_Communication", "Success Rate", "Success Rate", null, null, true);
        
        IOUtils.exportToJson(avgLearningAbilities, "out/" + folder + "/l_ab_pComm_all.json");
        IOUtils.exportToJson(avgSuccessRates, "out/" + folder + "/s_rate_pComm_all.json");

        timer.stop("All simulations done");
    }
}