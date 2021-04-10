package com.kamaii.customer.DTO;

import java.io.Serializable;

public class TaxiRouteData implements Serializable {


    private double latitude;
    private double longitude;
    private String time;
    private long distance;
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }


    public TaxiRouteData() {
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
