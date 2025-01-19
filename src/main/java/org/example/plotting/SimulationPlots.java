package org.example.plotting;

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
import java.util.List;

public class SimulationPlots {
    private static final String OUTPUT_DIR = "out/";
    private static String folderName = "";

    public static void setFolderName(String folder) {
        folderName = folder;
    }

    private static String getOutputPath(String fileName) {
        return OUTPUT_DIR + folderName + "/" + fileName;
    }

    private static void saveChartAsPNG(JFreeChart chart, String fileName, int width, int height) {
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

    public static void plotTwoSeriesOverIterations(List<Double> series1, List<Double> series2, String title, String xAxisLabel, String yAxisLabel, int series1Shift, int series2Shift, String series1Name, String series2Name, Double rangeMin, Double rangeMax) {
        XYSeries xySeries1 = new XYSeries(series1Name);
        for (int i = 0; i < series1.size(); i++) {
            xySeries1.add(i + series1Shift, series1.get(i));
        }

        XYSeries xySeries2 = new XYSeries(series2Name);
        for (int i = 0; i < series2.size(); i++) {
            xySeries2.add(i + series2Shift, series2.get(i));
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

        saveChartAsPNG(chart, title.replaceAll(" ", "") + ".png", 800, 600);
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

        saveChartAsPNG(chart, title.replaceAll(" ", "") + ".png", 800, 600);
    }

    public static void plotSeriesAsDependentOnAnother(List<Double> xData, List<Double> yData, String title, String xAxisLabel, String yAxisLabel, String seriesName, Double rangeMin, Double rangeMax) {
        // Prepend null values to yData until it is of the same size as xData
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
        renderer.setSeriesShapesVisible(0, false);

        plot.setRenderer(renderer);

        // Set background color to white
        plot.setBackgroundPaint(Color.WHITE);

        // Set grid lines color to grey
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        saveChartAsPNG(chart, title.replaceAll(" ", "") + ".png", 800, 600);
    }
}