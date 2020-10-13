
package com.technicalskillz.gogobus.Bus;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusList {

    @SerializedName("odata.metadata")
    @Expose
    private String odataMetadata;
    @SerializedName("BusStopCode")
    @Expose
    private String busStopCode;
    @SerializedName("Services")
    @Expose
    private List<Service> services = null;

    public String getOdataMetadata() {
        return odataMetadata;
    }

    public void setOdataMetadata(String odataMetadata) {
        this.odataMetadata = odataMetadata;
    }

    public String getBusStopCode() {
        return busStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        this.busStopCode = busStopCode;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

}
