module com.assignment.assignment {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.assignment.assignment to javafx.fxml;
    exports com.assignment.assignment;
    exports com.assignment.assignment.controller;
    opens com.assignment.assignment.controller to javafx.fxml;
    exports com.assignment.assignment.algorithms;
    opens com.assignment.assignment.algorithms to javafx.fxml;
}