package com.tave8.ottu.data;

public class SimpleCommunityInfo {
    private int ottID;
    private String ottName;
    private String latestContent;

    public SimpleCommunityInfo(int ottID, String ottName, String latestContent) {
        this.ottID = ottID;
        this.ottName = ottName;
        this.latestContent = latestContent;
    }

    public int getOttID() {
        return ottID;
    }

    public String getOttName() {
        return ottName;
    }

    public String getLatestContent() {
        return latestContent;
    }
}
