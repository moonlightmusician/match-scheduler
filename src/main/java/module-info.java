module matchscheduler {
    requires javafx.controls;
    requires javafx.fxml;


    opens matchscheduler to javafx.fxml;
    exports matchscheduler;
    exports matchscheduler.exceptions;
    opens matchscheduler.exceptions to javafx.fxml;
}