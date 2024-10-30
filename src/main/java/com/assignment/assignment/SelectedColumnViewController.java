package com.assignment.assignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.Arrays;

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
    private TableView<String> columnTableView;

    @FXML
    private BarChart<String, Number> sortingTimeBarChart;

    private double mergeSortTime;
    private double quickSortTime;
    private double insertionSortTime;
    private double shellSortTime;
    private double heapSortTime;

    public void setColumnData(String columnHeader, ObservableList<String> columnData) {
        TableColumn<String, String> column = new TableColumn<>(columnHeader);
        column.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()));
        column.setPrefWidth(columnTableView.getPrefWidth() - 20);

        columnTableView.getColumns().clear();
        columnTableView.getColumns().add(column);
        columnTableView.setItems(columnData);
    }

    @FXML
    public void initialize() {
        mergeSortButton.setOnAction(event -> {
            mergeSortTime = performMergeSort();
            updateBarChart();
        });
        quickSortButton.setOnAction(event -> {
            quickSortTime = performQuickSort();
            updateBarChart();
        });
        insertionSortButton.setOnAction(event -> {
            insertionSortTime = performInsertionSort();
            updateBarChart();
        });
        shellSortButton.setOnAction(event -> {
            shellSortTime = performShellSort();
            updateBarChart();
        });
        heapSortButton.setOnAction(event -> {
            heapSortTime = performHeapSort();
            updateBarChart();
        });
    }

    private double performMergeSort() {
        String[] dataArray = getDataArrayFromTable();
        long duration = measureSortingTime(() -> mergeSort(dataArray, 0, dataArray.length - 1));
        double timeInMs = duration / 1_000_000.0;
        mergeSortTimeTextField.setText(String.format("%.2f ms", timeInMs));
        updateTable(dataArray);
        return timeInMs;
    }

    private double performQuickSort() {
        String[] dataArray = getDataArrayFromTable();
        long duration = measureSortingTime(() -> quickSort(dataArray, 0, dataArray.length - 1));
        double timeInMs = duration / 1_000_000.0;
        quickSortTimeTextField.setText(String.format("%.2f ms", timeInMs));
        updateTable(dataArray);
        return timeInMs;
    }

    private double performInsertionSort() {
        String[] dataArray = getDataArrayFromTable();
        long duration = measureSortingTime(() -> insertionSort(dataArray));
        double timeInMs = duration / 1_000_000.0;
        insertionSortTimeTextField.setText(String.format("%.2f ms", timeInMs));
        updateTable(dataArray);
        return timeInMs;
    }

    private double performShellSort() {
        String[] dataArray = getDataArrayFromTable();
        long duration = measureSortingTime(() -> shellSort(dataArray));
        double timeInMs = duration / 1_000_000.0;
        shellSortTimeTextField.setText(String.format("%.2f ms", timeInMs));
        updateTable(dataArray);
        return timeInMs;
    }

    private double performHeapSort() {
        String[] dataArray = getDataArrayFromTable();
        long duration = measureSortingTime(() -> heapSort(dataArray));
        double timeInMs = duration / 1_000_000.0;
        heapSortTimeTextField.setText(String.format("%.2f ms", timeInMs));
        updateTable(dataArray);
        return timeInMs;
    }

    private void updateBarChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sorting Times");

        series.getData().add(new XYChart.Data<>("Merge Sort", mergeSortTime));
        series.getData().add(new XYChart.Data<>("Quick Sort", quickSortTime));
        series.getData().add(new XYChart.Data<>("Insertion Sort", insertionSortTime));
        series.getData().add(new XYChart.Data<>("Shell Sort", shellSortTime));
        series.getData().add(new XYChart.Data<>("Heap Sort", heapSortTime));

        sortingTimeBarChart.getData().clear();
        sortingTimeBarChart.getData().add(series);
    }

    private String[] getDataArrayFromTable() {
        ObservableList<String> data = columnTableView.getItems();
        return data.toArray(new String[0]);
    }

    private void updateTable(String[] sortedData) {
        ObservableList<String> updatedData = FXCollections.observableArrayList(sortedData);
        columnTableView.setItems(updatedData);
    }

    private long measureSortingTime(Runnable sortingAlgorithm) {
        long startTime = System.nanoTime();
        sortingAlgorithm.run();
        return System.nanoTime() - startTime;
    }

    private void mergeSort(String[] array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);
            merge(array, left, middle, right);
        }
    }

    private void merge(String[] array, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        String[] leftArray = new String[n1];
        String[] rightArray = new String[n2];

        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, middle + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArray[i].compareTo(rightArray[j]) <= 0) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }

    private void quickSort(String[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    private int partition(String[] array, int low, int high) {
        String pivot = array[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (array[j].compareTo(pivot) <= 0) {
                i++;
                String temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        String temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    private void insertionSort(String[] array) {
        int n = array.length;
        for (int i = 1; i < n; i++) {
            String key = array[i];
            int j = i - 1;

            while (j >= 0 && array[j].compareTo(key) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    private void shellSort(String[] array) {
        int n = array.length;

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                String temp = array[i];
                int j;
                for (j = i; j >= gap && array[j - gap].compareTo(temp) > 0; j -= gap) {
                    array[j] = array[j - gap];
                }
                array[j] = temp;
            }
        }
    }

    private void heapSort(String[] array) {
        int n = array.length;

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(array, n, i);

        // One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            String temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            // call max heapify on the reduced heap
            heapify(array, i, 0);
        }
    }

    private void heapify(String[] array, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && array[left].compareTo(array[largest]) > 0)
            largest = left;

        if (right < n && array[right].compareTo(array[largest]) > 0)
            largest = right;

        if (largest != i) {
            String swap = array[i];
            array[i] = array[largest];
            array[largest] = swap;

            heapify(array, n, largest);
        }
    }
}