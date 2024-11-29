package com.assignment.assignment.controller;

import com.assignment.assignment.util.AlertDialogUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class MainFormController {
    @FXML
    private TableView<ObservableList<String>> tableView;
    @FXML
    private TextField columnIndexField;
    @FXML
    private Button showColumnButton;
    @FXML
    private ComboBox<String> numericColumnComboBox;
    @FXML
    private Label dateTimeLabel;

    private ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

    private ObservableList<ObservableList<Double>> numericData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        showColumnButton.setOnAction(this::showSelectedColumn);
    }

    public void addCSVFileOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            String fileName = file.getName();
            if (fileName.endsWith(".csv")) {
                AlertDialogUtil.showConfirmation("CSV File Selected Successfully");
                loadCSVFile(file);
            } else {
                AlertDialogUtil.showError("Invalid file selected. Please select a valid CSV file.");
            }
        } else {
            AlertDialogUtil.showWarning("File selection canceled by the user");

        }
    }


    private void loadCSVFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstRow = true;
            tableView.getColumns().clear();
            data.clear();
            numericColumnComboBox.getItems().clear();
            numericData.clear(); // Reset numericData

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (isFirstRow) {
                    isFirstRow = false;
                    createColumns(fields);
                } else {
                    ObservableList<String> row = FXCollections.observableArrayList(Arrays.asList(fields));
                    data.add(row);
                    ObservableList<Double> numericRow = FXCollections.observableArrayList();
                    for (int i = 0; i < fields.length; i++) {
                        try {
                            numericRow.add(Double.parseDouble(fields[i])); // Try parsing each value as a Double
                        } catch (NumberFormatException e) {
                            numericRow.add(Double.NaN); // Add NaN if not a valid number
                        }
                    }
                    numericData.add(numericRow);
                }
            }

            tableView.setItems(data);
            identifyNumericColumns(); // Identify numeric columns after loading data

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createColumns(String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(headers[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));
            tableView.getColumns().add(column);
        }
    }

    private void identifyNumericColumns() {
        for (int colIndex = 0; colIndex < tableView.getColumns().size(); colIndex++) {

            boolean isNumeric = true;

            for (ObservableList<String> row : data) {
                String cellValue = row.get(colIndex);
                if (cellValue == null || !cellValue.matches("-?\\d+(\\.\\d+)?")) { // Check for numeric values
                    isNumeric = false;
                    break;
                }
            }

            if (isNumeric) {
                String columnName = tableView.getColumns().get(colIndex).getText();
                numericColumnComboBox.getItems().add(colIndex + " - " + columnName); // Add index and name to ComboBox
            }
        }
    }

    @FXML
    private void showSelectedColumn(ActionEvent event) {
        try {
            int columnIndex = Integer.parseInt(columnIndexField.getText());

            if (columnIndex >= 0 && columnIndex < tableView.getColumns().size()) {
                ObservableList<Double> columnData = FXCollections.observableArrayList(); // Changed to hold Doubles
                String columnHeader = tableView.getColumns().get(columnIndex).getText();

                for (ObservableList<String> row : data) {
                    try {
                        // Try parsing each cell as a Double
                        columnData.add(Double.parseDouble(row.get(columnIndex)));
                    } catch (NumberFormatException e) {
                        columnData.add(Double.NaN);
                    }
                }

                openColumnWindow(columnHeader, columnData); // Pass the Double list
            } else {
                AlertDialogUtil.showWarning("Invalid column index. Please enter a number between 0 and " +
                        (tableView.getColumns().size() - 1));
            }
        } catch (NumberFormatException e) {
            AlertDialogUtil.showWarning("Please enter a valid number");
        }
    }

    // Handle selection from the numeric column ComboBox
    @FXML
    private void handleNumericColumnSelection(ActionEvent event) {
        String selectedColumn = numericColumnComboBox.getSelectionModel().getSelectedItem();
        if (selectedColumn != null) {
            int columnIndex = Integer.parseInt(selectedColumn.split(" - ")[0]);
            showSelectedColumnData(columnIndex);
        }
    }

    private void showSelectedColumnData(int columnIndex) {
        ObservableList<Double> columnData = FXCollections.observableArrayList();
        String columnHeader = tableView.getColumns().get(columnIndex).getText();

        for (ObservableList<String> row : data) {
            try {
                columnData.add(Double.parseDouble(row.get(columnIndex)));
            } catch (NumberFormatException e) {
                columnData.add(Double.NaN);
            }
        }
        openColumnWindow(columnHeader, columnData);  // Pass the Double list
    }

    private void openColumnWindow(String columnHeader, ObservableList<Double> columnData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/assignment/assignment/AnalysisScreen.fxml"));
            Parent root = loader.load();

            AnalysisScreenController controller = loader.getController();
            controller.setColumnData(columnHeader, columnData);

            Stage stage = new Stage();
            stage.setTitle("Analysis Screen - " + columnHeader);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialogUtil.showError("Error loading column view window");
        }
    }

}
