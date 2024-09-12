module org.example.drawing {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.example.drawing to javafx.fxml;
    exports org.example.drawing;
}