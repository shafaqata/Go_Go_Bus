package com.technicalskillz.gogobus.BusStation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Station {

    private double distance;

    private String busStopCode;

    private String roadName;

    private String description;

    private Double latitude;

    private Double longitude;

    public Station(double distance, String busStopCode, String roadName, String description, Double latitude, Double longitude) {
        this.distance = distance;
        this.busStopCode = busStopCode;
        this.roadName = roadName;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getBusStopCode() {
        return busStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        this.busStopCode = busStopCode;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
