module com.assignment.assignment {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.assignment.assignment to javafx.fxml;
    exports com.assignment.assignment;
}