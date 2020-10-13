
package com.technicalskillz.gogobus.Bus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NextBusSimple {

    @SerializedName("OriginCode")
    @Expose
    private String originCode;
    @SerializedName("DestinationCode")
    @Expose
    private String destinationCode;
    @SerializedName("EstimatedArrival")
    @Expose
    private String estimatedArrival;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("VisitNumber")
    @Expose
    private String visitNumber;
    @SerializedName("Load")
    @Expose
    private String load;
    @SerializedName("Feature")
    @Expose
    private String feature;
    @SerializedName("Type")
    @Expose
    private String type;

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public String getEstimatedArrival() {
        return estimatedArrival;
    }

    public void setEstimatedArrival(String estimatedArrival) {
        this.estimatedArrival = estimatedArrival;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
