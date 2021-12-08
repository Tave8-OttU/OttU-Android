package com.tave8.ottu.data;

import androidx.annotation.NonNull;

import com.tave8.ottu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SingletonPlatform {
    private static SingletonPlatform platform;
    @NonNull private HashMap<Integer, Integer> platformLogoList;                    //platformId로 찾음
    @NonNull private HashMap<Integer, String> platformNameList;                     //platformId로 찾음
    @NonNull private HashMap<Integer, ArrayList<RatePlanInfo>> platformInfoList;    //platformId로 찾음

    private SingletonPlatform() {
        platformLogoList = new HashMap<>();
        platformLogoList.put(1, R.drawable.icon_ott_netflix);
        platformLogoList.put(2, R.drawable.icon_ott_tving);
        platformLogoList.put(3, R.drawable.icon_ott_wavve);
        platformLogoList.put(4, R.drawable.icon_ott_watcha);
        platformLogoList.put(5, R.drawable.icon_ott_disney);
        platformLogoList.put(6, R.drawable.icon_ott_coupang_play);

        platformNameList = new HashMap<>();
        platformNameList.put(1, "넷플릭스");
        platformNameList.put(2, "티빙");
        platformNameList.put(3, "웨이브");
        platformNameList.put(4, "왓챠");
        platformNameList.put(5, "디즈니 플러스");
        platformNameList.put(6, "쿠팡 플레이");

        platformInfoList = new HashMap<>();
        ArrayList<RatePlanInfo> netflixRatePlanInfoList = new ArrayList<>();
        netflixRatePlanInfoList.add(new RatePlanInfo("베이식", 1, 9500));
        netflixRatePlanInfoList.add(new RatePlanInfo("스탠다드", 2, 13500));
        netflixRatePlanInfoList.add(new RatePlanInfo("프리미엄", 4, 17000));
        platformInfoList.put(1, netflixRatePlanInfoList);

        ArrayList<RatePlanInfo> tvingRatePlanInfoList = new ArrayList<>();
        tvingRatePlanInfoList.add(new RatePlanInfo("베이직", 1, 7900));
        tvingRatePlanInfoList.add(new RatePlanInfo("스탠다드", 2, 10900));
        tvingRatePlanInfoList.add(new RatePlanInfo("프리미엄", 4, 13900));
        platformInfoList.put(2, tvingRatePlanInfoList);

        ArrayList<RatePlanInfo> wavveRatePlanInfoList = new ArrayList<>();
        wavveRatePlanInfoList.add(new RatePlanInfo("Basic", 1, 7900));
        wavveRatePlanInfoList.add(new RatePlanInfo("Standard", 2, 10900));
        wavveRatePlanInfoList.add(new RatePlanInfo("Premium", 4, 13900));
        platformInfoList.put(3, wavveRatePlanInfoList);

        ArrayList<RatePlanInfo> watchaRatePlanInfoList = new ArrayList<>();
        watchaRatePlanInfoList.add(new RatePlanInfo("베이직", 1, 7900));
        watchaRatePlanInfoList.add(new RatePlanInfo("프리미엄", 4, 12900));
        platformInfoList.put(4, watchaRatePlanInfoList);

        ArrayList<RatePlanInfo> disneyPlusRatePlanInfoList = new ArrayList<>();
        disneyPlusRatePlanInfoList.add(new RatePlanInfo("Disney+", 4, 9900));
        platformInfoList.put(5, disneyPlusRatePlanInfoList);

        ArrayList<RatePlanInfo> coupangPlayRatePlanInfoList = new ArrayList<>();
        coupangPlayRatePlanInfoList.add(new RatePlanInfo("쿠팡 와우 멤버십", 2, 2900));
        platformInfoList.put(6, coupangPlayRatePlanInfoList);
    }

    public static SingletonPlatform getPlatform() {
        if (platform == null) {
            platform = new SingletonPlatform();
        }
        return platform;
    }

    @NonNull
    public HashMap<Integer, Integer> getPlatformLogoList() {
        return platformLogoList;
    }

    @NonNull
    public HashMap<Integer, String> getPlatformNameList() {
        return platformNameList;
    }

    @NonNull
    public HashMap<Integer, ArrayList<RatePlanInfo>> getPlatformInfoList() {
        return platformInfoList;
    }

    public RatePlanInfo getPlatformRatePlanInfo(int platformId, int headCount) {
        ArrayList<RatePlanInfo> platformRatePlanInfoList = getPlatformInfoList().get(platformId);
        for (RatePlanInfo ratePlanInfo : Objects.requireNonNull(platformRatePlanInfoList)) {
            if (ratePlanInfo.getHeadCount() == headCount)
                return ratePlanInfo;
        }
        return null;
    }
}
