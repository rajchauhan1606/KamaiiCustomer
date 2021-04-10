package com.kamaii.customer.ui.models;

public class DailyModel {
    String time;
    String disttance;
    String isselected;
    public DailyModel(String time, String disttance, String isselected) {
        this.time = time;
        this.disttance = disttance;
        this.isselected = isselected;
    }


    public String getIsselected() {
        return isselected;
    }

    public void setIsselected(String isselected) {
        this.isselected = isselected;
    }

    public String getTime() {
        return time;
    }

    public String getDisttance() {
        return disttance;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDisttance(String disttance) {
        this.disttance = disttance;
    }
}

