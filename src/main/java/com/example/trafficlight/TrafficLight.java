package com.example.trafficlight;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.geometry.Pos;


public class TrafficLight extends VBox {
    private Circle redLight;
    private Circle yellowLight;
    private Circle greenLight;
    private Rectangle trafficBody;
    private Text timerText;

    // Цвета для огней светофора
    private final Color RED_COLOR = Color.rgb(221, 46, 68);
    private final Color YELLOW_COLOR = Color.rgb(255, 204, 77);
    private final Color GREEN_COLOR = Color.rgb(119, 178, 85);
    private final Color BODY_COLOR = Color.rgb(49, 55, 61);

    private TrafficLightListener listener; // log interface

    private String[] phases = {"Red", "RedYellow", "Yellow", "Green", "FlashingGreen", "Yellow"}; // Phase array
    private int[] phaseDurations = {5, 2, 2, 5, 3, 2}; // Default phase durations
    private int currentPhaseIndex = 0; // Current Phase
    boolean isRunning = false; // Startup state
    boolean isPaused = false;  // Flag for tracking pause state
    private Timeline timeline; // Timer for phases
    private int timeRemaining; // Current phase time
    private Timeline flashingGreenTimeline; // Timer for flashing green
    private Timeline flashingYellowTimeline; // Flashing Yellow Timer


    public TrafficLight(TrafficLightListener listener) {
        this.listener = listener; // Set interfere
        initializeLights();
        initializeTimer();
        setPhase(0); // Set the initial position
    }

    private void initializeLights() { // Create and initialize all traffic light circles and time
        trafficBody = new Rectangle(100, 200); // Create a traffic light body (rectangle)
        trafficBody.setArcWidth(20);
        trafficBody.setArcHeight(20);
        trafficBody.setFill(BODY_COLOR);

        redLight = createLight(RED_COLOR);
        yellowLight = createLight(YELLOW_COLOR);
        greenLight = createLight(GREEN_COLOR);

        timerText = new Text("Time: 0");
        // Vertical container for lanterns with 15px padding between them
        VBox lightBox = new VBox(15, redLight, yellowLight, greenLight);
        lightBox.setAlignment(Pos.CENTER);

        // Wrap the lights and body in a StackPane to center them
        StackPane traffic = new StackPane();
        traffic.getChildren().addAll(trafficBody, lightBox);

        this.getChildren().addAll(traffic, timerText);
    }

    private Circle createLight(Color color) { // Create and initialize traffic light circles
        Circle light = new Circle(20);
        light.setFill(Color.GRAY); // Initially off
        return light;
    }

    private void initializeTimer() { // Initialize the state timer
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateTimer();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void updateTimer() { // Updating the timer
        if (timeRemaining > 0) {
            timeRemaining--;
            timerText.setText("Time: " + timeRemaining);
            if (listener != null) {
                listener.onPhaseChange(phases[currentPhaseIndex], timeRemaining);
            }
        } else {
            switchToNextPhase(); // If time is up, update color index
        }
    }

    private void switchToNextPhase() { // update the color index and change the color
        currentPhaseIndex = (currentPhaseIndex + 1) % phases.length;
        setPhase(currentPhaseIndex);
    }

    private void setPhase(int phaseIndex) {
        currentPhaseIndex = phaseIndex;
        timeRemaining = phaseDurations[currentPhaseIndex]; // Set the time of the current state
        resetLights(); // Reset colors
        stopFlashingGreen(); // Stop the green light from flashing if it was there

        switch (phases[phaseIndex]) {
            case "Red":
                redLight.setFill(RED_COLOR);
                break;
            case "RedYellow":
                redLight.setFill(RED_COLOR);
                yellowLight.setFill(YELLOW_COLOR);
                break;
            case "Yellow":
                yellowLight.setFill(YELLOW_COLOR);
                break;
            case "Green":
                greenLight.setFill(GREEN_COLOR);
                break;
            case "FlashingGreen":
                startFlashingGreen();
                break;
        }
    }

    private void resetLights() { // Reset all colors
        redLight.setFill(Color.GRAY);
        yellowLight.setFill(Color.GRAY);
        greenLight.setFill(Color.GRAY);
    }

    private void startFlashingGreen() { // Start green blinking
        flashingGreenTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            if (greenLight.getFill() == Color.GRAY) {
                greenLight.setFill(GREEN_COLOR);
            } else {
                greenLight.setFill(Color.GRAY);
            }
        }));
        flashingGreenTimeline.setCycleCount(Timeline.INDEFINITE);
        flashingGreenTimeline.play();
    }

    private void stopFlashingGreen() {  // Stop blinking green
        if (flashingGreenTimeline != null) {
            flashingGreenTimeline.stop();
            greenLight.setFill(Color.GRAY); // Turn off green after flashing ends
        }
    }

    private void startFlashingYellow() { // Start green Yellow
        flashingYellowTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            if (yellowLight.getFill() == Color.GRAY) {
                yellowLight.setFill(YELLOW_COLOR); // Включить желтый
            } else {
                yellowLight.setFill(Color.GRAY);   // Выключить желтый
            }
        }));
        flashingYellowTimeline.setCycleCount(Timeline.INDEFINITE); // Бесконечный цикл мигания
        flashingYellowTimeline.play(); // Запуск анимации
    }

    private void stopFlashingYellow() { // Stop blinking Yellow
        if (flashingYellowTimeline != null) {
            flashingYellowTimeline.stop(); // Остановить таймер
            yellowLight.setFill(Color.GRAY); // Выключить желтый после остановки
        }
    }

    public void start() {
        isRunning = true;
        timeline.play(); // Start the main traffic light timer
    }

    public void resume() {
        if (isPaused) {
            isRunning = true;   // Resume the traffic light
            stopFlashingYellow();
            timeline.play();    // Restarting the timer
            restoreCurrentPhaseLight(); // Restore the current light
            isPaused = false;   // Resetting the pause flag
        }
    }

    private void restoreCurrentPhaseLight() { // Restore the current light
        resetLights();  // Turn off all lights
        stopFlashingGreen(); // Stop the green light from flashing if it was there

        switch (phases[currentPhaseIndex]) {
            case "Red":
                redLight.setFill(RED_COLOR);
                break;
            case "RedYellow":
                redLight.setFill(RED_COLOR);
                yellowLight.setFill(YELLOW_COLOR);
                break;
            case "Yellow":
                yellowLight.setFill(YELLOW_COLOR);
                break;
            case "Green":
                greenLight.setFill(GREEN_COLOR);
                break;
            case "FlashingGreen":
                startFlashingGreen();
                break;
        }
    }

    public void stop() {
        if (isRunning) {
            isPaused = true;    // Flag that the traffic light is paused
            timeline.stop();     // Stop the main timer
            stopFlashingGreen(); // Stop green blinking if active
            resetLights(); // Reset colors
            startFlashingYellow(); // startFlashingYellow(); If the traffic light is off, the yellow light flashes

        }
        isRunning = false;  // Please note that the traffic light is not running
    }

    public void setPhaseDuration(int phaseIndex, int duration) {
        phaseDurations[phaseIndex] = duration;
    }
}
