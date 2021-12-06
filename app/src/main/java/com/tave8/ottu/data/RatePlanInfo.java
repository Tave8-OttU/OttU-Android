package com.tave8.ottu.data;

public class RatePlanInfo {
    private String ratePlanName;
    private int headCount;
    private int charge;

    public RatePlanInfo(String ratePlanName, int headCount, int charge) {
        this.ratePlanName = ratePlanName;
        this.headCount = headCount;
        this.charge = charge;
    }

    public String getRatePlanName() {
        return ratePlanName;
    }

    public int getHeadCount() {
        return headCount;
    }

    public int getCharge() {
        return charge;
    }
}
