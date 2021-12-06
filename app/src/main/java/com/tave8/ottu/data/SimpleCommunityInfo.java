package com.tave8.ottu.data;

public class SimpleCommunityInfo {
    private int platformId;
    private String platformName;
    private String latestContent;

    public SimpleCommunityInfo(int platformId, String platformName, String latestContent) {
        this.platformId = platformId;
        this.platformName = platformName;
        this.latestContent = latestContent;
    }

    public int getPlatformId() {
        return platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getLatestContent() {
        return latestContent;
    }
}
