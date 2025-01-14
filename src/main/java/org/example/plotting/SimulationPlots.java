package org.example.plotting;

import org.example.entities.Agent;
import org.example.entities.World;
import org.example.stats.SimulationStats;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SimulationPlots {
    private final SimulationStats simulationStats;
    private static final String outputDir = "out/";

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
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);

        plot.setRenderer(renderer);

        // Set background color to white
        plot.setBackgroundPaint(Color.WHITE);

        // Set grid lines color to grey
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);


        try {
            ChartUtils.saveChartAsPNG(new File(outputDir + "SuccessRateLearningAbility.png"), chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Number> void plotStat(List<T> data, String title, String seriesName, String yAxisLabel, int startIteration) {
        plotStat(data, title, seriesName, yAxisLabel, startIteration, null, null);
    }

    public static <T extends Number> void plotStat(List<T> data, String title, String seriesName, String yAxisLabel, int startIteration, Double rangeMin, Double rangeMax) {
        XYSeries series = new XYSeries(seriesName);
        for (int i = 0; i < data.size(); i++) {
            series.add(i + startIteration, data.get(i).doubleValue());
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "Iteration",
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        if (rangeMin != null && rangeMax != null) {
            plot.getRangeAxis().setRange(rangeMin, rangeMax);
        }

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesShapesVisible(0, false);

        plot.setRenderer(renderer);

        // Set background color to white
        plot.setBackgroundPaint(Color.WHITE);

        // Set grid lines color to grey
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        try {
            ChartUtils.saveChartAsPNG(new File(outputDir + title.replaceAll(" ", "") + ".png"), chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final Color EMPTY_CELL_COLOR = Color.WHITE;
    private static final Color EMPTY_LEXICON_COLOR = Color.BLACK;

    public static void plotWorld(World world, String fileName) {
        int size = world.getSize();
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        Map<String, Color> languageColorMap = new HashMap<>();
        Random random = new Random();

        double[] xValues = new double[size * size];
        double[] yValues = new double[size * size];
        double[] zValues = new double[size * size];

        int index = 0;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                xValues[index] = x;
                yValues[index] = y;
                Agent agent = world.getAgentAt(x, y);
                if (agent == null) {
                    zValues[index] = EMPTY_CELL_COLOR.getRGB();
                } else if (agent.getLexicon().isEmpty()) {
                    zValues[index] = EMPTY_LEXICON_COLOR.getRGB();
                } else {
                    String topWord = agent.getLexicon().getTopWord();
                    languageColorMap.putIfAbsent(topWord, new Color(random.nextInt(0xFFFFFF)));
                    zValues[index] = languageColorMap.get(topWord).getRGB();
                }
                index++;
            }
        }

        dataset.addSeries("World", new double[][]{xValues, yValues, zValues});

        JFreeChart chart = ChartFactory.createScatterPlot(
                "World Grid",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true, false, false
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        XYBlockRenderer renderer = new XYBlockRenderer();
        renderer.setBlockWidth(1.0);
        renderer.setBlockHeight(1.0);

        // Create a custom PaintScale to map the z-values to colors
        PaintScale paintScale = new PaintScale() {
            @Override
            public double getLowerBound() {
                return 0;
            }

            @Override
            public double getUpperBound() {
                return 0xFFFFFF;
            }

            @Override
            public Paint getPaint(double value) {
                return new Color((int) value);
            }
        };

        renderer.setPaintScale(paintScale);
        plot.setRenderer(renderer);

        // Set the background color of the plot to white
        plot.setBackgroundPaint(Color.WHITE);

        try {
            ChartUtils.saveChartAsPNG(new File(outputDir + fileName + ".png"), chart, 800, 800);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}