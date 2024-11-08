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
    private TableView<Double> columnTableView; // TableView for displaying column data

    @FXML
    private BarChart<String, Number> sortingTimeBarChart;

    private double mergeSortTime;
    private double quickSortTime;
    private double insertionSortTime;
    private double shellSortTime;
    private double heapSortTime;

    public void setColumnData(String columnHeader, ObservableList<Double> columnData) {
        // Create a TableColumn for Double values
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
        double[] dataArray = getDataArrayFromTable();
        long duration = measureSortingTime(() -> mergeSort(dataArray, 0, dataArray.length - 1));
        double timeInMs = duration / 1_000_000.0;
        mergeSortTimeTextField.setText(String.format("%.2f ms", timeInMs));
        updateTable(dataArray);
        return timeInMs;
    }

    private double performQuickSort() {
        double[] dataArray = getDataArrayFromTable();
        long duration = measureSortingTime(() -> quickSort(dataArray, 0, dataArray.length - 1));
        double timeInMs = duration / 1_000_000.0;
        quickSortTimeTextField.setText(String.format("%.2f ms", timeInMs));
        updateTable(dataArray);
        return timeInMs;
    }

    private double performInsertionSort() {
        double[] dataArray = getDataArrayFromTable();
        long duration = measureSortingTime(() -> insertionSort(dataArray));
        double timeInMs = duration / 1_000_000.0;
        insertionSortTimeTextField.setText(String.format("%.2f ms", timeInMs));
        updateTable(dataArray);
        return timeInMs;
    }

    private double performShellSort() {
        double[] dataArray = getDataArrayFromTable();
        long duration = measureSortingTime(() -> shellSort(dataArray));
        double timeInMs = duration / 1_000_000.0;
        shellSortTimeTextField.setText(String.format("%.2f ms", timeInMs));
        updateTable(dataArray);
        return timeInMs;
    }

    private double performHeapSort() {
        double[] dataArray = getDataArrayFromTable();
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

    // Sorting algorithms
    private void mergeSort(double[] array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);
            merge(array, left, middle, right);
        }
    }

    private void merge(double[] array, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        double[] leftArray = new double[n1];
        double[] rightArray = new double[n2];

        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, middle + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
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

    private void quickSort(double[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    private int partition(double[] array, int low, int high) {
        double pivot = array[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                double temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        double temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    private void insertionSort(double[] array) {
        for (int i = 1; i < array.length; i++) {
            double key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = key;
        }
    }

    private void shellSort(double[] array) {
        int n = array.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                double temp = array[i];
                int j = i;
                while (j >= gap && array[j - gap] > temp) {
                    array[j] = array[j - gap];
                    j -= gap;
                }
                array[j] = temp;
            }
        }
    }

    private void heapSort(double[] array) {
        int n = array.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            double temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            heapify(array, i, 0);
        }
    }

    private void heapify(double[] array, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && array[left] > array[largest]) {
            largest = left;
        }
        if (right < n && array[right] > array[largest]) {
            largest = right;
        }

        if (largest != i) {
            double temp = array[i];
            array[i] = array[largest];
            array[largest] = temp;

            heapify(array, n, largest);
        }
    }
}
