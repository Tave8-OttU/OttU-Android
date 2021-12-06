package com.tave8.ottu.data;

public enum Platform {
    NETFLIX(1), TIVING(2), WAVVE(3), WATCHA(4), DISNEY_PLUS(5), COUPANG_PLAY(6);

    private int platformId;

    Platform(int platformId) {
        this.platformId = platformId;
    }

    public int getPlatformId() {
        return platformId;
    }
}
