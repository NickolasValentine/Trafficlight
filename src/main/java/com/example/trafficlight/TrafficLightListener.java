package com.example.trafficlight;

public interface TrafficLightListener {
    void onPhaseChange(String newPhase, int remainingTime);
}
