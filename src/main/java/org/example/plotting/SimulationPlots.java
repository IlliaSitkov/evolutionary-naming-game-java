package org.example.plotting;

import org.example.export.IOUtils;
import org.example.stats.SimulationStats;
import org.example.strategies.pCommunication.PCommunicationStrategy;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulationPlots {
    private static final String OUTPUT_DIR = "out/";
    private final String folderName;

    public SimulationPlots(String folder) {
        this.folderName = folder;
    }

    private String getOutputPath(String fileName) {
        return OUTPUT_DIR + folderName + "/" + fileName;
    }

    private void saveChartAsPNG(JFreeChart chart, String fileName, int width, int height) {
        try {
            File file = new File(getOutputPath(fileName));
            File directory = file.getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            ChartUtils.saveChartAsPNG(file, chart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void plotTwoSeriesOverIterations(List<Double> series1, List<Double> series2, String title, String xAxisLabel, String yAxisLabel, int series1Shift, int series2Shift, String series1Name, String series2Name, Double rangeMin, Double rangeMax) {
        plotTwoSeriesOverIterations(series1, series2, title, xAxisLabel, yAxisLabel, series1Shift, series2Shift, series1Name, series2Name, rangeMin, rangeMax, series1.size());
    }

    public void plotTwoSeriesOverIterations(List<Double> series1, List<Double> series2, String title, String xAxisLabel, String yAxisLabel, int series1Shift, int series2Shift, String series1Name, String series2Name, Double rangeMin, Double rangeMax, int iterations) {
        XYSeries xySeries1 = new XYSeries(series1Name);
        for (int i = 0; i < iterations; i++) {
            Double elem = null;
            if (i < series1.size()) {
                elem = series1.get(i);
            }
            xySeries1.add(i + series1Shift, elem);
        }

        XYSeries xySeries2 = new XYSeries(series2Name);
        for (int i = 0; i < iterations; i++) {
            Double elem = null;
            if (i < series2.size()) {
                elem = series2.get(i);
            }
            xySeries2.add(i + series2Shift, elem);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(xySeries1);
        dataset.addSeries(xySeries2);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
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
        renderer.setSeriesPaint(0, Color.GREEN);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);

        plot.setRenderer(renderer);

        // Set background color to white
        plot.setBackgroundPaint(Color.WHITE);

        // Set grid lines color to grey
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        saveChartAsPNG(chart, title.replaceAll(" ", "") + ".png", 800, 600);
    }


    public void plotTwoSeriesOverThird(List<Double> xData, List<Double> series1, List<Double> series2, String title, String xAxisLabel, String yAxisLabel, int series1Shift, int series2Shift, String series1Name, String series2Name, Double rangeMin, Double rangeMax, int iterations) {
        XYSeries xySeries1 = new XYSeries(series1Name);
        for (int i = 0; i < iterations; i++) {
            Double elem = null;
            if (i < series1.size()) {
                elem = series1.get(i);
            }
            xySeries1.add(xData != null && i < xData.size() ? xData.get(i) : i + series1Shift, elem);
        }
    
        XYSeries xySeries2 = new XYSeries(series2Name);
        for (int i = 0; i < iterations; i++) {
            Double elem = null;
            if (i < series2.size()) {
                elem = series2.get(i);
            }
            xySeries2.add(xData != null && i < xData.size() ? xData.get(i) : i + series2Shift, elem);
        }
    
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(xySeries1);
        dataset.addSeries(xySeries2);
    
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
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
        renderer.setSeriesPaint(0, Color.GREEN);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
    
        plot.setRenderer(renderer);
    
        // Set background color to white
        plot.setBackgroundPaint(Color.WHITE);
    
        // Set grid lines color to grey
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);
    
        saveChartAsPNG(chart, title.replaceAll(" ", "") + ".png", 800, 600);
    }
    
    public <T extends Number> void plotStat(List<T> data, String title, String seriesName, String yAxisLabel, int startIteration) {
        plotStat(data, title, seriesName, yAxisLabel, startIteration, null, null);
    }

    public <T extends Number> void plotStat(List<T> data, String title, String seriesName, String yAxisLabel, int startIteration, Double rangeMin, Double rangeMax) {
        XYSeries series = new XYSeries(seriesName);
        for (int i = 0; i < data.size(); i++) {
            T value = data.get(i);
            if (value == null) {
                series.add(i + startIteration, null);
            } else {
                series.add(i + startIteration, data.get(i).doubleValue());
            }
            
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

        saveChartAsPNG(chart, title.replaceAll(" ", "") + ".png", 800, 600);
    }

    public void plotSeriesAsDependentOnAnother(List<Double> xData, List<Double> yData, String title, String xAxisLabel, String yAxisLabel, String seriesName, Double rangeMin, Double rangeMax) {
        plotSeriesAsDependentOnAnother(xData, yData, title, xAxisLabel, yAxisLabel, seriesName, rangeMin, rangeMax, false);
    }

    public void plotSeriesAsDependentOnAnother(List<Double> xData, List<Double> yData, String title, String xAxisLabel, String yAxisLabel, String seriesName, Double rangeMin, Double rangeMax, boolean showMarkers) {
        xData = new ArrayList<>(xData);
        yData = new ArrayList<>(yData);
        
        while (yData.size() < xData.size()) {
            yData.add(0, null);
        }

        XYSeries series = new XYSeries(seriesName);
        for (int i = 0; i < xData.size(); i++) {
            series.add(xData.get(i), yData.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
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
        renderer.setSeriesShapesVisible(0, showMarkers);

        plot.setRenderer(renderer);

        // Set background color to white
        plot.setBackgroundPaint(Color.WHITE);

        // Set grid lines color to grey
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        saveChartAsPNG(chart, title.replaceAll(" ", "") + ".png", 800, 600);
    }

    public void saveSimulationStats(SimulationStats simulationStats, PCommunicationStrategy strategy, int nIters) {

        List<Double> pCommunicationOverIterations = SimulationStats.getPCommunicationOverIterations(strategy, nIters);

        plotTwoSeriesOverIterations(simulationStats.getAvgLearningAbilities(),
                simulationStats.getSuccessRates(), "l_ab_&_s_rate_over_iterations", "Iteration",
                "Value", 0, 1, "Learning Ability", "Success Rate", 0.0, 1.2, nIters);

        plotTwoSeriesOverThird(pCommunicationOverIterations, simulationStats.getAvgLearningAbilities(),
                simulationStats.getSuccessRates(), "l_ab_&_s_rate_over_p_comm", "P_Communication",
                "Value", 0, 1, "Learning Ability", "Success Rate", 0.0, 1.2, nIters);

        plotStat(simulationStats.getSuccessRates(), "s_rate_over_iterations", "Success Rate",
                "Success Rate", 1, 0.0, 1.2);

        plotStat(simulationStats.getAvgLearningAbilities(), "l_ab_over_iterations",
                "Average Learning Ability", "Learning Ability", 0, 0.0, 1.2);

        plotStat(simulationStats.getLanguagesNumber(), "languages_number",
                "Languages Number", "Languages Number", 0);

        plotStat(simulationStats.getCommunicationsNumber(), "communications_number",
                "Communications Number", "Communications Number", 1);

        plotStat(simulationStats.getAvgAges(), "avg_age", "Average Age", "Age", 0);
        
        plotStat(simulationStats.getKilledAgentsNumber(), "killed_agents",
                "Killed Agents Number", "Killed Agents Number", 1);

        plotStat(simulationStats.getAvgKnowledge(), "avg_knowledge",
                "Avg Knowledge", "Avg Knowledge", 0);

        plotStat(simulationStats.getBornAgentsNumber(), "born_agents",
                "Number Of Born Agents", "Number Of Born Agents", 1);
        plotStat(simulationStats.getNAgentsAlive(), "alive_agents", "Number Of Alive Agents",
                "Number Of Alive Agents", 0);
                
        plotStat(simulationStats.getLearningAbilityLanguageARI(), "l_ab_lang_ari", "Learning Ability - Language ARI",
                "Learning Ability - Language ARI", 0, -1.05, 1.05);

        plotStat(simulationStats.getLearningAbilityLanguageRI(), "l_ab_lang_ri", "Learning Ability - Language RI",
                "Learning Ability - Language RI", 0, 0.0, 1.05);

        plotTwoSeriesOverThird(
            pCommunicationOverIterations,
            simulationStats.getLearningAbilityLanguageARI(),
            simulationStats.getLearningAbilityLanguageRI(),
            "l_ab_lang_ari_&_ri_over_p_comm",
            "P_Communication",
            "Value",
            0, 0, "Learning Ability - Language ARI", "Learning Ability - Language RI",
            -1.05, 1.05, pCommunicationOverIterations.size()
        );

        plotTwoSeriesOverIterations(
            simulationStats.getLearningAbilityLanguageARI(),
            simulationStats.getLearningAbilityLanguageRI(),
            "l_ab_lang_ari_&_ri_over_iterations",
            "Iteration",
            "Value",
            0, 0, "Learning Ability - Language ARI", "Learning Ability - Language RI",
            -1.05, 1.05, pCommunicationOverIterations.size()
        );

        plotSeriesAsDependentOnAnother(
                pCommunicationOverIterations,
                simulationStats.getLearningAbilityLanguageARI(),
                "l_ab_lang_ari_over_p_comm",
                "P_Communication",
                "Learning Ability - Language ARI",
                "Learning Ability - Language ARI",
                -1.05, 1.05);

        plotSeriesAsDependentOnAnother(
                pCommunicationOverIterations,
                simulationStats.getLearningAbilityLanguageRI(),
                "l_ab_lang_ri_over_p_comm",
                "P_Communication",
                "Learning Ability - Language RI",
                "Learning Ability - Language RI",
                0.0, 1.05);
        
        plotSeriesAsDependentOnAnother(
                pCommunicationOverIterations,
                simulationStats.getSuccessRates(),
                "s_rate_over_p_comm",
                "P_Communication",
                "Success rate",
                "Success rate",
                0.0, 1.2);

        plotSeriesAsDependentOnAnother(
                pCommunicationOverIterations,
                simulationStats.getAvgLearningAbilities(),
                "l_ab_over_p_comm",
                "P_Communication",
                "Learning ability",
                "Learning ability",
                0.0, 1.2);

        plotSeriesAsDependentOnAnother(
                simulationStats.getAvgLearningAbilities(),
                simulationStats.getAvgKnowledge(),
                "knowledge_over_learning_ability",
                "Learning ability",
                "Knowledge",
                "Knowledge",
                null, null);

        plotSeriesAsDependentOnAnother(
                simulationStats.getAvgKnowledge(),
                simulationStats.getSuccessRates(),
                "s_rate_over_knowledge",
                "Knowledge",
                "Success Rate",
                "Success Rate",
                null, null);

        IOUtils.exportToJson(simulationStats.getLanguageMaps(), getOutputPath("/language_maps.json"));
        IOUtils.exportToJson(simulationStats.getLearningAbilityMaps(), getOutputPath("/learning_ability_maps.json"));
    }
}