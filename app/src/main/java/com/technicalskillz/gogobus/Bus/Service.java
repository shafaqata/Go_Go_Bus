
package com.technicalskillz.gogobus.Bus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Service {

    @SerializedName("ServiceNo")
    @Expose
    private String serviceNo;
    @SerializedName("Operator")
    @Expose
    private String operator;
    @SerializedName("NextBus")
    @Expose
    private NextBus nextBus;
    @SerializedName("NextBus2")
    @Expose
    private NextBus2 nextBus2;
    @SerializedName("NextBus3")
    @Expose
    private NextBus3 nextBus3;

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public NextBus getNextBus() {
        return nextBus;
    }

    public void setNextBus(NextBus nextBus) {
        this.nextBus = nextBus;
    }

    public NextBus2 getNextBus2() {
        return nextBus2;
    }

    public void setNextBus2(NextBus2 nextBus2) {
        this.nextBus2 = nextBus2;
    }

    public NextBus3 getNextBus3() {
        return nextBus3;
    }

    public void setNextBus3(NextBus3 nextBus3) {
        this.nextBus3 = nextBus3;
    }

}
