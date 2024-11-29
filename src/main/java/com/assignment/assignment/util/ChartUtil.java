package com.assignment.assignment.util;

import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

public class ChartUtil {


    public void updateSortingPerformanceChart(
            BarChart<String, Number> barChart,
            double mergeSortTime,
            double quickSortTime,
            double insertionSortTime,
            double shellSortTime,
            double heapSortTime,
            String mergeSort,
            String quickSort,
            String insertionSort,
            String shellSort,
            String heapSort) {

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sorting Times");

        if (mergeSortTime != Double.MAX_VALUE) {
            series.getData().add(new XYChart.Data<>(mergeSort, mergeSortTime));
        }
        if (quickSortTime != Double.MAX_VALUE) {
            series.getData().add(new XYChart.Data<>(quickSort, quickSortTime));
        }
        if (insertionSortTime != Double.MAX_VALUE) {
            series.getData().add(new XYChart.Data<>(insertionSort, insertionSortTime));
        }
        if (shellSortTime != Double.MAX_VALUE) {
            series.getData().add(new XYChart.Data<>(shellSort, shellSortTime));
        }
        if (heapSortTime != Double.MAX_VALUE) {
            series.getData().add(new XYChart.Data<>(heapSort, heapSortTime));
        }

        barChart.getData().clear();
        barChart.getData().add(series);
    }

    public int findBestAlgorithm(double[] sortingTimes, String[] algorithmNames) {
        int bestAlgorithmIndex = -1;
        double bestTime = Double.MAX_VALUE;

        for (int i = 0; i < sortingTimes.length; i++) {
            if (sortingTimes[i] < bestTime && sortingTimes[i] != Double.MAX_VALUE) {
                bestTime = sortingTimes[i];
                bestAlgorithmIndex = i;
            }
        }

        return bestAlgorithmIndex;
    }
}