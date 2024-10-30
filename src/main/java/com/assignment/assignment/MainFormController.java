package com.assignment.assignment;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @FXML
    public void initialize() {
        // Initialize the button click handler
        showColumnButton.setOnAction(this::showSelectedColumn);
        updateDateTime(); // Set the current date and time on startup
    }

    public void addCSVFileOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            loadCSVFile(file);
        }
    }

    private void loadCSVFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstRow = true;
            tableView.getColumns().clear();
            data.clear();
            numericColumnComboBox.getItems().clear();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (isFirstRow) {
                    isFirstRow = false;
                    createColumns(fields);
                } else {
                    ObservableList<String> row = FXCollections.observableArrayList(Arrays.asList(fields));
                    data.add(row);
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

    // Identifies numeric columns and populates the ComboBox with their names and indices
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
                ObservableList<String> columnData = FXCollections.observableArrayList();
                String columnHeader = tableView.getColumns().get(columnIndex).getText();

                for (ObservableList<String> row : data) {
                    columnData.add(row.get(columnIndex));
                }

                openColumnWindow(columnHeader, columnData);
            } else {
                showAlert("Invalid column index. Please enter a number between 0 and " +
                        (tableView.getColumns().size() - 1));
            }
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid number");
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

    // Show selected column data in a separate window
    private void showSelectedColumnData(int columnIndex) {
        ObservableList<String> columnData = FXCollections.observableArrayList();
        String columnHeader = tableView.getColumns().get(columnIndex).getText();

        for (ObservableList<String> row : data) {
            columnData.add(row.get(columnIndex));
        }
        openColumnWindow(columnHeader, columnData);
    }

    // Open a new window to display the column data
    private void openColumnWindow(String columnHeader, ObservableList<String> columnData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/assignment/assignment/SelectedColumnViews.fxml"));
            Parent root = loader.load();

            SelectedColumnViewController controller = loader.getController();
            controller.setColumnData(columnHeader, columnData);

            Stage stage = new Stage();
            stage.setTitle("Analysis Screen - " + columnHeader);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading column view window");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        dateTimeLabel.setText(dtf.format(now));
    }
}
