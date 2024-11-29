package com.assignment.assignment.controller;

import com.assignment.assignment.algorithms.SortingAlgorithm;
import com.assignment.assignment.algorithms.*;
import com.assignment.assignment.util.AlertDialogUtil;
import com.assignment.assignment.util.ChartUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AnalysisScreenController {

    @FXML
    private Button mergeSortButton;
    @FXML
    private Button quickSortButton;
    @FXML
    private Button insertionSortButton;
    @FXML
    private Button shellSortButton;
    @FXML
    private Button heapSortButton;
    @FXML
    private Button showBestButton;
    @FXML
    private Label mergeSortTimeLabel;
    @FXML
    private Label quickSortTimeLabel;
    @FXML
    private Label insertionSortTimeLabel;
    @FXML
    private Label shellSortTimeLabel;
    @FXML
    private Label heapSortTimeLabel;

    @FXML
    private TableView<Double> columnTableView;

    @FXML
    private BarChart<String, Number> sortingTimeBarChart;

    private final ChartUtil chartUtil = new ChartUtil();
    private final SortingAlgorithm mergeSort = new MergeSort();
    private final SortingAlgorithm quickSort = new QuickSort();
    private final SortingAlgorithm insertionSort = new InsertionSort();
    private final SortingAlgorithm shellSort = new ShellSort();
    private final SortingAlgorithm heapSort = new HeapSort();

    private double mergeSortTime = Double.MAX_VALUE;
    private double quickSortTime = Double.MAX_VALUE;
    private double insertionSortTime = Double.MAX_VALUE;
    private double shellSortTime = Double.MAX_VALUE;
    private double heapSortTime = Double.MAX_VALUE;

    public void setColumnData(String columnHeader, ObservableList<Double> columnData) {
        TableColumn<Double, Double> column = new TableColumn<>(columnHeader);
        column.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue()));
        column.setPrefWidth(columnTableView.getPrefWidth() - 20);

        columnTableView.getColumns().clear();
        columnTableView.getColumns().add(column);
        columnTableView.setItems(columnData);
    }

    @FXML
    public void initialize() {
        mergeSortButton.setOnAction(event -> {
            mergeSortTime = performSort(mergeSort, mergeSortTimeLabel);
            updateBarChart();
        });
        quickSortButton.setOnAction(event -> {
            quickSortTime = performSort(quickSort, quickSortTimeLabel);
            updateBarChart();
        });
        insertionSortButton.setOnAction(event -> {
            insertionSortTime = performSort(insertionSort, insertionSortTimeLabel);
            updateBarChart();
        });
        shellSortButton.setOnAction(event -> {
            shellSortTime = performSort(shellSort, shellSortTimeLabel);
            updateBarChart();
        });
        heapSortButton.setOnAction(event -> {
            heapSortTime = performSort(heapSort, heapSortTimeLabel);
            updateBarChart();
        });

        showBestButton.setOnAction(event -> showBestAlgorithm());
    }

    private void showBestAlgorithm() {
        double[] sortingTimes = {
                mergeSortTime,
                quickSortTime,
                insertionSortTime,
                shellSortTime,
                heapSortTime
        };

        String[] algorithmNames = {
                mergeSort.getName(),
                quickSort.getName(),
                insertionSort.getName(),
                shellSort.getName(),
                heapSort.getName()
        };

        int bestAlgorithmIndex = chartUtil.findBestAlgorithm(sortingTimes, algorithmNames);

        if (bestAlgorithmIndex == -1) {
            AlertDialogUtil.showWarning("Click on sorting algorithm buttons to measure their performance.");
            return;
        }

        AlertDialogUtil.showInformation(String.format(
                "The fastest algorithm is %s\nTime taken: %.2f ms",
                algorithmNames[bestAlgorithmIndex],
                sortingTimes[bestAlgorithmIndex]
        ));
    }

    private void updateBarChart() {
        chartUtil.updateSortingPerformanceChart(
                sortingTimeBarChart,
                mergeSortTime,
                quickSortTime,
                insertionSortTime,
                shellSortTime,
                heapSortTime,
                mergeSort.getName(),
                quickSort.getName(),
                insertionSort.getName(),
                shellSort.getName(),
                heapSort.getName()
        );
    }

    private double performSort(SortingAlgorithm algorithm, Label timeLabel) {
        double[] dataArray = getDataArrayFromTable();
        long duration = measureSortingTime(() -> algorithm.sort(dataArray));
        double timeInMs = duration / 1_000_000.0;
        timeLabel.setText(String.format("%.2f ms", timeInMs));
        updateTable(dataArray);
        return timeInMs;
    }

    private double[] getDataArrayFromTable() {
        ObservableList<Double> data = columnTableView.getItems();
        double[] dataArray = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            dataArray[i] = data.get(i);
        }
        return dataArray;
    }

    private void updateTable(double[] sortedData) {
        ObservableList<Double> updatedData = FXCollections.observableArrayList();
        for (double value : sortedData) {
            updatedData.add(value);
        }
        columnTableView.setItems(updatedData);
    }

    private long measureSortingTime(Runnable sortingAlgorithm) {
        long startTime = System.nanoTime();
        sortingAlgorithm.run();
        return System.nanoTime() - startTime;
    }
}