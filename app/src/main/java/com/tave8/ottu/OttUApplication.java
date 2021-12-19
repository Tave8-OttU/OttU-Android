package com.tave8.ottu;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class OttUApplication extends Application {
    private static OttUApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        KakaoSdk.init(this, getResources().getString(R.string.KAKAO_APP_KEY));
    }
}
