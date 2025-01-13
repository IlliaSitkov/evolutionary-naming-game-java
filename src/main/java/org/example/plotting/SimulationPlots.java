package org.example.plotting;

import org.example.stats.SimulationStats;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SimulationPlots {
    private final SimulationStats simulationStats;

    public SimulationPlots(SimulationStats simulationStats) {
        this.simulationStats = simulationStats;
    }

    public void plotSuccessRateLearningAbility() {
        List<Double> successRates = simulationStats.getSuccessRates();
        List<Double> avgLearningAbilities = simulationStats.getAvgLearningAbilities();

        XYSeries successRateSeries = new XYSeries("Success Rate");
        for (int i = 0; i < successRates.size(); i++) {
            successRateSeries.add(i + 1, successRates.get(i));
        }

        XYSeries learningAbilitySeries = new XYSeries("Average Learning Ability");
        for (int i = 0; i < avgLearningAbilities.size(); i++) {
            learningAbilitySeries.add(i, avgLearningAbilities.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(successRateSeries);
        dataset.addSeries(learningAbilitySeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Success Rate and Average Learning Ability Over Iterations",
                "Iteration",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        plot.getRangeAxis().setRange(0.0, 1.2);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.GREEN);
        plot.setRenderer(renderer);

        JFrame frame = new JFrame("Simulation Results");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(new ChartPanel(chart), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        try {
            ChartUtils.saveChartAsPNG(new File("SuccessRateLearningAbility.png"), chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}