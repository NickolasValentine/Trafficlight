module com.example.trafficlight {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.trafficlight to javafx.fxml;
    exports com.example.trafficlight;
}