package com.example.trafficlight;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TrafficLightApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        TrafficLight trafficLight = new TrafficLight(new TrafficLightListener() { // TrafficLight class
            @Override
            public void onPhaseChange(String newPhase, int remainingTime) {
                System.out.println("Phase changed to: " + newPhase + ", Remaining time: " + remainingTime);
            }
        });

        // UI to control phase durations with labeled sliders
        VBox redDurationSlider = createDurationSlider("Red Duration", 5); // Red Slider + label
        VBox redYellowDurationSlider = createDurationSlider("Red-Yellow Duration", 2); // redYellow Slider + label
        VBox yellowDurationSlider1 = createDurationSlider("Yellow Duration (Before Green)", 2); // yellow Slider + label
        VBox greenDurationSlider = createDurationSlider("Green Duration", 5); // green Slider + label
        VBox flashingGreenDurationSlider = createDurationSlider("Flashing Green Duration", 3); // flashingGreen Slider + label
        VBox yellowDurationSlider2 = createDurationSlider("Yellow Duration (After Green)", 2); // yellow Slider + label



        Button stopButton = new Button("Stop Traffic Light");
        stopButton.setOnAction(event -> {
            trafficLight.stop();  // Stop and pause
        });

        VBox root = new VBox(10, trafficLight, redDurationSlider, redYellowDurationSlider, yellowDurationSlider1, greenDurationSlider, flashingGreenDurationSlider, yellowDurationSlider2, startButton, stopButton);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root, 350, 720);
        primaryStage.setTitle("Traffic Light");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icons/traffic-light.png")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // Creating a Slider and its Text
    private VBox createDurationSlider(String labelText, int defaultValue) {
        Label label = new Label(labelText);
        Slider slider = new Slider(1, 10, defaultValue);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setSnapToTicks(true);

        VBox vbox = new VBox(5, label, slider); // Label + Slider with spacing
        return vbox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
