package com.assignment.assignment.controller;

import com.assignment.assignment.algorithms.SortingAlgorithm;
import com.assignment.assignment.algorithms.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class SelectedColumnViewController {

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
    private TextField mergeSortTimeTextField;
    @FXML
    private TextField quickSortTimeTextField;
    @FXML
    private TextField insertionSortTimeTextField;
    @FXML
    private TextField shellSortTimeTextField;
    @FXML
    private TextField heapSortTimeTextField;

    @FXML
    private TableView<Double> columnTableView;

    @FXML
    private BarChart<String, Number> sortingTimeBarChart;

    private final SortingAlgorithm mergeSort = new MergeSort();
    private final SortingAlgorithm quickSort = new QuickSort();
    private final SortingAlgorithm insertionSort = new InsertionSort();
    private final SortingAlgorithm shellSort = new ShellSort();
    private final SortingAlgorithm heapSort = new HeapSort();

    private double mergeSortTime;
    private double quickSortTime;
    private double insertionSortTime;
    private double shellSortTime;
    private double heapSortTime;

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
            mergeSortTime = performSort(mergeSort, mergeSortTimeTextField);
            updateBarChart();
        });
        quickSortButton.setOnAction(event -> {
            quickSortTime = performSort(quickSort, quickSortTimeTextField);
            updateBarChart();
        });
        insertionSortButton.setOnAction(event -> {
            insertionSortTime = performSort(insertionSort, insertionSortTimeTextField);
            updateBarChart();
        });
        shellSortButton.setOnAction(event -> {
            shellSortTime = performSort(shellSort, shellSortTimeTextField);
            updateBarChart();
        });
        heapSortButton.setOnAction(event -> {
            heapSortTime = performSort(heapSort, heapSortTimeTextField);
            updateBarChart();
        });
    }

    private double performSort(SortingAlgorithm algorithm, TextField timeTextField) {
        double[] dataArray = getDataArrayFromTable();
        long duration = measureSortingTime(() -> algorithm.sort(dataArray));
        double timeInMs = duration / 1_000_000.0;
        timeTextField.setText(String.format("%.2f ms", timeInMs));
        updateTable(dataArray);
        return timeInMs;
    }

    private void updateBarChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sorting Times");

        series.getData().add(new XYChart.Data<>(mergeSort.getName(), mergeSortTime));
        series.getData().add(new XYChart.Data<>(quickSort.getName(), quickSortTime));
        series.getData().add(new XYChart.Data<>(insertionSort.getName(), insertionSortTime));
        series.getData().add(new XYChart.Data<>(shellSort.getName(), shellSortTime));
        series.getData().add(new XYChart.Data<>(heapSort.getName(), heapSortTime));

        sortingTimeBarChart.getData().clear();
        sortingTimeBarChart.getData().add(series);
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