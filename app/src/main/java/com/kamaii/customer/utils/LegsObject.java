package com.kamaii.customer.utils;

import java.util.List;

public class LegsObject {
    private List<StepsObject> steps;
    private String distance;
    private String duration;

    public LegsObject(String duration, String distance, List<StepsObject> steps) {
        this.duration = duration;
        this.distance = distance;
        this.steps = steps;
    }
    public List<StepsObject> getSteps() {
        return steps;
    }
    public String getDistance() {
        return distance;
    }
    public String getDuration() {
        return duration;
    }
}
