package com.tave8.ottu.data;

public class SimpleCommunityInfo {
    private int platformIdx;
    private String platformName;
    private String latestContent;

    public SimpleCommunityInfo(int platformIdx, String platformName, String latestContent) {
        this.platformIdx = platformIdx;
        this.platformName = platformName;
        this.latestContent = latestContent;
    }

    public int getPlatformIdx() {
        return platformIdx;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getLatestContent() {
        return latestContent;
    }
}
