module com.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens chatApp to javafx.fxml;
    exports chatApp;
}
